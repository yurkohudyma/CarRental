package ua.hudyma.servlets.order_car;


import org.apache.log4j.Logger;

import ua.hudyma.constants.AccessLevel;
import ua.hudyma.constants.OrderStatus;
import ua.hudyma.dao.User;
import ua.hudyma.db.DBManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Servlet sets status of order to DECLINED when
 * user or admin confirms rejecting.
 *
 * @author IC
 */
@WebServlet(name = "DecliningServlet")
public class DecliningServlet extends HttpServlet {
   
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(DecliningServlet.class);
    private static final String DECLINING_JSP = "/jsp/warnings/decline_warning.jsp";
    private static final String USER_JSP = "/user";
    private static final String ADMIN_JSP = "/admin";
    private static final String PROPERTY = "local";

    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(DECLINING_JSP).forward(req, resp);
    }

    /**
     * declines order
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String act = request.getParameter("actionName");
        int orderId = (int) request.getSession().getAttribute("order_id");
        if (act.equals("decline")) {
            fwd(request, response);
        } else if (act.equals("confirmed")) {
            try {
                User user = (User) request.getSession().getAttribute("user");
                boolean correct = declineOrder(orderId, request);
                if (correct) {
                    if (user.getAccesslevel().equals(AccessLevel.ADMIN)) {
                        response.sendRedirect(ADMIN_JSP);
                    } else {
                        response.sendRedirect(USER_JSP);
                    }
                } else {
                    request.setAttribute("error", true);
                    fwd(request, response);
                }

            } catch (SQLException e) {
                request.setAttribute("error", true);
                LOG.error(e.getMessage());
                fwd(request, response);
            }
        } else {
            fwd(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        fwd(request, response);
    }

    /**
     * @param orderId
     * @param request
     * @return if error occurred
     * @throws SQLException
     */
    private boolean declineOrder(int orderId, HttpServletRequest request) throws SQLException {
        int statusRows = DBManager.getInstance().setOrderStatus(orderId, OrderStatus.DECLINED);
        int dropCarRows = DBManager.getInstance().setOrdersCar(orderId, 0); // drop car_id
        int dropPriceRows = DBManager.getInstance().setNullPrice(orderId);
        boolean statusErr = checkResult(request, "order.set_status.result_error.log", statusRows);
        boolean setCarErr = checkResult(request, "order.drop_car.error.log", dropCarRows);
        boolean dropPriceErr = checkResult(request, "order.drop_price.error.log", dropPriceRows);
        return !(statusErr || setCarErr || dropPriceErr);
    }

    /**
     * Checks if setting parameter to db was correct.
     * Number of affected rows must be equal 1
     *
     * @param request
     * @param logKeyName - message to set in log
     * @param checkValue - checked result (must be 1 if correct)
     * @return true if error occurred
     */
    private boolean checkResult(HttpServletRequest request, String logKeyName, int checkValue) {
        if (checkValue == 0) {
            Locale locale = (Locale) request.getSession().getAttribute("locale");
            ResourceBundle resourceBundle = ResourceBundle.getBundle(PROPERTY, locale);
            String logErr = resourceBundle.getString(logKeyName);
            LOG.error(logErr + " " + String.valueOf(checkValue));
        }
        return (checkValue == 0);
    }
}
