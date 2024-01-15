package ua.hudyma.db;

import javax.security.auth.login.LoginException;

import ua.hudyma.constants.AccessLevel;
import ua.hudyma.dao.Comment;
import ua.hudyma.dao.CarDescription;
import ua.hudyma.constants.CarClass;
import ua.hudyma.constants.OrderStatus;
import ua.hudyma.dao.Car;
import ua.hudyma.dao.Order;
import ua.hudyma.dao.User;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

/**
 * Interface for working with database.
 *
 * @author IC
 */
public interface DBManagerInterface {

	/**
	 * Edits order by its id. First comment in 'comments' is replaced by a new one.
	 *
	 * @param order_id
	 * @param user_id
	 * @param classOfComfort
	 * @param dateIn
	 * @param dateOut
	 * @param comment
	 * @return number of affectsd in 'orders' rows
	 * @throws SQLException
	 */
	int editOrder(int order_id, int user_id, CarClass carclass, LocalDate dateIn, LocalDate dateOut, String comment)
			throws SQLException;

	int editOrder(int order_id, int user_id, int classOfComfort, LocalDate dateIn, LocalDate dateOut, String comment)
			throws SQLException;
	
	/**
	 * Gets TOTAL of orders of an user. Comment by YH
	 *
	 * @param user_id
	 * @return integer value of users' quantity
	 * @throws SQLException
	 * @see User
	 */

	int getAllUserOrdersNumber(int user_id);
	//

	/**
	 * Gets NUMBER OF st of all orders. By parts for paging // Comment corrected by YH
	 *
	 * @param limit
	 * @param offset
	 * @return ArrayList of Order objects from offset to offset + limit
	 * @throws SQLException
	 * @see Order
	 */
	List<Order> getAllOrders(int limit, int offset) throws SQLException;

	/**
	 * Gets sum of prices for all orders. Comment and JavaDoc by YH
	 * 
	 * @param user_id
	 * @return ArrayList of User objects
	 * @throws SQLException
	 */
	
	int getUserAllOrdersSum(int user_id);

	/**
	 * Gets list of all users. By parts for paging
	 * 
	 * @param limit
	 * @param offset
	 * @return ArrayList of User objects
	 * @throws SQLException
	 */
	List<User> getAllUsers(int limit, int offset) throws SQLException, LoginException;

	/**
	 * Gets Car object by car id
	 *
	 * @param car_id
	 * @return
	 * @throws SQLException
	 * @see Car
	 */
	Car getCar(int car_id) throws SQLException;

	/**
	 * Gets number of suitable to orders parameters Cars
	 *
	 * @param order_id
	 * @return number of Cars that match requirements
	 * @throws SQLException
	 */

//	int getCarNumbers(int order_id) throws SQLException;

	/**
	 * Gets car's description by its number and locale
	 *
	 * @param textNumber - number of description in 'car_text' table
	 * @param locale     - "en" or "ru" Locale
	 * @return LocalCarDescription object
	 * @throws SQLException
	 * @see CarDescription
	 */
	CarDescription getDescription(int textNumber, Locale locale) throws SQLException;

	/**
	 * Gets number of all orders in 'orders' table
	 *
	 * @return number of all orders
	 * @throws SQLException
	 */
	int getNumberOfAllOrders() throws SQLException;

	/**
	 * Gets number of orders comments
	 *
	 * @param order_id
	 * @return total number of comments to one order
	 * @throws SQLException
	 */
	int getNumberOfComments(int order_id) throws SQLException;

	/**
	 * Gets number of orders with specified status:
	 *
	 * @param status
	 * @return total number of orders with specified status
	 * @throws SQLException
	 * @see OrderStatus
	 */
	int getNumberOfOrdersByStatus(OrderStatus status) throws SQLException;

	/**
	 * Gets Order object with set in its fields values from 'orders' by its id
	 *
	 * @param order_id
	 * @return Order object
	 * @throws SQLException
	 * @see Order
	 */
	Order getOrder(int order_id) throws SQLException;

	/**
	 * Gets list of orders by their status from offset to offset + limit.
	 *
	 * @param limit
	 * @param offset
	 * @param status
	 * @return ArrayList of Order objects
	 * @throws SQLException
	 * @see OrderStatus from 'orders'
	 */
	List<Order> getOrdersByStatus(int limit, int offset, OrderStatus status) throws SQLException;

