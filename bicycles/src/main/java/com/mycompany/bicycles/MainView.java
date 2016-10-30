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
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;

import java.util.Date;

/**
 *
 * The main view of the application,
 * listing all the biddable items
 */
public class MainView extends CustomComponent implements View{
    
    public static final String NAME = "main";
    private final HorizontalLayout navBarLayout;
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
            getUI().removeWindow(createAuctionWindow);
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

        openCreationWindow = new Button("Create Auction");
        openCreationWindow.addClickListener( h -> {
            getUI().addWindow(createAuctionWindow);
        });
        openCreationWindow.setVisible(false);

        bikeBrand = new TextField("Brand:");
        bikeBrand.setRequired(true);

        bikeModel = new TextField("Model:");
        bikeModel.setRequired(true);

        bikeDescription = new TextField("Description:");
        startingPrice = new TextField("Starting price:");
        startingPrice.setRequired(true);

        buyoutPrice = new TextField("Buyout price:");
        endingDate = new PopupDateField("Endind date:");

        closeButton = new Button("Close");
        closeButton.addClickListener( click -> {
            getUI().removeWindow(createAuctionWindow);
        });



        createAuction = new Button("Create Auction");
        createAuction.addClickListener( click -> {
            validateInput();
            getUI().removeWindow(createAuctionWindow);
        });

        buttonContainer2 = new HorizontalLayout();
        buttonContainer2.setSpacing(true);
        buttonContainer2.addComponents(createAuction, closeButton);

        endingDate.setResolution(Resolution.MINUTE);

        content.addComponents(bikeBrand, bikeModel, bikeDescription, startingPrice, buyoutPrice, endingDate,  buttonContainer2);

        auctionContainer.addComponent(panel);
        auctionContainer.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);

        buttonContainer = new HorizontalLayout();
        buttonContainer.setSpacing(true);
        buttonContainer.addComponents(registerButton, userPageButton,openCreationWindow, loginButton, logoutButton);
        
        
        navBarLayout.addComponents(userLabel, buttonContainer);
        navBarLayout.setMargin(true);
        navBarLayout.setSpacing(true);
        navBarLayout.setComponentAlignment(buttonContainer, Alignment.TOP_RIGHT);
        navBarLayout.setSizeFull();
        setCompositionRoot(navBarLayout);
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
        int buynow = Integer.parseInt(buyoutPrice.getValue());
        int startprice = Integer.parseInt(startingPrice.getValue());
        Date enddate = endingDate.getValue();
        String edate = toString(enddate);


        TextField[] reqFields = {bikeBrand, startingPrice};
        boolean formFilled = true;

        // Check that all the required fields are filled
        for (TextField field : reqFields) {
            if(field.isEmpty()) {
                formFilled = false;
                field.setRequiredError("Please fill out this field");
            }
        }
        if(!formFilled) {
            showNotification("Oops!", "Please fill out all the required fields!");
            return;
        }else{
            DatabaseHelper.addItem(brand,model,descr,userid,buynow,startprice,edate);
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


    
}
