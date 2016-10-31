package com.mycompany.bicycles.utilities;

import java.util.Date;

public class AuctionItem {

	int itemid;
	String brand;
	String model;
	 String descr;
	double buynow;
	double startprice;
	Date enddate;
	double high;

	int userid;
	int active;
	public int getItemid() {
		return itemid;
	}
	public void setItemid(int itemid) {
		this.itemid = itemid;
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
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public int getActive() {
		return active;
	}
	public void setActive(int active) {
		this.active = active;
	}
	public void setHighest(double double1) {
		this.high =double1;
		
	}	
	public double getHighest(){
		return high;
	}
	
	
	
	
}
