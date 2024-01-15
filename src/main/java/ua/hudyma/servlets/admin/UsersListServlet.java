package ua.hudyma.servlets.admin;

import ua.hudyma.dao.User;
import ua.hudyma.db.DBManager;

import org.apache.log4j.Logger;

import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/**
 * Gets list of all users (by page) and can set a user as admin.
 *
 * @author IC
 */
@WebServlet(name = "UsersListServlet")
public class UsersListServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(UsersListServlet.class);
	private static final String USER_LIST_JSP = "/jsp/admin/users_list.jsp";
	private static final String USER_LIST_PAGE = "users_list";
	private static final int PER_PAGE = 10;
	private static final String PROPERTY = "local";

	private static void fwd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher(USER_LIST_JSP).forward(req, resp);
	}

	/**
	 * Gets users list and if administrator sets some user as new administrator,
	 * changes user's access level.
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {

			//User currentUser = (User) request.getSession().getAttribute("user");
			// DBManager.getInstance().getAllPriceOrder(currentUser.getUserId());
			/** ints TOTAL OF AN USER ORDERS CURRENTLY NOT NEEDED*/
			//int counter = DBManager.getInstance().getAllUserOrdersTotal(currentUser.getUser_id());
			int page = 1;
			if (null != request.getParameter("page")) {
				page = Integer.parseInt(request.getParameter("page"));
			}
			
			int offset = (page - 1) * PER_PAGE;
			List<User> usersList = DBManager.getInstance().getAllUsers(PER_PAGE, offset);
			
			int numberOfOrders = DBManager.getInstance().getUsersNumber();
			int noOfPages = (int) Math.ceil(numberOfOrders * 1.0 / PER_PAGE);
			request.setAttribute("noOfPages", noOfPages);
			request.setAttribute("currentPage", page);
			request.setAttribute("users_list", usersList);

			List<Integer> list = new LinkedList<>();
			/** Summing UP all user's orders balance */
			int balance = 0;
			for (User user : usersList) {
				balance = DBManager.getInstance().getUserAllOrdersSum(user.getUser_id());
				list.add(balance);
			}
			request.setAttribute("balance", list);
			//request.getSession().setAttribute("counter", counter);

		} catch (SQLException | LoginException e) {
			request.setAttribute("error", true);
			e.printStackTrace();                                                   
			LOG.error(e.getMessage());             
		}
		int user_id=0;
		if (null != request.getParameter("user_id")) {
			user_id = Integer.parseInt(request.getParameter("user_id"));
		}
		
		if (null != request.getParameter("actionName")) {
			String act = request.getParameter("actionName");
			if (act.equals("set_admin")) {
				//user_id = Integer.parseInt(request.getParameter("user_id"));
				try {
					int res = DBManager.getInstance().setUserAdmin(user_id);
					if (res == 0) {
						Locale locale = (Locale) request.getSession().getAttribute("locale");
						ResourceBundle resourceBundle = ResourceBundle.getBundle(PROPERTY, locale);
						String logErr = resourceBundle.getString("users.set_admin.log");
						LOG.error(logErr + " " + String.valueOf(res));
						request.setAttribute("error", true);
						fwd(request, response);
					} else {
						response.sendRedirect(USER_LIST_PAGE);
					}
				} catch (SQLException e) {
					request.setAttribute("error", true);
					e.printStackTrace();
					LOG.error(e.getMessage());
				}

			} else if (act.equals("set_manager")) {
				//user_id = Integer.parseInt(request.getParameter("user_id"));
				try {
					int res = DBManager.getInstance().setUserManager(user_id);
					if (res == 0) {
						Locale locale = (Locale) request.getSession().getAttribute("locale");
						ResourceBundle resourceBundle = ResourceBundle.getBundle(PROPERTY, locale);
						String logErr = resourceBundle.getString("users.set_manager.log");
						LOG.error(logErr + " " + String.valueOf(res));
						request.setAttribute("error", true);
						fwd(request, response);
					} else {
						response.sendRedirect(USER_LIST_PAGE);
					}
				} catch (SQLException e) {
					request.setAttribute("error", true);
					e.printStackTrace();
					LOG.error(e.getMessage());
				}
			}
			
			else if (act.equals("block_user")) {
				//user_id = Integer.parseInt(request.getParameter("user_id"));
				try {
					int res = DBManager.getInstance().blockUser(user_id);
					if (res == 0) {
						Locale locale = (Locale) request.getSession().getAttribute("locale");
						ResourceBundle resourceBundle = ResourceBundle.getBundle(PROPERTY, locale);
						String logErr = resourceBundle.getString("users.block_user.log");
						LOG.error(logErr + " " + String.valueOf(res));
						request.setAttribute("error", true);
						fwd(request, response);
					} else {
						response.sendRedirect(USER_LIST_PAGE);
					}
				} catch (SQLException e) {
					request.setAttribute("error", true);
					e.printStackTrace();
					LOG.error(e.getMessage());
				}
			}
			
			else if (act.equals("unblock_user")) {
				//user_id = Integer.parseInt(request.getParameter("user_id"));
				try {
					int res = DBManager.getInstance().unBlockUser(user_id);
					if (res == 0) {
						Locale locale = (Locale) request.getSession().getAttribute("locale");
						ResourceBundle resourceBundle = ResourceBundle.getBundle(PROPERTY, locale);
						String logErr = resourceBundle.getString("users.unblock_user.log");
						LOG.error(logErr + " " + String.valueOf(res));
						request.setAttribute("error", true);
						fwd(request, response);
					} else {
						response.sendRedirect(USER_LIST_PAGE);
					}
				} catch (SQLException e) {
					request.setAttribute("error", true);
					e.printStackTrace();
					LOG.error(e.getMessage());
				}
			}
			

			else {
				fwd(request, response);
			}

		} else {
			fwd(request, response);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

}
