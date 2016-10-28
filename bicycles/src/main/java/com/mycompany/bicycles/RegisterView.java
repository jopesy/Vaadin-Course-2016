/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.bicycles;

import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.data.validator.StringLengthValidator;
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

        phoneNroField = new TextField("Phone number:");
        phoneNroField.setRequired(false);
        phoneNroField.addValidator(new RegexpValidator("\\d+", "Phone number must be digits only!"));
        phoneNroField.addValidator(new StringLengthValidator("Phone number length must not be longer than 12 digits!", 10, 12, true));
        phoneNroField.setValue(null);
        phoneNroField.setNullRepresentation("");
        
        passwordField = new PasswordField("Password:");
        passwordField.setRequired(true);
        passwordField.addValidator(new PasswordValidator());
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
                showErrorMessage("Please fill out all the required fields!");
                return;
            }
        }
        if(passwordField.isEmpty()){
            showErrorMessage("Please fill out all the required fields!");
            return;
        }        
        if(!emailField.isValid()) {
            showErrorMessage("Please enter a valid email address!");
            return;
        }        
        for(TextField field : reqFields){
            if(!field.isValid()){
                return;
            }
        }        
        if(!passwordField.isValid()){
            showErrorMessage("That password is not valid!");
            return;
        }
        
        // Username needs to be unique
        boolean usernameAvailable = DatabaseHelper.checkUsernameAvailability(username);
        
        if(usernameAvailable && valid){
            DatabaseHelper.addUser(firstname, lastname, username, email, phonenro, password);
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
        new Notification("Thank you for registering!",
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
