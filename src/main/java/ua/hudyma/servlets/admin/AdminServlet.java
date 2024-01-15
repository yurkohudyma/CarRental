package ua.hudyma.servlets.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.hudyma.constants.OrderStatus;
import ua.hudyma.dao.Order;
import ua.hudyma.db.DBManager;

/**
 * Displays list of all orders and orders
 * by their status on admin's page.
 *
 * @author ihor chekhunov
 * code comments by Yurko Hudyma
 */
@WebServlet(name = "AdminServlet")
public class AdminServlet extends HttpServlet {
    
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(AdminServlet.class);
    private static final String ADMIN_JSP = "/jsp/admin/admin.jsp";

    private static final int PER_PAGE = 10;
    private static final String PROPERTY = "local";

    /**
     * Forwards to current jsp page
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(ADMIN_JSP).forward(req, resp);
    }

    /**
     * Gets list of all orders and of orders by status.
     * Sets them partly as attribute
     * @see DBManager
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /** container for orders */
    	List<Order> ordersList;
    	/** var for pagination */
        int page = 1;
        /** if not null get the int */
        if (null != request.getParameter("page")) {
            page = Integer.parseInt(request.getParameter("page"));
        }
        /** set the range of pages */
        int offset = (page - 1) * PER_PAGE;
        /** var for defining scope of pagination */
        int numberOfOrders;
        /** var set to get the keypressed action for Orders Statuses */
        OrderStatus status = null;
        Locale locale = (Locale) request.getSession().getAttribute("locale");
        ResourceBundle resourceBundle = ResourceBundle.getBundle(PROPERTY, locale);
        String heading = resourceBundle.getString("admin.orders.heading.all");
        if (null != request.getParameter("actionName")) {
        	/** find out the button/form pressed */
            String act = request.getParameter("actionName");
            switch (act) {
                case "requested":
                    status = OrderStatus.REQUESTED;
                    heading = resourceBundle.getString("admin.orders.heading.requested");
                    break;
                case "pending":
                    status = OrderStatus.PENDING;
                    heading = resourceBundle.getString("admin.orders.heading.pending");
                    break;
                case "approved":
                    status = OrderStatus.APPROVED;
                    heading = resourceBundle.getString("admin.orders.heading.approved");
                    break;
                case "paid":
                    status = OrderStatus.PAID;
                    heading = resourceBundle.getString("admin.orders.heading.paid");
                    break;
                case "declined":
                    status = OrderStatus.DECLINED;
                    heading = resourceBundle.getString("admin.orders.heading.declined");
                    break;
            }
        }
        try {
        	/** initial page loading with ALL STATUSES display, where status is NULL */
            if (null == status) {
            	/** get a container with ALL orders PAGINATED BY 10 starting with 0 (initially) */
                ordersList = DBManager.getInstance().getAllOrders(PER_PAGE, offset);
                numberOfOrders = DBManager.getInstance().getNumberOfAllOrders();
            } else {
            	/** status is received from a keypressed, so get a container with the STATUS */
                ordersList = DBManager.getInstance().getOrdersByStatus(PER_PAGE, offset, status);
                numberOfOrders = DBManager.getInstance().getNumberOfOrdersByStatus(status);
            }
            
            /** need to get a user's e-mail and a car by number to show it in ALL ORDERS list
             * In future this data should be retrieved as FK from corr tables
             * */
            
            //User user = DBManager.getInstance().getUser(ordersList.);
            
        	/** pagination code */
            int noOfPages = (int) Math.ceil(numberOfOrders * 1.0 / PER_PAGE);
            request.setAttribute("noOfPages", noOfPages);
            request.setAttribute("currentPage", page);
            request.setAttribute("heading", heading);
            request.setAttribute("orders_list", ordersList);
            if (ordersList.isEmpty()) {
                request.setAttribute("no_result", true);
            }
        } catch (SQLException e) {
            request.setAttribute("no_result", true);
            request.setAttribute("error", true);
            e.printStackTrace();
            LOG.error(e.getMessage());
        }
        fwd(request, response);

    }

    /**
     * Removes attribute order_id when is called from any
     * page connected with specified order
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (null != request.getSession().getAttribute("order_id")) {
            request.getSession().removeAttribute("order_id");
        }
        doPost(request, response);
    }

}
