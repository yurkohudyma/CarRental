package ua.hudyma.servlets.order_car.user_servlets;


import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.hudyma.constants.OrderStatus;
import ua.hudyma.dao.Car;
import ua.hudyma.dao.Order;
import ua.hudyma.dao.User;
import ua.hudyma.db.DBManager;

/**
 * Servlet shows to user final bill.
 * Sets status of order to PAID when user confirms payment.
 *Сервлет показывает пользователю окончательный счет.
 *Устанавливает статус заказа на оплаченный, когда пользователь подтверждает оплату.
 * @author IC
 */
@WebServlet(name = "PaymentServlet")
public class PaymentServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(PaymentServlet.class);
	
	private static final String CAR_RENTAL = "/";
    private static final String PAYMENT_BILL_JSP = "/jsp/user/bill.jsp";
    private static final String ORDER_USER_PAGE = CAR_RENTAL+"user_order";
    private static final String PROPERTY = "local";

    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(PAYMENT_BILL_JSP).forward(req, resp);
    }

    /**
     * Sets status to PAID.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int order_id = (int) request.getSession().getAttribute("order_id");

        User user = (User) request.getSession().getAttribute("user");


        if (null != request.getParameter("actionName")) {
            String act = request.getParameter("actionName");
            if (act.equals("bill")) {
                try {
                    int paidRows = DBManager.getInstance().setOrderStatus(order_id, OrderStatus.PAID);
//

//
                    if (paidRows == 0) {
                        Locale locale = (Locale) request.getSession().getAttribute("locale");
                        ResourceBundle resourceBundle = ResourceBundle.getBundle(PROPERTY, locale);


                        String logErr = resourceBundle.getString("order.pay.error.log");
                        LOG.error(logErr + " " + String.valueOf(paidRows));
                        request.setAttribute("error", true);
                        fwd(request, response);
                    } else {
                        response.sendRedirect(ORDER_USER_PAGE);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    request.setAttribute("error", true);
                    LOG.error(e.getMessage());
                    fwd(request, response);
                }
            } else {
                fwd(request, response);
            }
        } else {
            fwd(request, response);
        }
    }

    /**
     * Shows final bill with all parameters.
     * Показывает итоговый счет со всеми параметрами.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int order_id = (int) request.getSession().getAttribute("order_id");
        try {
            Order order = DBManager.getInstance().getOrder(order_id);
            Car car = DBManager.getInstance().getCar(order.getOrderedCarId());
            request.setAttribute("order", order);
            request.setAttribute("car", car);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", true);
            LOG.error(e.getMessage());
        }

        fwd(request, response);
    }
}
