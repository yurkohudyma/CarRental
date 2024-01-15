package ua.hudyma.dao;

import java.util.Locale;

public class CarDescription {
    private int info_number;
    private Locale locale;
    private String car_info;
    
    
	public int getInfo_number() {
		return info_number;
	}
	public void setInfo_number(int info_number) {
		this.info_number = info_number;
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

    
}