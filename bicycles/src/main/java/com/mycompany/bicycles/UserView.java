



package com.mycompany.bicycles;


import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
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
    //private TableQuery tq;
    private int userid;





    public UserView(){


        //Hakee tällä hetkellä userid 1 mukaan koska jostain syystä toi alempi koodinpätkä ei toimi,
        //väittäen userid olevan object tyyppinen eikä int?
        //userid = getSession().getAttribute("userid");
        userid = 1;

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



        //Queryt vaatii muokkausta vielä...
        try {
            pool = new SimpleJDBCConnectionPool("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/auctions?zeroDateTimeBehavior=convertToNull", "root", "root", 2, 5);
            //tq = new TableQuery("items", pool);  helppo tapa hakea koko taulu
            FreeformQuery query1 = new FreeformQuery(
                    " SELECT * FROM items WHERE userid = " + userid, pool
            );

            FreeformQuery query2 = new FreeformQuery(
                    " SELECT * FROM bids WHERE userid = " + userid, pool
            );

            itemContainer = new SQLContainer(query1);
            bidContainer = new SQLContainer(query2);

        } catch (Exception e){System.out.println("Table query for user items failed");}

        ownItems.setContainerDataSource(itemContainer);
        ownBids.setContainerDataSource(bidContainer);

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
