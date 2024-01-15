package ua.hudyma.servlets.order_car.user_servlets;

import ua.hudyma.db.DBManager;
import ua.hudyma.dao.User;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serial;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Servlet gets order parameters and sets new order.
 *
 * @author IC
 */
@WebServlet(name = "OrderFormServlet")
public class OrderFormServlet extends HttpServlet {

	@Serial
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(OrderFormServlet.class);
	
	private static final String CAR_RENTAL = "/";
	private static final String ORDER_JSP = "/jsp/user/order.jsp";
	private static final String USER_PAGE = CAR_RENTAL+"user";
	private static final String PROPERTY = "local";
	/* MySQL retrieves and displays DATE values in 'YYYY-MM-DD' format. This var change has no effect*/ 
	public static final String FORMATTER_PATTERN = "yyyy-MM-dd";

	private static void fwd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher(ORDER_JSP).forward(req, resp);
	}

	/**
	 * Method gets order parameters and makes a new order with them if possible.
	 * Checks if dates are set correct
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String act = request.getParameter("actionName");
		if (act.equals("order")) {
			User user = (User) request.getSession().getAttribute("user");
			int user_id = user.getUser_id();
			String name = request.getParameter("name");
			String model = request.getParameter("model");
			/*String carclass = request.getParameter("carclass");*/
			String comment = request.getParameter("comment");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMATTER_PATTERN);
			String date_begin = request.getParameter("date_in");
			String date_end = request.getParameter("date_out");
			LocalDate date_in = LocalDate.parse(date_begin, formatter);
			LocalDate date_out = LocalDate.parse(date_end, formatter);
			/** out of order.jsp, at the select form, we obtain indexes of sorted car models which here are converted into car_id*/
			int car_id = Integer.parseInt(request.getParameter("model"));
			String passport_data = request.getParameter("passdata");
			boolean driver_needed = Boolean.parseBoolean(request.getParameter("driver"));
			if (date_in.isAfter(LocalDate.now()) && date_in.isBefore(date_out)) {
				try {
					/** this var means number of ResultSet rows returned */
					int affectedRows = DBManager.getInstance().setNewOrder(user_id, name, /*carclass,*/ car_id, date_in, date_out,
							comment, passport_data, driver_needed, model);
					/** if zero, generate error output */
					if (affectedRows == 0) {
						Locale locale = (Locale) request.getSession().getAttribute("locale");
						ResourceBundle resourceBundle = ResourceBundle.getBundle(PROPERTY, locale);
						String logErr = resourceBundle.getString("order.set_order.error.log");
						LOG.error(logErr + " " + affectedRows);
						request.setAttribute("error", true);
						fwd(request, response);
					/** otherwise, redirect to user.jsp, showing newly created order */
					} else {
						response.sendRedirect(USER_PAGE);
					}
				} catch (SQLException e) {
					request.setAttribute("error", true);
					LOG.error(e.getMessage());
					fwd(request, response);
				}
			/** if submitted dates are not suitable */
			} else {
				request.setAttribute("order_error", true);
				fwd(request, response);
			}
			
		} else {
			/** New Order button in user.jsp as keypressed (with req.attrib 'newOrder') goes to order.jsp */
			fwd(request, response);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		fwd(request, response);
	}
}
