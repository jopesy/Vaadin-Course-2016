/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.bicycles;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mycompany.bicycles.utilities.AuctionItem;
//Importtaa koko javan sql libraryn
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Image;


public class DatabaseHelper {
    
    //Lisää käyttäjä
    static void addUser(String firstname, String lastname, String username, String email, String phonenro, String password){    
        try{
        Class.forName("com.mysql.jdbc.Driver");  
        Connection con=DriverManager.getConnection(  
        "jdbc:mysql://localhost:3306/auctions?zeroDateTimeBehavior=convertToNull", "root", "root");  
        //Määrittelee yhteyden databaseen ja yhdistää

        //SQL queryn määrittely
        PreparedStatement stmt = con.prepareStatement("INSERT INTO users(firstname, lastname, email, phone, password, username) VALUES(?,?,?,?,md5(?),?)");
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
    }catch(Exception e){ System.out.println(e);} //try ja catch pakollisia
}

    //Poista käyttäjä
static void deleteUser(){
    try{
        Class.forName("com.mysql.jdbc.Driver");  
        Connection con=DriverManager.getConnection(  
        "jdbc:mysql://localhost:3306/auctions?zeroDateTimeBehavior=convertToNull", "root", "root");
        //Yhteys
        
        int userid = 6; //Käytettävä muuttuja
        PreparedStatement stmt = con.prepareStatement("DELETE FROM users WHERE userid=?"); //Query
        stmt.setInt(1,userid); //WHERE lausekkeen arvon määritys
        stmt.executeUpdate(); //Queryn suoritus
        con.close(); //Yhteyden katkaisu
    }catch(Exception e){ System.out.println(e);}
}

static boolean checkUsernameAvailability(String username) {
    boolean usernameAvailable = true;
    try {
        Class.forName("com.mysql.jdbc.Driver");  
        Connection con=DriverManager.getConnection(  
        "jdbc:mysql://localhost:3306/auctions?zeroDateTimeBehavior=convertToNull", "root", "root");
        
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM users WHERE username=?");
        stmt.setString(1,username);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            usernameAvailable = false;
        }
        con.close(); //Yhteyden katkaisu
    }catch(Exception e){ System.out.println(e);}
    return usernameAvailable;
}

//Palauttaa userid:n, tai -1:n mikäli käyttäjää ei löydy tietokannasta
static int getUserId(String username, String password) {
    int userid = -1;
    
    try{
        Class.forName("com.mysql.jdbc.Driver");  
        Connection con=DriverManager.getConnection(  
        "jdbc:mysql://localhost:3306/auctions?zeroDateTimeBehavior=convertToNull", "root", "root");
        
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM users WHERE username=? AND password=md5(?)"); //Query
        stmt.setString(1,username);
        stmt.setString(2,password);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            userid = rs.getInt("userid");
        }
        con.close(); //Yhteyden katkaisu
    }catch(Exception e){ System.out.println(e);}
    return userid;
}

//Lisää esine ja sen kuva
static void addItem(String brand, String model, String descr, int userid, int buynow, int startprice, String enddate){
    try{
        Class.forName("com.mysql.jdbc.Driver");  
        Connection con=DriverManager.getConnection(  
        "jdbc:mysql://localhost:3306/auctions?zeroDateTimeBehavior=convertToNull", "root", "root");

        
        PreparedStatement stmt = con.prepareStatement("INSERT INTO items(brand, model, descr, userid, buynow, startprice, enddate) VALUES(?,?,?,?,?,?,?)");
        stmt.setString(1,brand);
        stmt.setString(2,model);
        stmt.setString(3,descr);
        stmt.setInt(4,userid);
        stmt.setInt(5,buynow);
        stmt.setInt(6,startprice);
        stmt.setString(7, enddate);
        // stmt.setString(8, photoid);
        stmt.executeUpdate();
        
        String phototype = ".jpg";
        //*Blob photo = ; /!!Kuva on kannassa Blob muodossa, ilmeisesti näin sen pitää olla? Tähän on määriteltävä Blob arvo!!
        String photosize = "100x100";
        String photoname = "hienojopo";
        
        
        //Lisää kuvan viimeksi lisätyn itemidn perusteella
        PreparedStatement stmt2 = con.prepareStatement("INSERT INTO photos(itemid, phototype, photosize, photoname) VALUES (LAST_INSERT_ID(),?,?,?)");
        stmt2.setString(1, phototype);
        //*stmt2.setBlob(2, ); //!!BLOB ARVO MÄÄRITELTÄVÄ JA LISÄTTÄVÄ, MYÖS QUERYYN YKSI ? MERKKI LISÄÄ!!
        stmt2.setString(2, photosize);
        stmt2.setString(3, photoname);
        stmt2.executeUpdate();

        con.close();
    }catch(Exception e){ System.out.println(e);}
}

