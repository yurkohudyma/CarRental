package ua.hudyma.dao;

import java.time.LocalDate;
import org.apache.log4j.Logger;
import ua.hudyma.constants.CarClass;
import ua.hudyma.constants.OrderStatus;

public class Order {
	
	public static final Logger LOG = Logger.getLogger(Order.class);
	
	private int order_id;
	private int user_id; //FK from User
	private int car_id; //FK from Car
	private int model; //FK from Car
	private CarClass carclass;
	private LocalDate date_begin;
	private LocalDate date_end;
	private OrderStatus orderStatus;
	private String passport_data; //FK from User
	private boolean driverNeeded;
	private int price;
	private int orderedCarId;
	private String additionalInfo;
	private String name;
	
	
	public String getAdditionalInfo() {
		return additionalInfo;
	}
	public int getCar_id() {
		return car_id;
	}
	public CarClass getCarclass() {
		return carclass;
	}
	public LocalDate getDate_begin() {
		return date_begin;
	}
	public LocalDate getDate_end() {
		return date_end;
	}
	public int getModel() {
		return model;
	}
	public int getOrder_id() {
		return order_id;
	}
	public int getOrderedCarId() {		
		return orderedCarId;
	}
	public OrderStatus getOrderStatus() {
		return orderStatus;
	}
	public String getPassport_data() {
		return passport_data;
	}
	public int getPrice() {
		return price;
	}
	public int getUser_id() {
		return user_id;
	}
	public String getName() {
		return name;
	}
	public boolean isDriverNeeded() {
		return driverNeeded;
	}
	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
	public void setCar_id(int car_id) {
		this.car_id = car_id;
	}
	public void setCarclass(CarClass carclass) {
		this.carclass = carclass;
	}
	public void setDate_begin(LocalDate date_begin) {
		this.date_begin = date_begin;
	}
	public void setDate_end(LocalDate date_end) {
		this.date_end = date_end;
	}
	public void setDriverNeeded(boolean driverNeeded) {
		this.driverNeeded = driverNeeded;
	}
	public void setModel(int model) {
		this.model = model;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}
	public int setOrderedCarId() {
		return orderedCarId;
	}
	public void setOrderedCarId(int orderedCarId) {
		this.orderedCarId = orderedCarId;
	}
	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	public void setPassport_data(String passport_data) {
		this.passport_data = passport_data;
	}
		
	public void setPrice(int price) {
		this.price = price;
	}
	public void setStatus(OrderStatus status) {
		this.orderStatus = status;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
}