	/**
	 * Gets list of all comments to the order by its id. By parts for paging
	 *
	 * @param order_id
	 * @param limit
	 * @param offset
	 * @return part (from offset to offset + limit) of the ArrayList of Comments
	 *         object with set fields
	 * @throws SQLException
	 * @see Comment
	 */
	List<Comment> getOrdersComments(int order_id, int limit, int offset) throws SQLException;

	/**
	 * Gets parted list of Cars that suit input parameters
	 *
	 * @param order_id
	 * @param limit
	 * @param offset
	 * @return ArrayList of Car objects that match requirements
	 * @throws SQLException
	 * @see Car
	 */
	List<Car> getSuitableCar(int order_id, int limit, int offset) throws SQLException;

	/**
	 * Gets values from users row in 'users' table and sets them to User object by
	 * his id
	 *
	 * @param user_id
	 * @return User object
	 * @throws SQLException
	 * @throws LoginException if there is no user with such id
	 * @see User
	 */
	User getUser(int user_id) throws SQLException, LoginException;

	/**
	 * Gets number of all users in 'users' table
	 * 
	 * @return total number of users
	 * @throws SQLException
	 */
	int getUsersNumber() throws SQLException;

	/**
	 * Gets total number of users orders by his id
	 *
	 * @param user_id
	 * @return number of users orders
	 * @throws SQLException
	 */
	int getUsersNumberOfOrders(int user_id) throws SQLException;

	/**
	 * Gets list of users orders by parts (for paging) by users id
	 *
	 * @param user_id
	 * @param offset  - depends on currently displayed page
	 * @param limit   - depends on number of orders we display on page
	 * @return ArrayList of Order objects
	 * @throws SQLException
	 */
	List<Order> getUsersOrders(int user_id, int offset, int limit) throws SQLException;

	/**
	 * Checks if input parameters email and password can be found in database
	 *
	 * @param password
	 * @return User object with set fields
	 * @throws LoginException when login and password are wrong
	 */
	User logIn(String email, String password) throws LoginException;

	/**
	 * Registers a user upon 3 fields --- IC comment is neglected. YH
	 * 
	 * @param name
	 * @param password
	 * @param email
	 * @return true if successful
	 */
	boolean register(String password, String email);

	/**
	 * Removes order by its id. Comments to this order are deleted too.
	 *
	 * @param order_id
	 * @return row count
	 * @throws SQLException
	 */
	int removeOrder(int order_id) throws SQLException;

	/**
	 * Sets additional info to order
	 *
	 * @param order_id
	 * @param addInfo
	 * @return row count
	 * @throws SQLException
	 */
	int setAddInfo(int order_id, String addInfo) throws SQLException;

	/**
	 * Sets new order in table 'orders' using input params
	 * @param comment        first comment to the order. Sets in 'comments' table
	 * @return row count
	 * @throws SQLException
	 */
	
	int setNewOrder(int user_id, String name, /*String carclass,*/ int car_id, LocalDate date_in, LocalDate date_out, String comment,
			String passport_data, boolean driver_needed, String model) throws SQLException;

	/**
	 * Sets orders price to 0
	 *
	 * @param order_id
	 * @return row count
	 * @throws SQLException
	 */
	int setNullPrice(int order_id) throws SQLException;

	/**
	 * Adds row to 'comments' table
	 *
	 * @param order_id
	 * @param comment  - comment text
	 * @param user_id  - id of the user who sent the comment
	 * @return row count
	 * @throws SQLException
	 */
	int setOrderComment(int order_id, String comment, int user_id) throws SQLException;

	/**
	 * Sets price to order in 'orders' table
	 *
	 * @param order_id
	 * @return row count
	 * @throws SQLException
	 */
	int setOrderPrice(int order_id) throws SQLException;

	/**
	 * Sets Car's id to order by order's id
	 *
	 * @param order_id
	 * @param car_id
	 * @return row count
	 * @throws SQLException
	 */
	int setOrdersCar(int order_id, int car_id) throws SQLException;

	/**
	 * Changes status of order in 'orders' table
	 *
	 * @param order_id
	 * @param status
	 * @return row count
	 * @throws SQLException
	 * @see OrderStatus
	 */
	int setOrderStatus(int order_id, OrderStatus status) throws SQLException;

	/**
	 * Sets ADMIN access level to user
	 * 
	 * @see AccessLevel
	 * @param user_id
	 * @return row count
	 * @throws SQLException
	 */
	int setUserAdmin(int user_id) throws SQLException;



}
