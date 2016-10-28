package utilities;

import java.util.Date;



public class AuctionItem{

	
	private int itemid;
	private String desc;
	private String brand;
	private String model;
	private int userid;
	private int photoid;
	private double buynow;
	private double startprice;
	private Date end;
	private boolean active;
	
	
	public AuctionItem(){
		
	}


	public int getItemid() {
		return itemid;
	}


	public String getDesc() {
		return desc;
	}


	public String getBrand() {
		return brand;
	}


	public String getModel() {
		return model;
	}


	public int getUserid() {
		return userid;
	}


	public int getPhotoid() {
		return photoid;
	}


	public double getBuynow() {
		return buynow;
	}


	public double getStartprice() {
		return startprice;
	}


	public Date getEnd() {
		return end;
	}


	public void setItemid(int itemid) {
		this.itemid = itemid;
	}


	public void setDesc(String desc) {
		this.desc = desc;
	}


	public void setBrand(String brand) {
		this.brand = brand;
	}


	public void setModel(String model) {
		this.model = model;
	}


	public void setUserid(int userid) {
		this.userid = userid;
	}


	public void setPhotoid(int photoid) {
		this.photoid = photoid;
	}


	public void setBuynow(double buynow) {
		this.buynow = buynow;
	}


	public void setStartprice(double startprice) {
		this.startprice = startprice;
	}


	public void setEnd(Date end) {
		this.end = end;
	}


	public void setActive(int int1) {
		// TODO Auto-generated method stub
		this.active = (int1==1)? true:false;
	}
	
	

	public boolean isActive(){
		return active;
	}

	
	
	
	

	
	
	
	
}
