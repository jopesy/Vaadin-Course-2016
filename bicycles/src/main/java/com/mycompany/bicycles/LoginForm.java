/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.bicycles;

import com.vaadin.ui.VerticalLayout;
import com.vaadin.navigator.View;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Page;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;



/**
 *
 * @author Joonas
 */
public class LoginForm extends VerticalLayout {
    public LoginForm() {
        Panel panel = new Panel("Login");
        panel.setSizeUndefined();
        addComponent(panel);
        
        FormLayout content = new FormLayout();
        
        TextField usernameField = new TextField("Username");
        PasswordField passwordField = new PasswordField("Password");
        
        Button submitButton = new Button("Submit");
        submitButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        
        content.addComponent(usernameField);
        content.addComponent(passwordField);
        content.addComponent(submitButton);
        content.setMargin(true);
        content.setSpacing(true);
        
        panel.setContent(content);
        setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
    }
}
