package ua.hudyma.dao;

import org.apache.log4j.Logger;

import ua.hudyma.constants.CarClass;

/**
 * Car DAO
 * 
 * @author Yurko Hudyma
 * @version 1.0
 * @since 25.12.2021
 */

public class Car {
	
	public static final Logger LOG = Logger.getLogger(Car.class);

	private int car_id;
	private String model;
	private CarClass carclass;
	private int price;
	private int description;

	public int getCar_id() {
		return car_id;
	}

	public String getModel() {
		return model;
	}

	public CarClass getCarclass() {
		return carclass;
	}

	public int getPrice() {
		return price;
	}

	public int getDescription() {
		return description;
	}

	public void setCar_id(int car_id) {
		this.car_id = car_id;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public void setCarclass(CarClass carclass) {
		this.carclass = carclass;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public void setDescription(int description) {
		this.description = description;
	}

	public void setNumber(String string) {
	}

}
