



package com.mycompany.bicycles;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;

import com.mycompany.bicycles.utilities.AuctionItem;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;


public class UserView extends CustomComponent implements View {


    public static final String NAME = "user";
    private final VerticalLayout Layout;
    private final HorizontalLayout buttonContainer;
    private  final Button backToMainButton;

    private Label ownItemsLabel;
    private Label ownBidsLabel;


//    private SQLContainer itemContainer;
//    private SQLContainer bidContainer;
    private Table items;
    private Table bids;
    private JDBCConnectionPool pool;
    private int userid;



    public UserView(){

    	items = new Table();


        userid = (int) VaadinSession.getCurrent().getSession().getAttribute("userid");


        backToMainButton = new Button("Back");
        backToMainButton.addClickListener(e-> {
            getUI().getNavigator().navigateTo(MainView.NAME);
        });
        buttonContainer = new HorizontalLayout();
        buttonContainer.setSpacing(true);
        buttonContainer.addComponent(backToMainButton);
        
        ownItemsLabel = new Label("My items:");
        ownBidsLabel = new Label("My bids:");
        ownItemsLabel.setStyleName("my-items-label");
        ownBidsLabel.setStyleName("my-bids-label");

        
      
       
       
        try {
            pool = new SimpleJDBCConnectionPool("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/auctions?zeroDateTimeBehavior=convertToNull", "root", "root", 2, 5);

            addItems(DatabaseHelper.listUsersItems());
            addBids(DatabaseHelper.getUsersBids());


        } catch (Exception e) {
         
            e.printStackTrace();
        }

        Layout = new VerticalLayout();

        Layout.addComponents(buttonContainer, ownItemsLabel, items, ownBidsLabel, bids);

        Layout.setComponentAlignment(items, Alignment.TOP_CENTER);
        Layout.setMargin(true);
        Layout.setSpacing(true);
        Layout.setComponentAlignment(buttonContainer, Alignment.TOP_RIGHT);
//        Layout.setComponentAlignment(ownItems, Alignment.MIDDLE_CENTER);
        Layout.setComponentAlignment(bids, Alignment.MIDDLE_CENTER);
        setCompositionRoot(Layout);
    }


    public void addItems(ArrayList<AuctionItem> list){
    	items = new Table();
    	items.addContainerProperty("image", Image.class, null,"Photo",null,null);
    	items.addContainerProperty("brand", String.class, "BRAND","Brand",null,null);
        items.addContainerProperty("model", String.class, "MODEL","Model",null,null);
        items.addContainerProperty("desc", VerticalLayout.class, null,"Description",null,null);
        items.addContainerProperty("buynow",Double.class, 0.0,"Buy now price:",null,null);
        items.addContainerProperty("current", Double.class, 0.0,"Current price",null,null);
        items.addContainerProperty("starting", Double.class, 0.0,"Staring price",null,null);
        items.addContainerProperty("end",Date.class,null,"Ends:",null,null);
        items.addContainerProperty("delete", Button.class, null);
        
        items.setPageLength(0);
        items.setStyleName("table-items");
        

        for(AuctionItem ai : list){
        	if(ai.getActive()==0)continue; 
	        Object it =  items.addItem();
	        Item item = items.getItem(it);
	        
	        Property p1 = item.getItemProperty("image");
	        p1.setValue(ai.getImage());
	        p1 = (Property)item.getItemProperty("brand");
	        p1.setValue(ai.getBrand());
	        p1 = (Property)item.getItemProperty("model");
	        p1.setValue(ai.getModel());
	        p1 = (Property)item.getItemProperty("desc");
//	        p1.setValue(ai.getDescr());
	        p1 = (Property)item.getItemProperty("buynow");
	        p1.setValue(ai.getBuynow());
	        p1 =(Property) item.getItemProperty("current");
	        p1.setValue(ai.getHighest());
	        p1 =(Property) item.getItemProperty("starting");
	        p1.setValue(ai.getStartprice());
	        p1 =(Property) item.getItemProperty("end");
	        p1.setValue(ai.getEnddate());
	        p1=(Property)item.getItemProperty("delete");
                Button del = new Button("Delete");
                del.setStyleName("button-delete");
	        p1.setValue(del);
	
        }
       }
    
    
    private void addBids(ArrayList<Bid> bidlist){
    	System.out.println("adding:" + bidlist.size() + " bids");
    	bids = new Table();
    	bids.addContainerProperty("image", Image.class, null);
    	bids.addContainerProperty("brand", String.class, "BRAND");
    	bids.addContainerProperty("model", String.class, "MODEL");
    	bids.addContainerProperty("desc", VerticalLayout.class, null);
    	bids.addContainerProperty("buynow",Double.class, 0.0);
//    	bids.addContainerProperty("current", Double.class, 0.0);
    	bids.addContainerProperty("starting", Double.class, 0.0);
    	bids.addContainerProperty("end",Date.class,null);
    	bids.addContainerProperty("yourbid",Double.class, 0.0);
        
        bids.setPageLength(0);
        bids.setStyleName("table-bids");
        
        

        for(Bid ai : bidlist){
        	
	        Object it =  bids.addItem();
	        Item item = bids.getItem(it);
	        
	        Property p1 = item.getItemProperty("image");
	        p1.setValue(ai.getImage());
	        p1 = (Property)item.getItemProperty("brand");
	        p1.setValue(ai.getBrand());
	        p1 = (Property)item.getItemProperty("model");
	        p1.setValue(ai.getModel());
	        p1 = (Property)item.getItemProperty("desc");
//	        p1.setValue(ai.getDescr());
	        p1 = (Property)item.getItemProperty("buynow");
	        p1.setValue(ai.getBuynow());
	        p1 =(Property) item.getItemProperty("yourbid");
	        p1.setValue(ai.getBid());
//	        p1=(Property)item.getItemProperty("current");
//	        p1.setValue(ai.get);
	        p1 =(Property) item.getItemProperty("starting");
	        p1.setValue(ai.getStartprice());
	        p1 =(Property) item.getItemProperty("end");
	        p1.setValue(ai.getEnddate());
        
        }
        
    }
    
    
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

}
