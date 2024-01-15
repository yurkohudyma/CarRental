package ua.hudyma.servlets.authentification;

import org.apache.log4j.Logger;

import ua.hudyma.constants.AccessLevel;
import ua.hudyma.dao.User;

import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Servlet registers users if possible and redirects to user's page
 * @author IC
 */

// added by YH
@WebServlet(name = "RegistrationServlet")
public class RegistrationServlet extends HttpServlet {
   
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger.getLogger(RegistrationServlet.class);

	private static final String CAR_RENTAL = "/";
	private static final String REG_JSP = "/jsp/authentification/registration.jsp";
    private static final String LOGIN_JSP = CAR_RENTAL+"authentification";
    private static final String USER_JSP = CAR_RENTAL+"user";
    private static final String ADMIN_JSP = CAR_RENTAL+"admin";
	
    private static final String PROPERTY = "local";

    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(REG_JSP).forward(req, resp);
    }

    /**
     * Creates new user if input data is correct
     * Logs user in
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	/** Retrieves a locale parameter from a current session */
        Locale locale = (Locale) request.getSession().getAttribute("locale");
        /** Sets locale upon retrieved from above parameter */
        ResourceBundle resourceBundle = ResourceBundle.getBundle(PROPERTY, locale);
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        //String name = request.getParameter("name");
        /** an action parameter used for distinguish which form has been submitted */
        String act = request.getParameter("actionName");
        /** when registration form is done...*/
        if (act.equals("registration")) {
            try {
            	/** create a new user object based upon 2 compulsory fields */
                User user = new User(email, password);
                /** sets as session attrib 'user' a just created User object */
                request.getSession().setAttribute("user", user.logIn(email, password));
                /** redirect to login page */
                response.sendRedirect(LOGIN_JSP);

                /** if unsuccessful.. */
            } catch (LoginException e) {
            	/** prepare a variable for error msg */
                String errorMsg;
                /** error code 1 means duplicate unique fields */
                if (e.getMessage()=="1") {
                    errorMsg = resourceBundle.getString("registration.registration_error.already_exists");
                } else {
                	/** otherwise the email is not matching */
                    errorMsg = resourceBundle.getString("registration.registration_error.wrong_email");
                }
                /** sets an error attrib for JSP */
                request.setAttribute("reg_error", errorMsg);
                LOG.info(errorMsg);
                fwd(request, response);
            }

        } else {
            fwd(request, response);
        }
    }

    /**
     * Redirects from registration page to user's or admin's page if user is logged in
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            if (user.getAccesslevel().equals(AccessLevel.ADMIN) || user.getAccesslevel().equals(AccessLevel.MANAGER)) {
                response.sendRedirect(ADMIN_JSP);
            } else {
                response.sendRedirect(USER_JSP);
            }
        } else {
            fwd(request, response);
        }
    }
}