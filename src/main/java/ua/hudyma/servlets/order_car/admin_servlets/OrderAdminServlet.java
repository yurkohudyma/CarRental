package ua.hudyma.servlets.order_car.admin_servlets;

import ua.hudyma.db.DBManager;

import ua.hudyma.constants.OrderStatus;
import ua.hudyma.dao.Comment;
import ua.hudyma.dao.Order;
import ua.hudyma.dao.User;
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
 * Servlet for order page for admin.
 * Gets list of all comments to order.
 * Sends comments.
 *
 * @author IC
 */
@WebServlet(name = "OrderAdminServlet")
public class OrderAdminServlet extends HttpServlet {
   
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(OrderAdminServlet.class);
    
	private static final String CAR_RENTAL = "/";
	private static final String ORDER_ADMIN_JSP = "/jsp/admin/admin_order.jsp";
    private static final String ORDER_ADMIN_PAGE = CAR_RENTAL+"admin_order";
    private static final String PROPERTY = "local";
    private static final int PER_PAGE = 10;

    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(ORDER_ADMIN_JSP).forward(req, resp);
    }

    /**
     * Sending comment or additional information about order.
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (null != request.getParameter("actionName")) {
            String act = request.getParameter("actionName");
            int order_id = (int) request.getSession().getAttribute("order_id");
            if (act.equals("send_comment")) {
                try {

                    User user = (User) request.getSession().getAttribute("user");
                    int affectedRows = DBManager.getInstance().setOrderComment(order_id, request.getParameter("comment"),
                            user.getUser_id());
                    boolean setCommentErr = checkResult(request, "order.send_comment.result_error.log",
                            affectedRows);
                    if (setCommentErr) {
                        request.setAttribute("error", true);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    request.setAttribute("error", true);
                    LOG.error(e.getMessage());
                }
            } else if (act.equals("add_info")) {
                String addInfo = request.getParameter("additional_info");
                try {
                    DBManager.getInstance().setAddInfo(order_id, addInfo);
                } catch (SQLException e) {
                    e.printStackTrace();
                    request.setAttribute("error", true);
                    LOG.error(e.getMessage());
                }
            }
            response.sendRedirect(ORDER_ADMIN_PAGE);

        } else {

            fwd(request, response);
        }
    }

    /** Here it is redirected on KEYPRESSED from admin.jsp/ ORDER details button (GET method)
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */

//     THE JAVADOC hereinbelow SEEMS TO BE MISTAKEN
//      
//     %%Displays list of all orders (by page).
//     %%Assigns car to order and changes its status.
//     %%Sets order id as session attribute (is remains
//     %%the same while working with this order).
     
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int order_id;
        try {
            Order order;
//            if (null != request.getParameter("car_id")) {
            if (null == request.getSession().getAttribute("order_id")) {
                order_id = Integer.parseInt(request.getParameter("order_id"));
                /** car_id is needed for assigning a car to the order
                //int car_id = Integer.parseInt(request.getParameter("car_id"));
                /** no need to assign car to an order. If the availability option shall be implemented, there are should be some kind of engaged cars out of A FREE POOL */
                //int setCarRows = DBManager.getInstance().setOrdersCar(order_id, car_id);
                /** as a return value this var stores NUMBER of returned by SQL rows or 0 if none 
                 * Upon keypressed Order Details, this very order in now PENDING */
                System.out.println("OrderAdminSV line 120");
                int setStatusRows = DBManager.getInstance().setOrderStatus(order_id, OrderStatus.PENDING);
                //order.set_car_id.error.log = Setting car id to order failed
                /** This var stores converted into boolean from SQL a number or 0 and logs it into LOG with i18ned message and */
                //boolean errSetCar = checkResult(request, "order.set_car_id.error.log", setCarRows);
                boolean errSetStatus = checkResult(request, "order.set_status.result_error.log", setStatusRows);
                /** if checkResult returns true, i.e. serOrderStatus returned 0, add 'error' attrib into req */ 
//                if (errSetCar) {
//                    request.setAttribute("error", true);
//                }
                /** forward error attrib, which means failure of setting order status to PENDING */
                if (errSetStatus) {
                    request.setAttribute("error", true);
                }
                
                /** if car_id attrib is NULL, order_id NULL or NOT NULL (?) */
			} else if (null == request.getSession().getAttribute("order_id") || null != request.getParameter("order_id") ) {
				System.out.println("OrderAdminSV line 137");
                order_id = Integer.parseInt(request.getParameter("order_id"));
                request.getSession().setAttribute("order_id", order_id);
            } else {
                order_id = (int) request.getSession().getAttribute("order_id");
            }
            System.out.println("OrderAdminSV line 143");
            order = DBManager.getInstance().getOrder(order_id);
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
            request.getSession().setAttribute("order_id", order_id);
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
     * Number of affected rows must not be equal 0
     *
     * @param request
     * @param logKeyName - message to set in log
     * @param checkValue - checked result (must not be 0 if correct)
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
