/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.bicycles;

import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
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
        usernameField.setInvalidAllowed(false);
        
        passwordField = new PasswordField("Password:");
        passwordField.setRequired(true);
        passwordField.setValue("");
        
        submitButton = new Button("Login");
        submitButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        submitButton.addClickListener( click -> {
            validateForm();
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
        panel.setStyleName("login-panel");
        
        layout = new VerticalLayout();
        layout.addComponent(panel);
        layout.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
        setCompositionRoot(layout);
        
    }
    
    public void validateForm() {
        String username = usernameField.getValue();
        String password = passwordField.getValue();
        boolean isValid = false;
        
        if(usernameField.isEmpty()){
            usernameField.setRequiredError("Please enter your username");
            showErrorMessage("Username field is empty!");
            return;
        }
        if(passwordField.isEmpty()){
            passwordField.setRequiredError("Please enter your password");
            showErrorMessage("Password field is empty!");
            return;
        }
        if(!usernameField.isValid() || !passwordField.isValid()) {
            return;
        }
        
        // Check if user exists in database
        int userid = DatabaseHelper.getUserId(username, password);
        if(userid != -1) {
            isValid = true;
        }

        if(isValid){
            VaadinSession.getCurrent().getSession().setAttribute("username", username);
            VaadinSession.getCurrent().getSession().setAttribute("userid", userid);
            getUI().getNavigator().navigateTo(MainView.NAME);

            new Notification("Logged in!",
                "Hello, "+username+"!",
                Notification.Type.TRAY_NOTIFICATION, true)
                .show(Page.getCurrent());
        }
        else {
            usernameField.setValue("");
            passwordField.setValue("");
            usernameField.focus();
            usernameField.setRequiredError(null);
            passwordField.setRequiredError(null);
            showErrorMessage("Username and password do not match!");
        }
    }
    
    public void showErrorMessage(String message) {
        new Notification(message,
            "Please try again!",
            Notification.Type.TRAY_NOTIFICATION, true)
            .show(Page.getCurrent());
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        usernameField.focus();
    }
    
}
