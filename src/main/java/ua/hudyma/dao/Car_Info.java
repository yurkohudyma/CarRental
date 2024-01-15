package ua.hudyma.dao;

import java.util.Locale;

/**
 * Car info DAO
 * 
 * @author Yurko Hudyma
 * @version 1.0
 * @since 25.12.2021
 */

public class Car_Info {
	
	private int info_id;
	private Locale locale;
	private String car_info;
	private int info_number;
	
	public int getInfo_id() {
		return info_id;
	}
	public void setInfo_id(int info_id) {
		this.info_id = info_id;
	}
	public Locale getLocale() {
		return locale;
	}
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	public String getCar_info() {
		return car_info;
	}
	public void setCar_info(String car_info) {
		this.car_info = car_info;
	}
	public int getInfo_number() {
		return info_number;
	}
	public void setInfo_number(int info_number) {
		this.info_number = info_number;
	}
	

}
