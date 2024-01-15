package ua.hudyma.servlets.order_car.user_servlets;

import ua.hudyma.dao.Car;
import ua.hudyma.db.DBManager;
import ua.hudyma.dao.Comment;
import ua.hudyma.dao.Order;
import ua.hudyma.constants.OrderStatus;
import ua.hudyma.dao.*;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Servlet for order page for user.
 * Gets list of all comments to order.
 * Sends comments.
 *
 * @author IC
 */
@WebServlet(name = "OrderUserServlet")
public class OrderUserServlet extends HttpServlet {
    
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(OrderUserServlet.class);
	
	private static final String CAR_RENTAL = "/CarRental";
    private static final String ORDER_USER_JSP = "/jsp/user/user_order.jsp";
    private static final String ORDER_USER_PAGE = CAR_RENTAL+"/user_order";
    private static final int PER_PAGE = 10;
    private static final String PROPERTY = "local";

    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(ORDER_USER_JSP).forward(req, resp);
    }

    /**
     * Sends comments
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (null != request.getParameter("actionName")) {
            int orderId = (int) request.getSession().getAttribute("order_id");
            String act = request.getParameter("actionName");
            if (act.equals("send_comment")) {
                try {
                    User user = (User) request.getSession().getAttribute("user");
                    int affectedRows = DBManager.getInstance().setOrderComment(orderId, request.getParameter("comment"),
                            user.getUser_id());
                    boolean errorRes = checkResult(request, "order.send_comment.result_error.log", affectedRows);
                    if (errorRes) {
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

            } else if (act.equals("change_lang")) {
                response.sendRedirect(ORDER_USER_PAGE);
            } else {
                fwd(request, response);
            }
        } else {
            fwd(request, response);
        }
    }

    /**
     * Gets comments, approves order and changes its
     * status when user approves order.
     * Sets order's id as session attribute (is will
     * remain the same while we are working with this order).
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int order_id;
        if (null != request.getParameter("order_id")) {
            order_id = Integer.parseInt(request.getParameter("order_id"));
        } else {
            order_id = (int) request.getSession().getAttribute("order_id");
        }

        if (null != request.getParameter("actionName")) {
            String act = request.getParameter("actionName");
            if (act.equals("approve")) {
                try {
                    int setStatusRows = DBManager.getInstance().setOrderStatus(order_id, OrderStatus.APPROVED);
                    int setPriceRows = DBManager.getInstance().setOrderPrice(order_id);
                    request.setAttribute("order", DBManager.getInstance().getOrder(order_id));
                    boolean statusErr = checkResult(request, "order.set_status.result_error.log", setStatusRows);
                    boolean priceErr = checkResult(request, "order.set_price.error.log", setPriceRows);
                    if (statusErr || priceErr) {
                        request.setAttribute("error", true);
                    }
                } catch (SQLException e) {
                    request.setAttribute("error", e.getMessage());
                    LOG.error(e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        try {
            Order order = DBManager.getInstance().getOrder(order_id);
            if (!order.getOrderStatus().equals(OrderStatus.REQUESTED) || !(order.getOrderStatus().equals(OrderStatus.DECLINED))) {
                Car car = DBManager.getInstance().getCar(order.setOrderedCarId());
                request.setAttribute("car", car);
                System.out.println("CAR ID PROCESSED OrderUserSV 129");
            }
            request.getSession().setAttribute("order_id", order_id);
            request.setAttribute("order", order);
            int page = 1;
            if (null != request.getParameter("page")) {
                page = Integer.parseInt(request.getParameter("page"));
            }
            int offset = (page - 1) * PER_PAGE;
            List<Comment> commentList = DBManager.getInstance().getOrdersComments(order_id, PER_PAGE, offset);
            int numberOfComments = DBManager.getInstance().getNumberOfComments(order_id);
            int noOfPages = (int) Math.ceil(numberOfComments * 1.0 / PER_PAGE);
            request.setAttribute("noOfPages", noOfPages);
            request.setAttribute("currentPage", page);
            request.setAttribute("comment_list", commentList);
            if (commentList.isEmpty()) {
                request.setAttribute("no_comments", true);
            }

        } catch (SQLException e) {
            request.setAttribute("error", true);
            e.printStackTrace();
            LOG.error(e.getMessage());
        }
        fwd(request, response);
    }

    /**
     * Checks if setting parameter to database was correct.
     * Number of affected rows must not be equal to 0
     *
     * @param request
     * @param keyLogName - message to set in log
     * @param checkedValue - checked result (must not be 0 if correct)
     * @return true if error occurred
     */
    private boolean checkResult(HttpServletRequest request, String keyLogName, int checkedValue) {
        if (checkedValue == 0) {
            Locale locale = (Locale) request.getSession().getAttribute("locale");
            ResourceBundle resourceBundle = ResourceBundle.getBundle(PROPERTY, locale);
            String logErr = resourceBundle.getString(keyLogName);
            LOG.error(logErr + " " + String.valueOf(checkedValue));
        }
        return (checkedValue == 0);
    }
    
    /**Overloaded method for FLOAT price
     * 
     * @param request
     * @param keyLogName
     * @param checkedValue
     * @return
     
    private boolean checkResult(HttpServletRequest request, String keyLogName, float checkedValue) {
        if (checkedValue == 0) {
            Locale locale = (Locale) request.getSession().getAttribute("locale");
            ResourceBundle resourceBundle = ResourceBundle.getBundle(PROPERTY, locale);
            String logErr = resourceBundle.getString(keyLogName);
            LOG.error(logErr + " " + String.valueOf(checkedValue));
        }
        return (checkedValue == 0);
    }
    */
}
