package ua.hudyma.servlets.order_car;

import ua.hudyma.dao.Car;
import ua.hudyma.dao.CarDescription;
import ua.hudyma.db.DBManager;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;

/**
 * Servlet shows information about the car
 *
 * @author IC
 */
@WebServlet(name = "CarDetailsServlet")
public class CarDetailsServlet extends HttpServlet {
    
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger.getLogger(CarDetailsServlet.class);
	
	private static final String CAR_RENTAL = "/CarRental";
    private static final String CAR_DETAILS_JSP = "/jsp/car.jsp";
    private static final String CAR_DETAILS = CAR_RENTAL+"/car";
    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(CAR_DETAILS_JSP).forward(req, resp);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int car_id = Integer.parseInt(request.getPathInfo().substring(1));
        response.sendRedirect(CAR_DETAILS + "/" + String.valueOf(car_id));
    }

    /**
     * Gets car id from URI and sets Car object and
     * its description text as attribute.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int order_id = (int) request.getSession().getAttribute("order_id");
            int car_id = Integer.parseInt(request.getPathInfo().substring(1));
            //int car_id = Integer.parseInt(request.getParameter("car_id"));
            Car car = DBManager.getInstance().getCar(car_id);
            Locale locale = (Locale) request.getSession().getAttribute("locale");
            CarDescription description = DBManager.getInstance().getDescription(car.getDescription(), locale);
            request.setAttribute("car", car);
            request.setAttribute("description", description.getCar_info());
            request.setAttribute("order_id", order_id);
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("error", true);
            LOG.error(e.getMessage());
        }

        fwd(request, response);
    }
}
