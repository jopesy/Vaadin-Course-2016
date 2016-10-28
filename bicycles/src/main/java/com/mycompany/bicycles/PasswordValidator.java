
package com.mycompany.bicycles;

import com.vaadin.data.validator.AbstractValidator;

/**
 *
 *  Validator to validate the registration form password field
 */
final class PasswordValidator extends AbstractValidator<String> {
    public PasswordValidator() {
        super("Passwords must be at least 8 characters long!");
    }

    @Override
    // Password must be at least 8 characters long
    protected boolean isValidValue(String value) {
        return !(value != null && value.length() < 8);
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }
}
