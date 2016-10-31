package com.mycompany.bicycles;

import java.util.Date;

import com.vaadin.ui.Image;

public class Bid {
	Image image;
	String brand, model, descr;
	double buynow , startprice;
	Date enddate;
	double  bid =0.0;

	
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public double getBuynow() {
		return buynow;
	}
	public void setBuynow(double buynow) {
		this.buynow = buynow;
	}
	public double getStartprice() {
		return startprice;
	}
	public void setStartprice(double startprice) {
		this.startprice = startprice;
	}
	public Date getEnddate() {
		return enddate;
	}
	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}
	public double getBid() {
		return bid;
	}
	public void setBid(double bid) {
		this.bid = bid;
	}
	
	
	
}
