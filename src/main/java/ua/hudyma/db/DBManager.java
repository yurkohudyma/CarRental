package ua.hudyma.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;

import ua.hudyma.constants.AccessLevel;
import ua.hudyma.constants.CarClass;
import ua.hudyma.constants.OrderStatus;
import ua.hudyma.dao.Car;
import ua.hudyma.dao.Comment;
import ua.hudyma.dao.CarDescription;
import ua.hudyma.dao.Order;
import ua.hudyma.dao.User;

public class DBManager implements DBManagerInterface {

	// GET ALL FK
	// select * from INFORMATION_SCHEMA.TABLE_CONSTRAINTS where CONSTRAINT_TYPE =
	// 'FOREIGN KEY' having TABLE_Schema = 'carrental';

	private static final Logger LOG = Logger.getLogger(DBManager.class);
	private static DBManager INSTANCE;
	
	private static final String BLOCK_USER = "UPDATE users SET access_level='BLOCKED' WHERE user_id=?";
	private static final String GET_ALL_USER_ORDERS_NUMBER = "SELECT COUNT(*) AS orders_number FROM orders WHERE user_id = ?";
	private static final String GET_USER_ALL_ORDERS_SUM = "SELECT SUM(order_price) AS user_all_orders_sum FROM orders WHERE user_id = ?";
	private static final String GET_ALL_ORDERS = "SELECT order_id, user_id, car_id, carclass, date_in, date_out, "
			+ "order_additional_info, status, order_price, passport_data, driver_needed, name "
			+ "FROM orders ORDER BY order_id DESC LIMIT ? OFFSET ?";
	private static final String GET_ALL_USERS = "SELECT user_id, email, access_level FROM users "
			+ "ORDER BY access_level LIMIT ? OFFSET ?";
	private static final String GET_CAR_BY_ID = "SELECT c.car_id, c.model, c.carclass, c.price, c.description "
			+ "FROM cars AS c WHERE c.car_id=?";
	private static final String GET_DESCRIPTION_BY_NUMBER = "SELECT car_info, info_number, locale FROM car_info WHERE locale=? AND info_number=?";
	private static final String GET_NUMBER_OF_USERS = "SELECT COUNT(*) as users_number FROM users";
    private static final String GET_COMMENTS_NUMBER = "SELECT COUNT(*) AS comments_number FROM comments WHERE order_id=?";

	private static final String GET_ORDER = "SELECT order_id, user_id, car_id, carclass, date_in, date_out, order_additional_info,"
			+ "order_price, status, name, passport_data, driver_needed FROM orders WHERE order_id=?;";
	
    private static final String GET_ORDERS_COMMENTS = "SELECT comment_id, order_id, comment, user_id FROM comments " +
                    "WHERE order_id=? ORDER BY comment_id DESC LIMIT ? OFFSET ?";

	private static final String GET_ORDERS_BY_STATUS = "SELECT order_id, user_id, car_id, carclass, date_in, date_out, "
			+ "order_additional_info, status, order_price, passport_data, driver_needed, name FROM orders "
			+ "WHERE (status=?) ORDER BY order_id DESC " + "LIMIT ? OFFSET ?";
	private static final String GET_USER_BY_ID = "SELECT user_id, email, access_level FROM users WHERE user_id=?";
	private static final String GET_USERS_ORDERS = "SELECT o.order_id, o.user_id, o.car_id, o.carclass, o.date_in, o.date_out, "
			+ "o.order_additional_info, o.status, o.order_price, o.passport_data, o.driver_needed, o.name FROM "
			+ "orders AS o WHERE (o.user_id=?) ORDER BY o.date_in DESC LIMIT ? OFFSET ?";

	private static final String LOGIN_ATTEMPT = "SELECT user_id, access_level FROM users WHERE email=? AND passhash=?";

	private static final String NUMBER_ALL_ORDERS = "SELECT COUNT(*) AS orders_number FROM orders ORDER BY order_id DESC";
	private static final String NUMBER_OF_ORDERS_BY_STATUS = "SELECT COUNT(*) AS orders_number FROM  orders WHERE (status=?) "
			+ "ORDER BY order_id DESC";

