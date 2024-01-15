package ua.hudyma.servlets.users;


import org.apache.log4j.Logger;

import ua.hudyma.dao.Order;
import ua.hudyma.dao.User;
import ua.hudyma.db.DBManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet displays list of all users orders.
 *
 * @author IC
 */
@WebServlet(name = "UserServlet")
public class UserServlet extends HttpServlet {
    
	private static final long serialVersionUID = 1L;
	/** CarRental URL is obligatory here, as Eclipse/Tomcat ain't run the webapp with no project's name URL-suffix, as IntelliJ does */
	private static final String CAR_RENTAL = "/";
	private static final Logger LOG = Logger.getLogger(UserServlet.class);
    private static final String USER_JSP = "/jsp/user/user.jsp";
    private static final String USER_PAGE = CAR_RENTAL+"user";
    private static final String ORDER_JSP = CAR_RENTAL+"order";
    private static final int PER_PAGE = 10;

    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(USER_JSP).forward(req, resp);
    }

    /**
     * Gets list of all orders of user by pages to display.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @apiNote Comments by YH
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	/** checks for keypressed from New Order button in user.jsp */
        if (null != request.getParameter("actionName")) {
            String act = request.getParameter("actionName");
            if (act.equals("newOrder")) {
            	/** goes to OrderFormServlet...*/
                response.sendRedirect(ORDER_JSP);
            } else {
                response.sendRedirect(USER_PAGE);
            }
        } else {
            try {
                User currentUser = (User) request.getSession().getAttribute("user");
                request.setAttribute("user_email", currentUser.getEmail());
                int page = 1;
                if (null != request.getParameter("page")) {
                    page = Integer.parseInt(request.getParameter("page"));
                }
                int offset = (page - 1) * PER_PAGE;
                /** retrieve cur user's orders*/
                List<Order> resList = DBManager.getInstance().getUsersOrders(currentUser.getUser_id(), offset, PER_PAGE);
                /** retrieve cur user's orders NUMBER for PAGINATION */
                int numberOfOrders = DBManager.getInstance().getUsersNumberOfOrders(currentUser.getUser_id());
                int noOfPages = (int) Math.ceil(numberOfOrders*1.0/PER_PAGE);
                List<Order> newList = new ArrayList<>();
                List<Order> oldList = new ArrayList<>();
                /** If ordered RETURN date is passed, add orded into OLD list, otherwise NEW*/
                for (Order r : resList) {
                    long p = ChronoUnit.DAYS.between(LocalDate.now(), r.getDate_end());
                    if (p>=0) {
                        newList.add(r);
                    } else {
                        oldList.add(r);
                    }
                }
                request.setAttribute("new_orders_list", newList);
                request.setAttribute("old_orders_list", oldList);
                request.setAttribute("noOfPages", noOfPages);
                request.setAttribute("currentPage", page);
                if (resList.isEmpty()) {
                    request.setAttribute("no_result", true);
                }
                fwd(request, response);
            } catch (SQLException e) {
                request.setAttribute("error", true);
                e.printStackTrace();
                LOG.error(e.getMessage());

            } catch (NullPointerException e) {
                e.printStackTrace();
                fwd(request, response);
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (null != request.getSession().getAttribute("order_id")) {
            request.getSession().removeAttribute("order_id");
        }
        doPost(request, response);
    }
}
