/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.bicycles;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

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
    
    public MainView() {
        navBarLayout = new HorizontalLayout();
        userLabel = new Label("Not logged in");
        userLabel.setCaption("Logged in as:");
        loginButton = new Button("Log in");
        loginButton.addClickListener( e-> {
            getUI().getNavigator().navigateTo(LoginView.NAME);
        });
        
        navBarLayout.addComponents(userLabel, loginButton);
        navBarLayout.setMargin(true);
        navBarLayout.setSpacing(true);
        navBarLayout.setComponentAlignment(loginButton, Alignment.TOP_RIGHT);
        navBarLayout.setSizeFull();
        setCompositionRoot(navBarLayout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        String defaultText = "Not logged in";
        String username;
        if(getSession().getAttribute("username") != null){
            username = String.valueOf(getSession().getAttribute("username"));
            if(!username.isEmpty()){
                userLabel.setValue(username);
                loginButton.setCaption("Log out");
            }
        }
        else {
            userLabel.setValue(defaultText);
        }
    }
    
}
