package com.mycompany.bicycles;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import utilities.AuctionItem;
import utilities.UserData;

public class DatabaseHelper2 {


	
	/**
	 * Gets all items from the database
	 * @return
	 */
	static ArrayList<AuctionItem> listAllItems(){
		ArrayList<AuctionItem> items = new ArrayList<AuctionItem>();
		
		String query = "SELECT * FROM items";
		
		
		try(Connection c = getConnection();
			PreparedStatement ps = c.prepareStatement(query);){
			
			ResultSet rs = ps.executeQuery();
			
		while(rs.next()){
			
			AuctionItem ai = new AuctionItem();
			
			ai.setItemid(rs.getInt("itemid"));
			ai.setBrand(rs.getString("brand"));
			ai.setModel(rs.getString("model"));
			ai.setDesc(rs.getString("descr"));
			ai.setBuynow(rs.getDouble("buynow"));
			ai.setStartprice(rs.getDouble("startprice"));
			ai.setEnd(rs.getDate("enddate"));
			ai.setUserid(rs.getInt("userid"));
			ai.setActive(rs.getInt("active"));									
			items.add(ai);
		}
			
		
		
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		
		
		return items;
	}
	
	
	static UserData getUser(int id){
		 try (Connection con = getConnection();
			  PreparedStatement stmt = con.prepareStatement("SELECT * FROM users WHERE userid=?");){

	        stmt.setInt(1,id);
	        ResultSet rs=stmt.executeQuery();
	        while(rs.next())
	        {
	        	
	        	UserData ud = new UserData();
	        	
	        	ud.setFirstname(rs.getString("firstname"));
	            ud.setLastname(rs.getString("lastname"));
	            ud.setEmail(rs.getString("email"));
	            ud.setPhone(rs.getString("phone"));
//	            ud.setrs.getString("password"));
	            ud.setUsername(rs.getString("username"));
	   
		        return ud;
	            
	        }

	    }catch(Exception e){
	    	e.printStackTrace();
	    }
		return null;
	}
	
	static boolean validateLogin(String name,String password){
		
	    String query = "SELECT * FROM users WHERE username=? AND password = md5(?)";
	    try (Connection con = getConnection();
        PreparedStatement stmt = con.prepareStatement(query);){
	    	
	    	stmt.setString(1, name);
	    	stmt.setString(2, password);
	    	return stmt.executeQuery().next();
	    	
	    
	    	
	    }catch(SQLException e){
	    	e.printStackTrace();
	    }
	    
		return false;
	}
	
	
	static boolean checkUsernameAvailability(String username) {
	    boolean usernameAvailable = false;
	    
	    String query = "SELECT * FROM users WHERE username=?";
	    try (Connection con = getConnection();
        PreparedStatement stmt = con.prepareStatement(query);){
	        
	
	        stmt.setString(1,username);
	        ResultSet rs = stmt.executeQuery();
	        if(rs.next()){
	            usernameAvailable = false;
	        }else{
	        	usernameAvailable = true;
	        }
	    }catch(Exception e){ 
	    	
	    	e.printStackTrace();
	    }
	    return usernameAvailable;
	}
	
	

 	/**
 	 * adds user to database
 	 * 
 	 * @param firstname
 	 * @param lastname
 	 * @param username
 	 * @param email
 	 * @param phonenro
 	 * @param password
 	 */
    static void addUser(String firstname, String lastname, String username, String email, String phonenro, String password){
    	String query = "INSERT INTO users(firstname, lastname, email, phone, password, username) VALUES(?,?,?,?,md5(?),?)";
    	
    	
    	 try (Connection con = getConnection();
    		        PreparedStatement stmt = con.prepareStatement(query);){

	        //Määrittelee yhteyden databaseen ja yhdistää
	
	        //SQL queryn määrittely
	       
	        //Jokaisen queryn ? merkin kohdalle määritellään arvo aiemmin määritettyjen muuttujien mukaan
	        stmt.setString(1,firstname);
	        stmt.setString(2,lastname);
	        stmt.setString(3,email);
	        stmt.setString(4,phonenro);
	        stmt.setString(5,password);
	        stmt.setString(6,username);
	        //Suorittaa queryn
	        stmt.executeUpdate();
	        //Sulkee yhteyden databaseen
	        con.close(); 
	    }catch(Exception e){
	    	e.printStackTrace();
	    } //try ja catch pakollisia
	}
	
	
	private static Connection getConnection(){
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		String connection = "jdbc:mysql://localhost:3306/auctions?zeroDateTimeBehavior=convertToNull";
		Connection con;
		try {
			con = DriverManager.getConnection(connection, "root", "root");
		 
			return con;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	
		return null;
	}
	
}
