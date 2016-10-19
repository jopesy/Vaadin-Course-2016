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
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * The login view of the application
 * Contains a simple form for logging in
 */
public class RegisterView extends CustomComponent implements View{
    public static final String NAME = "register";
    private final VerticalLayout layout;
    private final HorizontalLayout buttonContainer;
    private final TextField usernameField;
    private final TextField emailField;
    private final PasswordField passwordField;
    private final Button submitButton;
    private final Button returnButton;
    
    public RegisterView() {
        usernameField = new TextField("Username:");
        usernameField.setRequired(true);
        usernameField.setInputPrompt("Enter username");
        usernameField.setInvalidAllowed(false);
        
        emailField = new TextField("Email address:");
        emailField.setRequired(true);
        emailField.setInputPrompt("Enter email address");
        emailField.addValidator(new EmailValidator("Please enter a valid email address"));
        emailField.setInvalidAllowed(false);
        
        passwordField = new PasswordField("Password:");
        passwordField.setRequired(true);
        //passwordField.setInputPrompt("password");
        // TODO: Add password validator
        passwordField.setValue("");
        
        submitButton = new Button("Sign up");
        submitButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        submitButton.addClickListener( click -> {  
            String username = usernameField.getValue();
            //getUI().getNavigator().navigateTo(MainView.NAME);
        });
        
        returnButton = new Button("Cancel");
        returnButton.addClickListener( click -> {
            getUI().getNavigator().navigateTo(MainView.NAME);
        });
        
        buttonContainer = new HorizontalLayout();
        buttonContainer.setSpacing(true);
        buttonContainer.addComponents(submitButton, returnButton);
        
        Panel panel = new Panel("Sign up");
        panel.setSizeUndefined();
        FormLayout content = new FormLayout();
        
        content.addComponents(usernameField, emailField, passwordField, buttonContainer);
        content.setMargin(true);
        content.setSpacing(true);
        
        panel.setContent(content);
        
        layout = new VerticalLayout();
        layout.addComponent(panel);
        layout.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
        setCompositionRoot(layout);
        
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        usernameField.focus();
    }
    
}
