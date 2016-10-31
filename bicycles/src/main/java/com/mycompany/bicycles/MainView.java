/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.bicycles;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.WebBrowser;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import com.vaadin.data.validator.StringLengthValidator;
import com.mycompany.bicycles.utilities.AuctionItem;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.validator.RegexpValidator;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * The main view of the application,
 * listing all the biddable items
 */
public class MainView extends CustomComponent implements View{

    public static final String NAME = "main";
    private final HorizontalLayout navBarLayout;
    private final VerticalLayout mainLayout;
    private final Label userLabel;
    private final Button loginButton;
    private final Button logoutButton;
    private final Button registerButton;
    private final Button userPageButton;
    private final HorizontalLayout buttonContainer;

    private final HorizontalLayout buttonContainer2;
    private final Button openCreationWindow;
    private Window createAuctionWindow;
    private final VerticalLayout auctionContainer;
    private final Button closeButton;
    private final Button createAuction;
    private TextField bikeBrand;
    private TextField bikeModel;
    private TextField bikeDescription;
    private TextField startingPrice;
    private TextField buyoutPrice;
    private PopupDateField endingDate;
    private Date currentTime;
    private Table items;
    
    public MainView() {
        navBarLayout = new HorizontalLayout();
        navBarLayout.addStyleName("navbar");
        userLabel = new Label("Not logged in");
        userLabel.setCaption("Logged in as:");
        userLabel.setStyleName("user-label");
        loginButton = new Button("Log in");
        loginButton.addClickListener( e-> {
            getUI().getNavigator().navigateTo(LoginView.NAME);
        });
        logoutButton = new Button("Log out");
        logoutButton.addClickListener( click -> {
            logout();
        });
        logoutButton.setVisible(false);

        registerButton = new Button("Register");
        registerButton.addClickListener( click -> {
            getUI().getNavigator().navigateTo(RegisterView.NAME);
        });

        userPageButton = new Button("My Page");
        userPageButton.addClickListener(e-> {
            getUI().getNavigator().navigateTo(UserView.NAME);
        });
        userPageButton.setVisible(false);

        createAuctionWindow = new Window("Create Auction");
        auctionContainer = new VerticalLayout();
        auctionContainer.setMargin(true);
        createAuctionWindow.setContent(auctionContainer);
        createAuctionWindow.center();
        createAuctionWindow.setStyleName("auction-form");

        Panel panel = new Panel();
        panel.setSizeUndefined();
        FormLayout content = new FormLayout();
        content.setMargin(true);
        content.setSpacing(true);

        panel.setContent(content);
        panel.setStyleName("auction-form-panel");
        addItems(DatabaseHelper.listAllItems());
        openCreationWindow = new Button("Create Auction");
        openCreationWindow.addClickListener( h -> {
            getUI().addWindow(createAuctionWindow);
            navBarLayout.setVisible(false);
        });
        openCreationWindow.setVisible(false);

        bikeBrand = new TextField("Brand:");
        bikeBrand.setRequired(true);
        bikeBrand.addValidator(new StringLengthValidator("Brand length can be 25 characters at most!", 0, 25, true));

        bikeModel = new TextField("Model:");
        bikeModel.addValidator(new StringLengthValidator("Model length can be 25 characters at most!", 0, 25, true));

        bikeDescription = new TextField("Description:");
        bikeDescription.addValidator(new StringLengthValidator("Description length can be 255 characters at most!", 0, 255, true));

        startingPrice = new TextField("Starting price:");
        startingPrice.addValidator(new RegexpValidator("\\d+", "Starting price must be digits only!"));
        startingPrice.setRequired(true);

        buyoutPrice = new TextField("Buyout price:");
        buyoutPrice.addValidator(new RegexpValidator("\\d+", "Buyout price must be digits only!"));

        endingDate = new PopupDateField("Endind date:");
        currentTime = new Date();

        closeButton = new Button("Close");
        closeButton.addClickListener( click -> {
            getUI().removeWindow(createAuctionWindow);
            navBarLayout.setVisible(true);
        });



        createAuction = new Button("Create Auction");
        createAuction.addClickListener( click -> {
            validateInput();
            getUI().removeWindow(createAuctionWindow);
            navBarLayout.setVisible(true);
        });

        buttonContainer2 = new HorizontalLayout();
        buttonContainer2.setSpacing(true);
        buttonContainer2.addComponents(createAuction, closeButton);

        endingDate.setResolution(Resolution.MINUTE);
        endingDate.setRangeStart(currentTime);
        endingDate.setRequired(true);

        content.addComponents(bikeBrand, bikeModel, bikeDescription, startingPrice, buyoutPrice, endingDate,  buttonContainer2);

        auctionContainer.addComponent(panel);
        auctionContainer.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);

        buttonContainer = new HorizontalLayout();
        buttonContainer.setSpacing(true);
        buttonContainer.addComponents(registerButton, userPageButton,openCreationWindow, loginButton, logoutButton);