//Poista esine
static void deleteItem(){
    try{
        Class.forName("com.mysql.jdbc.Driver");  
        Connection con=DriverManager.getConnection(  
        "jdbc:mysql://localhost:3306/auctions?zeroDateTimeBehavior=convertToNull", "root", "root");
        
        int itemid = 19; //Queryssä käytettävä arvo
        PreparedStatement stmt = con.prepareStatement("DELETE FROM users WHERE userid=?");
        stmt.setInt(1,itemid);
        stmt.executeUpdate();
        con.close();
    }catch(Exception e){ System.out.println(e);}
}

//Lisää tarjous
static void addBid(){
    try{Class.forName("com.mysql.jdbc.Driver");  
        Connection con=DriverManager.getConnection(  
        "jdbc:mysql://localhost:3306/auctions?zeroDateTimeBehavior=convertToNull", "root", "root");
       
        String itemid = "";
        String userid = "";
        String bid = "";
        PreparedStatement stmt = con.prepareStatement("INSERT INTO bids(itemid, userid, bid) VALUES (?,?,?)");
        stmt.setString(1, itemid);
        stmt.setString(2, userid);
        stmt.setString(3, bid);
        stmt.executeUpdate();
        con.close();
    }catch(Exception e){ System.out.println(e);}
}

//Hakee kaikki esineet
static void getItems(){
    try{Class.forName("com.mysql.jdbc.Driver");  
        Connection con=DriverManager.getConnection(  
        "jdbc:mysql://localhost:3306/auctions?zeroDateTimeBehavior=convertToNull", "root", "root");
        
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM items"); //Käytetään ResultSettiä ja määritellään query
        while(rs.next()){ //Tarvittava looppi, jotta kaikki data saadaan haettua
            //Määrittää muuttujan nimen jokaiselle sarakkeelle
            int itemid = rs.getInt("itemid");
            String brand = rs.getString("brand");
            String model = rs.getString("model");
            int userid = rs.getInt("userid");
            String buynow = rs.getString("buynow");
            String startprice = rs.getString("startprice");
            String enddate = rs.getString("enddate");
            String active = rs.getString("active");
            String photoid = rs.getString("photoid");
            rs.getMetaData(); //Hakee metedatan = sarakkeiden nimet, tätä ei kuitenkaan käytetä missään toistaiseksi.
            //Testiä varten tehty printtaus
            System.out.println(itemid + "\t" + brand +
                               "\t" + model + "\t" + userid +
                               "\t" + startprice);
        }
        con.close();
    }catch(Exception e){ System.out.println(e);}
}

//Hakee yksittäisen käyttäjän tiedot
static void getUser(){
    try{
        Class.forName("com.mysql.jdbc.Driver");  
        Connection con=DriverManager.getConnection(  
        "jdbc:mysql://localhost:3306/auctions?zeroDateTimeBehavior=convertToNull", "root", "root");
        
        PreparedStatement stmt=null;
        ResultSet rs=null;
        int userid = 1; //Halutun käyttäjän määrittely
        stmt=con.prepareStatement("SELECT * FROM users WHERE userid=?");
        stmt.setInt(1,userid);
        rs=stmt.executeQuery();
        while(rs.next())
        {
            userid = rs.getInt("userid");
            String firstname = rs.getString("firstname");
            String lastname = rs.getString("lastname");
            String email = rs.getString("email");
            String phone = rs.getString("phone");
            String password = rs.getString("password");
            String username = rs.getString("username");
            rs.getMetaData();
            //Testi tulostus
            System.out.println(userid + "\t" + firstname +
                               "\t" + lastname + "\t" + email +
                               "\t" + username);
            
        }
        con.close();
    }catch(Exception e){ System.out.println(e);}
}

