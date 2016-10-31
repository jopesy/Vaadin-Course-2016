



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
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;


public class UserView extends CustomComponent implements View {


    public static final String NAME = "user";
    private final VerticalLayout Layout;
    private final HorizontalLayout buttonContainer;
    private  final Button backToMainButton;
    private Table ownItems;
    private Table ownBids;
//    private SQLContainer itemContainer;
//    private SQLContainer bidContainer;
    private Table items;
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

        ownItems = new Table();
        ownItems.setSelectable(true);
        ownItems.setSizeFull();

        ownBids = new Table();
        ownBids.setSelectable(true);
        ownBids.setSizeFull();

        
      
       
       
        try {
            pool = new SimpleJDBCConnectionPool("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/auctions?zeroDateTimeBehavior=convertToNull", "root", "root", 2, 5);
//            FreeformQuery query1 = new FreeformQuery(
//                    "SELECT photoid as Photo, brand as Brand, model as Model, descr as Description, buynow as 'Buy now price', startprice as 'Starting price', enddate as 'End date', MAX(bid) as 'Highest bid' FROM items AS i LEFT JOIN photos AS p ON i.itemid=p.itemid LEFT JOIN bids AS b ON i.itemid=b.itemid WHERE i.userid=" + userid + " GROUP BY i.itemid, p.photoid", pool
//            );
            addItems(DatabaseHelper.listAllItems());
//            FreeformQuery query2 = new FreeformQuery(
//                    "SELECT photoid as Photo, brand as Brand, model as Model, descr as Description, buynow as 'Buy now price', startprice as 'Starting price', enddate as 'End date', MAX(bid) as 'My bid' FROM items AS i LEFT JOIN photos AS p ON i.itemid=p.itemid LEFT JOIN bids AS b ON i.itemid=b.itemid WHERE b.userid=" + userid + " GROUP BY i.itemid, p.photoid", pool
//            );

//            itemContainer = new SQLContainer(query1);
//            bidContainer = new SQLContainer(query2);
        } catch (Exception e) {
            System.out.println("Table query for user items failed");
            System.out.println(e.getMessage());
        }

        
//        ownItems.setContainerDataSource(itemContainer);
//        ownBids.setContainerDataSource(bidContainer);

        ownItems.setSelectable(false);
        ownBids.setSelectable(false);
        
       

        /*
        Kyseinen pätkä koodia korvaa taulun ensimmäisen sarakkeen. Tällä vois ehkä jotenki saada kuvan esille....
        ownItems.addGeneratedColumn("Photo", new Table.ColumnGenerator() {
            public Object generateCell(Table source, Object itemId, Object columnId) {
                TextField tf = new TextField();
                return tf;
            }
        });
        */


        Collection<?> itemIds = ownItems.getItemIds();
        for(Object itemId:itemIds){
            Property p = ownItems.getContainerProperty(itemId, "Photo");
            System.out.println(p.getValue());

            if(p.getValue() != null) {
                byte[] photoAsBytes = DatabaseHelper.getItemPhoto((int) p.getValue());
                StreamResource.StreamSource streamSource = new StreamResource.StreamSource() {
                    public InputStream getStream() {
                        return new ByteArrayInputStream(photoAsBytes);
                    }
                };

                StreamResource resource = new StreamResource(streamSource, "filename");
                Image image = new Image("image title", resource);

                p.setValue(image);
            }
        }


//        ownItems.setSortEnabled(false);
//        ownBids.setSortEnabled(false);

//        ownItems.setPageLength(0);
        ownBids.setPageLength(0);

        Layout = new VerticalLayout();
        Layout.addComponents(buttonContainer, items, ownBids);
        Layout.setMargin(true);
        Layout.setSpacing(true);
        Layout.setComponentAlignment(buttonContainer, Alignment.TOP_RIGHT);
//        Layout.setComponentAlignment(ownItems, Alignment.MIDDLE_CENTER);
        Layout.setComponentAlignment(ownBids, Alignment.MIDDLE_CENTER);
        setCompositionRoot(Layout);
    }


    public void addItems(ArrayList<AuctionItem> list){
    	items = new Table();
    	items.addContainerProperty("image", Image.class, null);
    	items.addContainerProperty("brand", String.class, "BRAND");
        items.addContainerProperty("model", String.class, "MODEL");
        items.addContainerProperty("desc", VerticalLayout.class, null);
        items.addContainerProperty("buynow",Double.class, 0.0);
        items.addContainerProperty("current", Double.class, 0.0);
        items.addContainerProperty("starting", Double.class, 0.0);
        items.addContainerProperty("end",Date.class,null);
        items.addContainerProperty("delete", Button.class, null);

        for(AuctionItem ai : list){
        	if(ai.getActive()==0)continue; 
	        Object it =  items.addItem();
	        Item item = items.getItem(it);
	        
	        Property p1 = item.getItemProperty("image");
	        p1.setValue(null);
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
	        p1.setValue(new Button("Delete"));
	
        }
       }
    
    
    
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

}