	private static final String REGISTER_ATTEMPT = "INSERT INTO users (email, passhash, access_level) VALUES (?, ?, 'USER')";

	private static final String SET_ADD_INFO = "UPDATE orders SET order_additional_info=? WHERE order_id=?";
	private static final String SET_NEW_ORDER = "INSERT INTO orders (user_id, car_id, date_in, date_out, status, passport_data, driver_needed, name, model) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String SET_NULL_PRICE = "UPDATE orders SET order_price=NULL WHERE order_id=?";

	private static final String SET_ORDER_COMMENT = "INSERT INTO comments (order_id, comment, user_id) VALUES (?, ?, ?)";
	private static final String SET_ORDER_CAR = "UPDATE orders SET car_id=? WHERE order_id=?";
	private static final String SET_ORDER_PRICE = "UPDATE orders AS o INNER JOIN cars AS c ON (c.car_id=o.car_id) "
			+ "SET o.order_price=c.price*(o.date_out-o.date_in) WHERE o.order_id=?";
    private static final String SET_ORDER_STATUS = "UPDATE orders SET status=? WHERE order_id=?";
	private static final String SET_USER_AS_ADMIN = "UPDATE users SET access_level='ADMIN' WHERE user_id=?";
	private static final String SET_USER_AS_MANAGER = "UPDATE users SET access_level='MANAGER' WHERE user_id=?";
	private static final String USERS_NUMBER_OF_ORDERS = "SELECT COUNT(*) AS orders_number "
			+ "FROM  orders AS o WHERE (o.user_id=?)";
	private static final String UNBLOCK_USER = "UPDATE users SET access_level='USER' WHERE user_id=?";
	private static final String USE = "USE carrental;";



	private static Connection connection;

	private static Connection getConnection() {
		return Pool.getInstance().getConnection();
	}

	public static DBManager getInstance() {
		if (INSTANCE == null)
			synchronized (Pool.class) {
				if (INSTANCE == null) {
					INSTANCE = new DBManager();
				}
			}
		return INSTANCE;
	}

	{
		init();
	}

	public int blockUser(int user_id) throws SQLException {
		int res;
		try (PreparedStatement pstmt = connection.prepareStatement(BLOCK_USER)) {
			pstmt.setInt(1, user_id);
			res = pstmt.executeUpdate();
		}
		return res;
	}

	@Override
	public int editOrder(int order_id, int user_id, CarClass carclass, LocalDate dateIn, LocalDate dateOut,
			String comment) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int editOrder(int order_id, int user_id, int classOfComfort, LocalDate dateIn, LocalDate dateOut,
			String comment) throws SQLException {
		return 0;
	}