        navBarLayout.addComponents(userLabel, buttonContainer,items);
        navBarLayout.setMargin(true);
        navBarLayout.setSpacing(true);
        navBarLayout.setComponentAlignment(buttonContainer, Alignment.TOP_RIGHT);
        navBarLayout.setSizeFull();
        
        mainLayout = new VerticalLayout();
        mainLayout.addComponents(navBarLayout,items);
//        items.setSizeFull();
        mainLayout.setComponentAlignment(items, Alignment.TOP_CENTER);
        setCompositionRoot(mainLayout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        String defaultText = "Not logged in";
        String username;
        if(VaadinSession.getCurrent().getSession().getAttribute("username") != null){
            username = String.valueOf(VaadinSession.getCurrent().getSession().getAttribute("username"));
            if(!username.isEmpty()){
                userLabel.setValue(username);
                loginButton.setVisible(false);
                logoutButton.setVisible(true);
                registerButton.setVisible(false);
                userPageButton.setVisible(true);
                openCreationWindow.setVisible(true);

            }
            else {
                loginButton.setVisible(true);
                logoutButton.setVisible(false);
                registerButton.setVisible(true);
                userPageButton.setVisible(false);
                openCreationWindow.setVisible(false);
                items.setVisible(false);
            }
        }
        else {
            userLabel.setValue(defaultText);
        }
    }

    public void logout() {
        System.out.println("Log out");
        VaadinSession.getCurrent().getSession().setAttribute("username", null);
        userLabel.setValue("Not logged in");
        getUI().getNavigator().navigateTo(MainView.NAME);

        new Notification("Logged out!",
                null,
                Notification.Type.TRAY_NOTIFICATION, true)
                .show(Page.getCurrent());
    }

    private void validateInput(){
        String brand = bikeBrand.getValue();
        String model = bikeModel.getValue();
        String descr = bikeDescription.getValue();
        int userid = (int) VaadinSession.getCurrent().getSession().getAttribute("userid");
        int buynow;
        int startprice;
        Date enddate = endingDate.getValue();
        String edate;


        TextField[] reqFields = {bikeBrand, startingPrice};
        boolean formFilled = true;



        for (TextField field : reqFields) {
            if(field.isEmpty()) {
                formFilled = false;
                field.setRequiredError("Please fill out this field");
                showNotification("Please fill out required fields!", null);
            }
        }

        if(bikeBrand.isEmpty()){
            formFilled = false;
            bikeBrand.setRequiredError("Please enter bike brand");
        }


        if(!startingPrice.isValid()){
            return;
        }else{
            try{
                startprice = Integer.parseInt(startingPrice.getValue());
            }catch(NumberFormatException e){
                showNotification("Oops!", "Starting price must be assigned!");
                return;
            }
        }

        if(!buyoutPrice.isValid()) {
            showNotification("Oops!", "Only numbers in price!");
            return;
        }else{
            try {
                buynow = Integer.parseInt(buyoutPrice.getValue());
            }catch(NumberFormatException e){
                buynow = 0;
            }
        }
        if(endingDate.isEmpty()){
            showNotification("Oops!", "Please fill out the date!");
            return;
        }else{
            edate = toString(enddate);
        }
        if(enddate.compareTo(currentTime)<1){
            showNotification("Sorry!", "Date input invalid");
            return;
        }
        if(!formFilled) {
            showNotification("Oops!", "Please fill out all the required fields!");
            return;
        }else{
            DatabaseHelper.addItem(brand,model,descr,userid,buynow,startprice,edate);
            showNotification("Auction creation succesful!", null);
        }
    }

    private void showNotification(String caption, String message) {
        new Notification(caption, message,
                Notification.Type.TRAY_NOTIFICATION, true)
                .show(Page.getCurrent());
    }

    private String toString(Date date){
        return date.getYear()+1900 + "-" + (date.getMonth()+1) + "-" + date.getDate() + " " + date.getHours() + ":" + date.getMinutes() + ":" + "00";
    }

    public void addItems(ArrayList<AuctionItem> list){
    	items = new Table();
    	items.addContainerProperty("image", Image.class, null,"Photo",null,null);
    	items.addContainerProperty("brand", String.class, "BRAND","Brand",null,null);
        items.addContainerProperty("model", String.class, "MODEL","Model",null,null);
        items.addContainerProperty("desc", VerticalLayout.class, null,"Description",null,null);
        items.addContainerProperty("buynow",Double.class, 0.0,"Buy now price:",null,null);
        items.addContainerProperty("current", Double.class, 0.0,"Highest bid:",null,null);
        items.addContainerProperty("starting", Double.class, 0.0,"Starting price:", null,null);
        items.addContainerProperty("end",Date.class,null,"Auction ends:",null,null);
        items.addContainerProperty("makebid", Button.class, null,"",null,null);

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
	        p1=(Property)item.getItemProperty("makebid");
	        p1.setValue(new Button("Place bid"));
	
        }
       }
    


}