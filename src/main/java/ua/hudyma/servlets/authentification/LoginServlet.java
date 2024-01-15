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
 * Logs user in if login and password are correct
 * @author IC
 */

// added by YH
@WebServlet(name = "LoginServlet")
public class LoginServlet extends HttpServlet {
    
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(LoginServlet.class);
	private static final String PROPERTY = "local";
	private static final String CAR_RENTAL = "/";

    private static final String LOGIN_JSP = "/jsp/authentification/login.jsp";
    private static final String USER_JSP = CAR_RENTAL+"user";
    private static final String ADMIN_JSP = CAR_RENTAL+"admin";

    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(LOGIN_JSP).forward(req, resp);
    }

    /**
     * Gets User object and sets it as session
     * attribute. Redirects to user or admin home
     * page if authentification was successful.
     * Removes session's user attribute if user logs out
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    	/** email & pass from login form */
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String act = req.getParameter("actionName");
        /** check whether LOGIN form was submitted */
        switch (act) {
            case "authentification" -> {
                try {
                    User user = new User(email);
                    user = user.logIn(email, password);
                    //

                    req.getSession().setAttribute("user", user);
                    //

                    if (user.getAccesslevel().equals(AccessLevel.ADMIN)) {
                        req.getSession().setAttribute("isAdmin", true);
                    } else if (user.getAccesslevel().equals(AccessLevel.MANAGER)) {
                        req.getSession().setAttribute("isManager", true);
                    }
                    if (user.getAccesslevel().equals(AccessLevel.ADMIN) || user.getAccesslevel().equals(AccessLevel.MANAGER)) {
                        resp.sendRedirect(ADMIN_JSP);
                    } else if (user.getAccesslevel().equals(AccessLevel.USER)) {
                        resp.sendRedirect(USER_JSP);

                        /** this code concerns BLOCKED users*/
                    } else {
                        Locale locale = (Locale) req.getSession().getAttribute("locale");
                        ResourceBundle resourceBundle = ResourceBundle.getBundle(PROPERTY, locale);
                        String error = resourceBundle.getString("login.login_user_blocked");
                        req.setAttribute("auth_error", error);
                        LOG.info(error);
                        fwd(req, resp);
                    }

                } catch (LoginException e) {
                    Locale locale = (Locale) req.getSession().getAttribute("locale");
                    ResourceBundle resourceBundle = ResourceBundle.getBundle(PROPERTY, locale);
                    String error = resourceBundle.getString("login.login_error");
                    req.setAttribute("auth_error", error);
                    LOG.info(error);
                    fwd(req, resp);
                }
            }
            case "logout" -> {
                req.getSession().invalidate();
                fwd(req, resp);
            }
            default -> fwd(req, resp);
        }

    }

    /**
     * Redirects from authentification page to user's or
     * admin's page if user is logged in
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user != null) {
            if (user.getAccesslevel().equals(AccessLevel.ADMIN)||user.getAccesslevel().equals(AccessLevel.MANAGER)) {
                resp.sendRedirect(ADMIN_JSP);
            } else {
                resp.sendRedirect(USER_JSP);
            }
        } else {
            fwd(req, resp);
        }
    }
}