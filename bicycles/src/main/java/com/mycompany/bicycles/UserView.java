



package com.mycompany.bicycles;


import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.View;


public class UserView extends CustomComponent implements View {


    public static final String NAME = "user";
    private final VerticalLayout Layout;
    private final HorizontalLayout buttonContainer;
    private  final Button backToMainButton;
    private Table ownItems;
    private Table ownBids;
    private SQLContainer itemContainer;
    private SQLContainer bidContainer;
    private JDBCConnectionPool pool;
    private int userid;



    public UserView(){



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
            FreeformQuery query1 = new FreeformQuery(
                    "SELECT photo as Photo, brand as Brand, model as Model, descr as Description, buynow as 'Buy now price', startprice as 'Starting price', enddate as 'End date', bid as 'Highest bid' FROM items AS i LEFT JOIN photos AS p ON i.itemid=p.itemid LEFT JOIN bids AS b ON i.itemid=b.itemid WHERE i.userid=" + userid, pool
            );

            FreeformQuery query2 = new FreeformQuery(
                    "SELECT photo as Photo, brand as Brand, model as Model, descr as Description, buynow as 'Buy now price', startprice as 'Starting price', enddate as 'End date', bid as 'My bid' FROM items AS i LEFT JOIN photos AS p ON i.itemid=p.itemid LEFT JOIN bids AS b ON i.itemid=b.itemid WHERE b.userid=" + userid, pool
            );

            itemContainer = new SQLContainer(query1);
            bidContainer = new SQLContainer(query2);
        } catch (Exception e) {
            System.out.println("Table query for user items failed");
            System.out.println(e.getMessage());
        }


        ownItems.setContainerDataSource(itemContainer);
        ownBids.setContainerDataSource(bidContainer);

        ownItems.setSelectable(false);
        ownBids.setSelectable(false);

        /*
        Kyseinen pätkä koodia korvaa taulun ensimmäisen sarakkeen. Tällä vois ehkä jotenki saada kuvan esille....
        ownItems.addGeneratedColumn("Photo", new Table.ColumnGenerator() {
            public Object generateCell(Table source, Object itemId, Object columnId) {
                //Tähän tarttis kai saada joku tapa esittää kuvat...?
                TextField tf = new TextField();
                return tf;
            }
        });
        */

        ownItems.setSortEnabled(false);
        ownBids.setSortEnabled(false);

        ownItems.setPageLength(0);
        ownBids.setPageLength(0);

        Layout = new VerticalLayout();
        Layout.addComponents(buttonContainer, ownItems, ownBids);
        Layout.setMargin(true);
        Layout.setSpacing(true);
        Layout.setComponentAlignment(buttonContainer, Alignment.TOP_RIGHT);
        Layout.setComponentAlignment(ownItems, Alignment.MIDDLE_CENTER);
        Layout.setComponentAlignment(ownBids, Alignment.MIDDLE_CENTER);
        setCompositionRoot(Layout);
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }





}
