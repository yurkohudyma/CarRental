package ua.hudyma.dao;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;

import ua.hudyma.constants.*;
import ua.hudyma.db.DBManager;

/**
 * User DAO
 * 
 * @author Yurko Hudyma
 * @version 1.0
 * @since 25.12.2021
 */

public class User {

	public static final Logger LOG = Logger.getLogger(User.class);

	private int user_id;
	private String email;
	private String passhash;
	private AccessLevel accesslevel;
	//private String name;
	private String passport_data;
	
	public User(String email) {
		this.email = email;
	}

	public User(String email, String password) throws LoginException {
		this.email=email;
		if (checkEmail(email)) {
			boolean completed = DBManager.getInstance().register(this.email, hash(password));
			if (!completed) {
				throw new LoginException(String.valueOf(1));
			}
		} else {
			throw new LoginException(String.valueOf(0));
		}
	}

	

	public int getUser_id() {
		return user_id;
	}

	public String getEmail() {
		return email;
	}

	public String getPasshash() {
		return passhash;
	}

	public AccessLevel getAccesslevel() {
		return accesslevel;
	}

//	public String getName() {
//		return name;
//	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPasshash(String passhash) {
		this.passhash = passhash;
	}

	public void setAccesslevel(AccessLevel accesslevel) {
		this.accesslevel = accesslevel;
	}

//	public void setName(String name) {
//		this.name = name;
//	}

	public String getPassport_data() {
		return passport_data;
	}

	public void setPassport_data(String passport_data) {
		this.passport_data = passport_data;
	}

	public User logIn(String email, String password) throws LoginException {
		return DBManager.getInstance().logIn(email, hash(password));
	}

	private String hash(String input) {
		String md5Hashed = null;
		if (null == input) {
			return null;
		}

		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(input.getBytes(), 0, input.length());
			md5Hashed = new BigInteger(1, digest.digest()).toString(16);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return md5Hashed;
	}

	private boolean checkEmail(String email) {
		String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

}