	@Override
	public List<Order> getAllOrders(int limit, int offset) throws SQLException {
		List<Order> ordersList;
		try (PreparedStatement pstmt = connection.prepareStatement(GET_ALL_ORDERS)) {
			pstmt.setInt(1, limit);
			pstmt.setInt(2, offset);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				ordersList = resultSetToOrdersList(resultSet);
			}
		}
		return ordersList;
	}

	@Override

	public int getAllUserOrdersNumber(int user_id) {
		int number = 0;
		try (PreparedStatement pstmt = connection.prepareStatement(GET_ALL_USER_ORDERS_NUMBER, ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_READ_ONLY)) {
			pstmt.setInt(1, user_id);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.first()) {
					number = resultSet.getInt("orders_number");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return number;
	}

	@Override
	public List<User> getAllUsers(int limit, int offset) throws SQLException, LoginException {
		List<User> usersList;
		try (PreparedStatement pstmt = connection.prepareStatement(GET_ALL_USERS)) {
			pstmt.setInt(1, limit);
			pstmt.setInt(2, offset);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				usersList = resultSetToUsersList(resultSet);
			}
		}
		return usersList;
	}

	@Override
	public Car getCar(int car_id) throws SQLException {
		Car car;
		try (PreparedStatement pstmt = connection.prepareStatement(GET_CAR_BY_ID, ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_READ_ONLY)) {
			pstmt.setInt(1, car_id);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				car = resultSetToCar(resultSet);
			}
		}
		return car;
	}

	public int getCarNumbers(int orderId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CarDescription getDescription(int info_number, Locale locale) throws SQLException {
	    CarDescription description = new CarDescription();
        try (PreparedStatement pstmt = connection.prepareStatement(GET_DESCRIPTION_BY_NUMBER,
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            pstmt.setString(1, locale.getLanguage());
            pstmt.setInt(2, info_number);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.first()) {
                    description.setInfo_number(resultSet.getInt("info_number"));
                    description.setLocale(locale);
                    description.setCar_info(resultSet.getString("car_info"));
                }
            }
        }
        return description;
    }

	@Override
	public int getNumberOfAllOrders() throws SQLException {
		int res = 0;

		try (PreparedStatement pstmt = connection.prepareStatement(NUMBER_ALL_ORDERS, ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_READ_ONLY)) {
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.first()) {
					res = resultSet.getInt("orders_number");
				}
			}
		}
		return res;
	}

	@Override
	public int getNumberOfComments(int order_id) throws SQLException {
	       int res = 0;
	        try (PreparedStatement pstmt = connection.prepareStatement(GET_COMMENTS_NUMBER,
	                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
	            pstmt.setInt(1, order_id);
	            try (ResultSet resultSet = pstmt.executeQuery()) {
	                if (resultSet.first()) {
	                    res = resultSet.getInt("comments_number");
	                }
	            }
	        }
	        return res;
	    }

	@Override
	public int getNumberOfOrdersByStatus(OrderStatus status) throws SQLException {
		int res = 0;

		try (PreparedStatement pstmt = connection.prepareStatement(NUMBER_OF_ORDERS_BY_STATUS,
				ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
			pstmt.setString(1, status.toString());
			try (ResultSet resultSet = pstmt.executeQuery()) {

				if (resultSet.first()) {
					res = resultSet.getInt("orders_number");
				}
			}
		}
		return res;
	}

	@Override
	public Order getOrder(int order_id) throws SQLException {
		Order order;
		try (PreparedStatement pstmt = connection.prepareStatement(GET_ORDER, ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_READ_ONLY)) {
			pstmt.setInt(1, order_id);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				order = resultSetToOrder(resultSet);
			}
		}
		return order;
	}

	@Override
	public List<Order> getOrdersByStatus(int limit, int offset, OrderStatus status) throws SQLException {
		List<Order> ordersList;
		try (PreparedStatement pstmt = connection.prepareStatement(GET_ORDERS_BY_STATUS)) {
			pstmt.setString(1, status.toString());
			pstmt.setInt(2, limit);
			pstmt.setInt(3, offset);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				ordersList = resultSetToOrdersList(resultSet);
			}
		}
		return ordersList;
	}

	@Override
	public List<Comment> getOrdersComments(int order_id, int limit, int offset) throws SQLException {
	       List<Comment> commentList = new ArrayList<>();
	        try (PreparedStatement pstmt = connection.prepareStatement(GET_ORDERS_COMMENTS)) {
	            pstmt.setInt(1, order_id);
	            pstmt.setInt(2, limit);
	            pstmt.setInt(3, offset);
	            try (ResultSet resultSet = pstmt.executeQuery()) {
	                while (resultSet.next()) {
	                    if (!(resultSet.getString("comment")).equals("")) {
	                        Comment comment = new Comment();
	                        comment.setComment_id(resultSet.getInt("comment_id"));
	                        comment.setOrder_id(resultSet.getInt("order_id"));
	                        comment.setText(resultSet.getString("comment"));
	                        comment.setUser_id(resultSet.getInt("user_id"));
	                        commentList.add(comment);
	                    }
	                }
	            }
	        }
	        return commentList;
	    }

	@Override
	public List<Car> getSuitableCar(int order_id, int limit, int offset) throws SQLException {
		throw new IllegalStateException("method is not IMPLEMENTED");
	}

	public User getUser(int user_id) throws SQLException, LoginException {
		User user = null;
		try (PreparedStatement pstmt = connection.prepareStatement(GET_USER_BY_ID, ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_READ_ONLY)) {
			pstmt.setInt(1, user_id);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.first()) {
					String email = resultSet.getString("email");
					user = new User(email);
					user.setUser_id(resultSet.getInt("user_id"));
					user.setAccesslevel(AccessLevel.valueOf(resultSet.getString("access_level").toUpperCase()));
				}
			}
		}
		return user;
	}

	@Override
	public int getUserAllOrdersSum(int user_id) {
		int sum = 0;
		try (PreparedStatement pstmt = connection.prepareStatement(GET_USER_ALL_ORDERS_SUM, ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_READ_ONLY)) {
			pstmt.setInt(1, user_id);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.first()) {
					sum = resultSet.getInt("user_all_orders_sum");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sum;
	}

	@Override
	public int getUsersNumber() throws SQLException {
		int res = 0;
		try (PreparedStatement pstmt = connection.prepareStatement(GET_NUMBER_OF_USERS, ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_READ_ONLY)) {
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.first()) {
					res = resultSet.getInt("users_number");
				}
			}
		}
		return res;
	}

	@Override
	public int getUsersNumberOfOrders(int user_id) throws SQLException {
		int res = 0;
		try (PreparedStatement pstmt = connection.prepareStatement(USERS_NUMBER_OF_ORDERS,
				ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
			pstmt.setInt(1, user_id);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.first()) {
					res = resultSet.getInt("orders_number");
				}
			}
		}
		return res;
	}

	@Override
	public List<Order> getUsersOrders(int user_id, int offset, int limit) throws SQLException {
		List<Order> res;
		try (PreparedStatement pstmt = connection.prepareStatement(GET_USERS_ORDERS)) {
			pstmt.setInt(1, user_id);
			pstmt.setInt(2, limit);
			pstmt.setInt(3, offset);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				res = resultSetToOrdersList(resultSet);
			}
		}
		return res;
	}

	private void init() {
		connection = getConnection();
		try {
			connection.prepareStatement(USE).execute();
		} catch (SQLException e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public User logIn(String email, String password) throws LoginException {
		User user = new User(email);
		try (PreparedStatement pstmt = connection.prepareStatement(LOGIN_ATTEMPT, ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_READ_ONLY)) {

			pstmt.setString(1, email);
			pstmt.setString(2, password);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				boolean completed = resultSet.first();
				if (completed) {
					user.setUser_id(resultSet.getInt("user_id"));
					user.setAccesslevel(AccessLevel.valueOf(resultSet.getString("access_level").toUpperCase()));
				} else {
					throw new LoginException();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
			throw new LoginException();
		}
		return user;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean register(String email, String password) {
		boolean res = false;
		try (PreparedStatement pstmt = connection.prepareStatement(REGISTER_ATTEMPT)) {
			pstmt.setString(1, email);
			pstmt.setString(2, password);
			pstmt.executeUpdate();
			res = true;
		} catch (SQLException e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public int removeOrder(int order_id) throws SQLException {
		return 0;
	}

	/**
	 * Returns Car object from ResultSet (presuming there is only row in it)
	 *
	 * @param resultSet
	 * @return Car
	 * @throws SQLException
	 * @see Car
	 */
	private Car resultSetToCar(ResultSet resultSet) throws SQLException {
		Car car = new Car();
		if (resultSet.first()) {
			car.setCar_id(resultSet.getInt("car_id"));
			car.setModel(resultSet.getString("model"));
			car.setCarclass(CarClass.valueOf(resultSet.getString("carclass")));
			car.setPrice(resultSet.getInt("price"));
			car.setDescription(resultSet.getInt("description"));
		}
		return car;
	}

	/**
	 * Returns Order object from ResultSet (consuming there is one row in it)
	 *
	 * @param resultSet
	 * @return Order object
	 * @throws SQLException
	 * @see Order
	 */
	private Order resultSetToOrder(ResultSet resultSet) throws SQLException {
		Order order = new Order();

		if (resultSet.first()) {
			order.setOrder_id(resultSet.getInt("order_id"));
			order.setUser_id(resultSet.getInt("user_id"));
			order.setCarclass(CarClass.valueOf(resultSet.getString("carclass")));
			order.setDate_begin(resultSet.getDate("date_in").toLocalDate());
			order.setDate_end(resultSet.getDate("date_out").toLocalDate());
			// order.setOrderedCarId(resultSet.getInt("order_car_id"));
			order.setCar_id(resultSet.getInt("car_id"));
			order.setAdditionalInfo(resultSet.getString("order_additional_info"));
			order.setStatus(OrderStatus.valueOf(resultSet.getString("status")));
			order.setPrice(resultSet.getInt("order_price"));
			order.setName(resultSet.getString("name"));
			//order.setModel(resultSet.getInt("model"));
			order.setPassport_data(resultSet.getString("passport_data"));

		}
		return order;
	}

	/**
	 * Gets list of orders from ResultSet
	 *
	 * @param resultSet
	 * @return ArrayList of Order objects
	 * @throws SQLException
	 * @see Order
	 */
	private List<Order> resultSetToOrdersList(ResultSet resultSet) throws SQLException {
		List<Order> res = new ArrayList<>();
		while (resultSet.next()) {
			Order order = new Order();
			order.setOrder_id(resultSet.getInt("order_id"));
			order.setUser_id(resultSet.getInt("user_id"));
			// bind this attrib with OrderedCarId below
			order.setCar_id(resultSet.getInt("car_id"));
			order.setCarclass(CarClass.valueOf(resultSet.getString("carclass")));
			order.setDate_begin(resultSet.getDate("date_in").toLocalDate());
			order.setDate_end(resultSet.getDate("date_out").toLocalDate());
			order.setStatus(OrderStatus.valueOf(resultSet.getString("status")));
			order.setPassport_data(resultSet.getString("passport_data"));
			order.setDriverNeeded(resultSet.getBoolean("driver_needed"));
			order.setName(resultSet.getString("name"));
			try {
				// order.setOrderedCarId(resultSet.getInt("car_id"));
				order.setAdditionalInfo(resultSet.getString("order_additional_info"));
				order.setPrice(resultSet.getInt("order_price"));
			} catch (NullPointerException e) {
				LOG.info("Cars, price and additional info have not been set yet");
			}

			res.add(order);
		}
		return res;
	}

	// INSERT INTO orders (user_id, car_id, date_in, date_out, status,
	// passport_data, driver_needed) VALUES (?, ?, ?, ?, ?, ?, ?)";

	// user_id, name, carclass, car_id, date_in, date_out, comment, passport_data,
	// driver_needed

	/**
	 * Gets list of users from ResultSet
	 *
	 * @param resultSet
	 * @return ArrayList of User objects
	 * @throws SQLException
	 * @see User
	 * @see AccessLevel
	 */
	private List<User> resultSetToUsersList(ResultSet resultSet) throws SQLException, LoginException {
		List<User> res = new ArrayList<>();
		while (resultSet.next()) {
			String email = resultSet.getString("email");
			User user = new User(email);
			user.setUser_id(resultSet.getInt("user_id"));
			// user.setName(resultSet.getString("name"));
			user.setAccesslevel(AccessLevel.valueOf(resultSet.getString("access_level").toUpperCase()));
			res.add(user);
		}
		return res;
	}

	@Override
	public int setAddInfo(int order_id, String addInfo) throws SQLException {
		int res;
		try (PreparedStatement pstmt = connection.prepareStatement(SET_ADD_INFO)) {
			pstmt.setString(1, addInfo);
			pstmt.setInt(2, order_id);
			res = pstmt.executeUpdate();
		}
		return res;
	}

	@Override
	public int setNewOrder(int user_id, String name, /*String carclass,*/ int car_id, LocalDate date_in, LocalDate date_out,
			String comment, String passport_data, boolean driver_needed, String model) throws SQLException {
		Date sqlDateBegin = Date.valueOf(date_in);
		Date sqlDateEnd = Date.valueOf(date_out);
		/** this var specifies future order_id */
		int newRow_id = 0;
		try (PreparedStatement pstmt = connection.prepareStatement(SET_NEW_ORDER, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, user_id);
			pstmt.setInt(2, car_id);
			pstmt.setDate(3, sqlDateBegin);
			pstmt.setDate(4, sqlDateEnd);
			pstmt.setString(5, OrderStatus.REQUESTED.toString());
			pstmt.setString(6, passport_data);
			pstmt.setBoolean(7, driver_needed);
			//todo deprecated due to pstmt limitation of params in favor of 'model'
			/*pstmt.setString(8, carclass);*/
			pstmt.setString(8, name);
			pstmt.setString(9, model);

			/** return statement with code 1 means SUCCESS retrieved 1 row */
			int affectedRows = pstmt.executeUpdate();
			if (affectedRows == 1) {
				try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						newRow_id = generatedKeys.getInt(1);
						/** if comment's been submitted, insert into the DB */
						if (null != comment) {
							setOrderComment(newRow_id, comment, user_id);
						}
					}
				}
				/** if statement returns 0 which means NOTHING retrieved */
			} else {
				throw new SQLException("Creating order failed, no ID obtained.");
			}
		}
		return newRow_id;
	}

	@Override
	public int setNullPrice(int order_id) throws SQLException {
		int res;
		try (PreparedStatement pstmt = connection.prepareStatement(SET_NULL_PRICE)) {
			pstmt.setInt(1, order_id);
			res = pstmt.executeUpdate();
		}
		return res;
	}

	@Override
	public int setOrderComment(int order_id, String comment, int user_id) throws SQLException {
		int res;
		try (PreparedStatement pstmt = connection.prepareStatement(SET_ORDER_COMMENT)) {
			pstmt.setInt(1, order_id);
			pstmt.setString(2, comment);
			pstmt.setInt(3, user_id);
			res = pstmt.executeUpdate();
		}
		return res;
	}

	@Override
	public int setOrderPrice(int order_id) throws SQLException {
		int res;
		try (PreparedStatement pstmt = connection.prepareStatement(SET_ORDER_PRICE)) {
			pstmt.setInt(1, order_id);
			res = pstmt.executeUpdate();
		}
		return res;
	}

	/** @return number of row count or 0 if none returned
	 * 
	 * Is not needed in CarRental 
	 * @deprecated
	 * 
	 * */
	@Override
	public int setOrdersCar(int order_id, int car_id) throws SQLException {
		int res;
		try (PreparedStatement pstmt = connection.prepareStatement(SET_ORDER_CAR)) {
			if (car_id != 0) {
				pstmt.setInt(1, car_id);
			} else {
				pstmt.setNull(1, Types.INTEGER);
			}
			pstmt.setInt(2, order_id);
			res = pstmt.executeUpdate();
		}
		return res;
	}

	@Override
	public int setOrderStatus(int order_id, OrderStatus status) throws SQLException {
		int res;
		try (PreparedStatement pstmt = connection.prepareStatement(SET_ORDER_STATUS)) {
			pstmt.setString(1, status.toString());
			pstmt.setInt(2, order_id);
			res = pstmt.executeUpdate();
		}
		return res;
	}

	@Override
	public int setUserAdmin(int user_id) throws SQLException {
		//System.out.println("USER-ID for SETUSERADM"+user_id);
		int res;
		try (PreparedStatement pstmt = connection.prepareStatement(SET_USER_AS_ADMIN)) {
			pstmt.setInt(1, user_id);
			res = pstmt.executeUpdate();
		}
		return res;
	}

	public int setUserManager(int user_id) throws SQLException {
		//System.out.println("USER-ID for SETUSERMAN"+user_id);
		int res;
		try (PreparedStatement pstmt = connection.prepareStatement(SET_USER_AS_MANAGER)) {
			pstmt.setInt(1, user_id);
			res = pstmt.executeUpdate();
		}
		return res;
	}

	public int unBlockUser(int user_id) throws SQLException {
		int res;
		try (PreparedStatement pstmt = connection.prepareStatement(UNBLOCK_USER)) {
			pstmt.setInt(1, user_id);
			res = pstmt.executeUpdate();
		}
		return res;
	}
}