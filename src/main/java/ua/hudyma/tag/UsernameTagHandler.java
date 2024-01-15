package ua.hudyma.tag;


import org.apache.log4j.Logger;

import ua.hudyma.constants.AccessLevel;
import ua.hudyma.dao.User;
import ua.hudyma.db.DBManager;

import javax.security.auth.login.LoginException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Tag gets user's or admin's id and writes
 * "Administrator" + name if user is admin and
 * login otherwise.
 * @author ihor chekhunov
 */
public class UsernameTagHandler extends TagSupport {
   
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger.getLogger(UsernameTagHandler.class);

    private int user_id;
    private Locale lang;

    @Override
    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();
        try {
        	User user = DBManager.getInstance().getUser(user_id);

            if (user.getAccesslevel().equals(AccessLevel.ADMIN)) {
                String userEmail = user.getEmail();
                ResourceBundle resourceBundle = ResourceBundle.getBundle("local", lang);
                String admin = resourceBundle.getString("admin.order_page.admin_name");
                out.print(admin+ " " + userEmail);
            } 
            else if (user.getAccesslevel().equals(AccessLevel.MANAGER)) {
                String userEmail = user.getEmail();
                ResourceBundle resourceBundle = ResourceBundle.getBundle("local", lang);
                String manager = resourceBundle.getString("admin.order_page.manager_name");
                out.print(manager+ " " + userEmail);
            }
            else {
            	String userEmail = user.getEmail();
                out.print(userEmail);
            }
        } catch (SQLException | LoginException | IOException  e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
        }

        return SKIP_BODY;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Locale getLang() {
        return lang;
    }

    public void setLang(Locale lang) {
        this.lang = lang;
    }

}
