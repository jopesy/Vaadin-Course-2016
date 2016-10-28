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
import com.vaadin.server.Page;
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
public class RegisterView extends CustomComponent implements View{
       public static final String NAME = "register";
    private final VerticalLayout layout;
    private final HorizontalLayout buttonContainer;
    private final TextField firstNameField;
    private final TextField lastNameField;
    private final TextField usernameField;
    private final TextField emailField;
    private final TextField phoneNroField;
    private final PasswordField passwordField;
    private final Button submitButton;
    private final Button returnButton;
    
    public RegisterView() {
        firstNameField = new TextField("First name:");
        firstNameField.setRequired(true);
        firstNameField.setInputPrompt("");
        
        lastNameField = new TextField("Last name:");
        lastNameField.setRequired(true);
        lastNameField.setInputPrompt("");
        
        usernameField = new TextField("Username:");
        usernameField.setRequired(true);
        usernameField.setInputPrompt("Desired username");
        usernameField.setInvalidAllowed(false);
        
        emailField = new TextField("Email address:");
        emailField.setRequired(true);
        emailField.addValidator(new EmailValidator("Please enter a valid email address"));
        emailField.setInvalidAllowed(false);

        phoneNroField = new TextField("Phone number:");
        phoneNroField.setRequired(false);
        
        passwordField = new PasswordField("Password:");
        passwordField.setRequired(true);
        // TODO: Add password validator
        passwordField.setValue("");
        
        submitButton = new Button("Sign up");
        submitButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        submitButton.addClickListener( click -> { 
            validateForm();
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
        
        content.addComponents(firstNameField, lastNameField, phoneNroField, emailField, usernameField, passwordField, buttonContainer);
        content.setMargin(true);
        content.setSpacing(true);
        
        panel.setContent(content);
        panel.setStyleName("register-panel");
        
        layout = new VerticalLayout();
        layout.addComponent(panel);
        layout.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
        setCompositionRoot(layout);
        
    }
    
    private void validateForm() {
        boolean valid = true;
        String firstname = firstNameField.getValue();
        String lastname = lastNameField.getValue();
        String username = usernameField.getValue();
        String email = emailField.getValue();
        String phonenro = phoneNroField.getValue();
        String password = passwordField.getValue();
        
        TextField[] reqFields = {firstNameField, lastNameField, emailField, usernameField};
        
        for (TextField field : reqFields) {
            if(field.isEmpty()) {
                valid = false;
                showErrorMessage("Please fill out all the required fields!");
                field.setRequiredError("Please fill out this field");
                return;
            }
        }
        if(passwordField.isEmpty()){
            valid = false;
            showErrorMessage("Please fill out all the required fields!");
            passwordField.setRequiredError("Please enter your password");
            return;
        }
        
        for(TextField field : reqFields){
            if(!field.isValid()){
                valid = false;
                return;
            }
        }
        if(!passwordField.isValid()){
            valid = false;
            showErrorMessage("That password is not valid!");
            return;
        }
        
        // Validate username, needs to be unique
        boolean usernameAvailable = DatabaseHelper2.checkUsernameAvailability(username);
        
        if(usernameAvailable && valid){
        	DatabaseHelper2.addUser(firstname, lastname, username, email, phonenro, password);
            registrationSuccess();
        }
        else {
            showErrorMessage("That username is already taken!");
        }
    }
    
    private void registrationSuccess() {
        getUI().getNavigator().navigateTo(LoginView.NAME);
        showSuccessMessage();
    }
    
    private void showSuccessMessage() {
        new Notification("Registration completed!",
            "You can now log in using your credentials.",
            Notification.Type.TRAY_NOTIFICATION, true)
            .show(Page.getCurrent());
    }
    
    private void showErrorMessage(String message) {
        new Notification("Error!",
            message,
            Notification.Type.TRAY_NOTIFICATION, true)
            .show(Page.getCurrent());
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        firstNameField.focus();
    }
    
}