//Hakee halutun esineen tiedot, kuvan ja esineelle tehdyt tarjoukset
static void getItemInfo(){
    try{Class.forName("com.mysql.jdbc.Driver");  
        Connection con=DriverManager.getConnection(  
        "jdbc:mysql://localhost:3306/auctions?zeroDateTimeBehavior=convertToNull", "root", "root");
        
        ResultSet rs=null;
        int itemid = 7; //Halutun esineen määrittely
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM items AS i LEFT JOIN photos AS p ON i.itemid=p.itemid LEFT JOIN bids AS b ON i.itemid=b.itemid WHERE i.itemid=?");
        stmt.setInt(1,itemid);
        rs = stmt.executeQuery();
        while(rs.next()){
            itemid = rs.getInt("itemid");
            String brand = rs.getString("brand");
            String model = rs.getString("model");
            int userid = rs.getInt("userid");
            String buynow = rs.getString("buynow");
            String startprice = rs.getString("startprice");
            String enddate = rs.getString("enddate");
            String active = rs.getString("active");
            int photoid = rs.getInt("photoid");
            String phototype = rs.getString("phototype");
            //*Blob photo = rs2.getString(photo); //!!KUVAN MÄÄRITTELY!!
            String photosize = rs.getString("photosize");
            String photoname = rs.getString("photoname");
            int bidid = rs.getInt("bidid");
            int userid2 = rs.getInt("b.userid");
            String bid = rs.getString("bid");
            
            rs.getMetaData();
            //Testi tulostus
            System.out.println(itemid + "\t" + brand +
                               "\t" + model + "\t" + userid +
                               "\t" + photoname + "\t" + userid2 + "\t" + bid);
        }
        con.close();
    }catch(Exception e){ System.out.println(e);}
}

    static Image getItemPhoto(int itemid) {
        Image image=null;
        try{Class.forName("com.mysql.jdbc.Driver");
            Connection con=DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/auctions?zeroDateTimeBehavior=convertToNull", "root", "root");

            ResultSet rs=null;
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM photos WHERE itemid=?");
            stmt.setInt(1,itemid);
            rs = stmt.executeQuery();
            while(rs.next()){
                byte[] photoAsBytes = rs.getBytes("photo");
                StreamResource.StreamSource streamSource = new StreamResource.StreamSource() {
                    public InputStream getStream() {
                        return new ByteArrayInputStream(photoAsBytes);
                    }
                };
                StreamResource resource = new StreamResource(streamSource, "filename");
                image = new Image("image title", resource);


            }
            con.close();
        }catch(Exception e){ System.out.println(e);}
        return image;
    }

//Hakee tietyn käyttäjän kaikki esineet ja niiden kuvat
static void getUsersItems(){
    try{Class.forName("com.mysql.jdbc.Driver");  
        Connection con=DriverManager.getConnection(  
        "jdbc:mysql://localhost:3306/auctions?zeroDateTimeBehavior=convertToNull", "root", "root");

        ResultSet rs=null;
        int userid = 4; //Käyttäjän määrittely
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM items AS i LEFT JOIN photos AS p ON i.itemid=p.itemid WHERE i.userid=?");
        stmt.setInt(1,userid);
        rs = stmt.executeQuery();
        while(rs.next()){
            int itemid = rs.getInt("itemid");
            String brand = rs.getString("brand");
            String model = rs.getString("model");
            userid = rs.getInt("userid");
            String buynow = rs.getString("buynow");
            String startprice = rs.getString("startprice");
            String enddate = rs.getString("enddate");
            String active = rs.getString("active");
            int photoid = rs.getInt("photoid");
            String phototype = rs.getString("phototype");
            //*Blob photo = rs2.getString(photo); //!!KUVAN MÄÄRITTELY!!
            String photosize = rs.getString("photosize");
            String photoname = rs.getString("photoname");
            
            rs.getMetaData();
            System.out.println(itemid + "\t" + brand +
                               "\t" + model + "\t" + userid +
                               "\t" + photoname);
        }
        con.close();
    }catch(Exception e){ System.out.println(e);}
}

