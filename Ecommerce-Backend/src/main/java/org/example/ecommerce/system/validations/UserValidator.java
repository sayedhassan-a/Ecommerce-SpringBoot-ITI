package org.example.ecommerce.system.validations;

import org.example.ecommerce.models.Address;
import org.example.ecommerce.models.Customer;
import org.example.ecommerce.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserValidator {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<String> validateCustomer(User user) {
        List<String> errors = new ArrayList<>();

        errors.addAll(checkName(user));


        if (!isValidPassword(user.getPassword())) {
            errors.add("Password must be between 8 and 20 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character.");
        }

        return errors;
    }

    public List<String> checkName(User user) {
        List<String> errors = new ArrayList<>();
        if (user.getFirstName() == null || user.getFirstName().trim().isEmpty()) {
            errors.add("First name cannot be empty.");
        }
        return errors;
    }

    private List<String> validateAddress(Address address) {
        List<String> errors = new ArrayList<>();
        if(address.getAddressOne() == null || address.getAddressOne().trim().isEmpty()) {
            errors.add("Address line 1 cannot be empty.");
        }
        if(address.getCity() == null || address.getCity().trim().isEmpty()) {
            errors.add("City cannot be empty.");
        }
        if(address.getCountry() == null || address.getCountry().trim().isEmpty()) {
            errors.add("Country cannot be empty.");
        }
        if(address.getZipCode() == null || address.getZipCode().trim().isEmpty()) {
            errors.add("Zip code cannot be empty.");
        }
        // Add more address validation logic if needed
        return errors;
    }

    public List<String> validateChangePassword(User user, String oldPassword, String newPassword) {
        List<String> errors = new ArrayList<>();

        if (isNullOrEmpty(oldPassword) || isNullOrEmpty(newPassword)) {
            errors.add("Old password and new password cannot be empty.");
            return errors;
        }

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            errors.add("Old password is incorrect.");
        }

        if (!isValidPassword(newPassword)) {
            errors.add("New password must be between 8 and 20 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character.");
        }

        return errors;
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 8 || password.length() > 20) {
            return false;
        }

        boolean hasUppercase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLowercase = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecialChar = password.chars().anyMatch(ch -> !Character.isLetterOrDigit(ch));

        return hasUppercase && hasLowercase && hasDigit && hasSpecialChar;
    }
}