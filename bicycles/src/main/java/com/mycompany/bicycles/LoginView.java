/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.bicycles;

import com.vaadin.data.validator.EmailValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Link;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * The login view of the application
 * Contains a simple form for logging in
 */
public class LoginView extends CustomComponent implements View{
    public static final String NAME = "login";
    private final VerticalLayout layout;
    private final HorizontalLayout buttonContainer;
    private final TextField usernameField;
    private final PasswordField passwordField;
    private final Button registerButton;
    private final Button submitButton;
    private final Button returnButton;
    
    public LoginView() {
        usernameField = new TextField("Username:");
        usernameField.setRequired(true);
        usernameField.setInputPrompt("Enter username");
        //usernameField.addValidator(new EmailValidator("Please enter a valid email address"));
        usernameField.setInvalidAllowed(false);
        
        passwordField = new PasswordField("Password:");
        passwordField.setRequired(true);
        //passwordField.setInputPrompt("password");
        // TODO: Add password validator
        passwordField.setValue("");
        
        submitButton = new Button("Login");
        submitButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        submitButton.addClickListener( click -> {  
            validateFields();
        });
        
        registerButton = new Button("Register");
        registerButton.addClickListener( click -> {
            getUI().getNavigator().navigateTo(RegisterView.NAME);
        });
        
        returnButton = new Button("Cancel");
        returnButton.addClickListener( click -> {
            getUI().getNavigator().navigateTo(MainView.NAME);
        });
        
        buttonContainer = new HorizontalLayout();
        buttonContainer.setSpacing(true);
        buttonContainer.addComponents(submitButton, returnButton);
        
        Panel panel = new Panel("Login");
        panel.setSizeUndefined();
        FormLayout content = new FormLayout();
        
        content.addComponents(usernameField, passwordField, buttonContainer, registerButton);
        content.setMargin(true);
        content.setSpacing(true);
        content.setComponentAlignment(registerButton, Alignment.MIDDLE_CENTER);
        
        panel.setContent(content);
        
        layout = new VerticalLayout();
        layout.addComponent(panel);
        layout.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
        setCompositionRoot(layout);
        
    }
    
    public void validateFields() {
        String username = usernameField.getValue();
        String password = passwordField.getValue();
        boolean isValid = false;
        
        // TODO: validate with database
        if(username.equals("admin") && password.equals("admin")){
            isValid = true;
        }
        
        if(isValid){
            getSession().setAttribute("username", username);
            getUI().getNavigator().navigateTo(MainView.NAME);
        }
        else {
            usernameField.setValue("");
            passwordField.setValue("");
            usernameField.focus();
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        usernameField.focus();
    }
    
}