//Tietyn käyttäjän tekemät tarjoukset
static void getUsersBids(){
    try{Class.forName("com.mysql.jdbc.Driver");  
        Connection con=DriverManager.getConnection(  
        "jdbc:mysql://localhost:3306/auctions?zeroDateTimeBehavior=convertToNull", "root", "root");
        
        ResultSet rs=null;
        int userid = 3; //Käyttäjän määrittely
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM bids WHERE userid=?");
        stmt.setInt(1,userid);
        rs = stmt.executeQuery();
        while(rs.next()){
            userid = rs.getInt("userid");
            int bidid = rs.getInt("bidid");
            int itemid = rs.getInt("itemid");
            String bid = rs.getString("bid");
            
            rs.getMetaData();
            //Testi tulostus
            System.out.println(itemid + "\t" + bidid +
                               "\t" + bid + "\t" + userid);
        }
        con.close();
    }catch(Exception e){ System.out.println(e);}
}

static ArrayList<AuctionItem> listAllItems(){
	ArrayList<AuctionItem> items = new ArrayList<AuctionItem>();
	
	String query = "SELECT i.itemid,brand , model, descr , buynow, startprice, enddate,active, MAX(bid) as bid FROM items AS i LEFT JOIN photos AS p ON i.itemid=p.itemid LEFT JOIN bids AS b ON i.itemid=b.itemid GROUP BY i.itemid, p.photoid";
	
	try(Connection c = getConnection();
		PreparedStatement ps = c.prepareStatement(query);
//			ps.setInt(1, );
			){
		
		ResultSet rs = ps.executeQuery();
		
	while(rs.next()){
		
		AuctionItem ai = new AuctionItem();
		
		ai.setItemid(rs.getInt("itemid"));
		ai.setBrand(rs.getString("brand"));
		ai.setModel(rs.getString("model"));
		ai.setDescr(rs.getString("descr"));
		ai.setBuynow(rs.getDouble("buynow"));
		ai.setStartprice(rs.getDouble("startprice"));
		ai.setEnddate(rs.getDate("enddate"));
//		ai.setUserid(rs.getInt("userid"));
		ai.setActive(rs.getInt("active"));
		ai.setImage(getItemPhoto(rs.getInt("itemid")));
		ai.setHighest(rs.getDouble("bid"));
		items.add(ai);
		
//		ai.setImage();
	}
		
	
	
	}catch(SQLException e){
		e.printStackTrace();
	}
	
	
	
	return items;
}


static ArrayList<AuctionItem> listUsersItems(){
	ArrayList<AuctionItem> items = new ArrayList<AuctionItem>();
	
	String query = "SELECT i.itemid,brand , model, descr , buynow, startprice, enddate,active, MAX(bid) as bid FROM items AS i LEFT JOIN photos AS p ON i.itemid=p.itemid LEFT JOIN bids AS b ON i.itemid=b.itemid GROUP BY i.itemid, p.photoid";
	
	try(Connection c = getConnection();
		PreparedStatement ps = c.prepareStatement(query);
//			ps.setInt(1, );
			){
		
		ResultSet rs = ps.executeQuery();
		
	while(rs.next()){
		
		AuctionItem ai = new AuctionItem();
		
		ai.setItemid(rs.getInt("itemid"));
		ai.setBrand(rs.getString("brand"));
		ai.setModel(rs.getString("model"));
		ai.setDescr(rs.getString("descr"));
		ai.setBuynow(rs.getDouble("buynow"));
		ai.setStartprice(rs.getDouble("startprice"));
		ai.setEnddate(rs.getDate("enddate"));
		ai.setUserid(rs.getInt("userid"));
		ai.setActive(rs.getInt("active"));
		ai.setHighest(rs.getDouble("bid"));
		ai.setImage(getItemPhoto(rs.getInt("itemid")));
		items.add(ai);
		

	}
		
	
	
	}catch(SQLException e){
		e.printStackTrace();
	}
	
	
	
	return items;
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
