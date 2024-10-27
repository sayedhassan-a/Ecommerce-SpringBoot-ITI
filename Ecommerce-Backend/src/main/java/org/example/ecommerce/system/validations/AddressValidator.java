// src/main/java/org/example/ecommerce/validations/AddressValidator.java
package org.example.ecommerce.system.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.ecommerce.models.Address;

public class AddressValidator implements ConstraintValidator<ValidAddress, Address> {

    @Override
    public void initialize(ValidAddress constraintAnnotation) {
    }

    @Override
    public boolean isValid(Address address, ConstraintValidatorContext context) {
        if (address == null) {
            return false;
        }

        boolean isValid = true;

        if (isNullOrEmpty(address.getAddressOne())) {
            context.buildConstraintViolationWithTemplate("Address line one is required")
                    .addPropertyNode("addressOne").addConstraintViolation();
            isValid = false;
        }

        if (isNullOrEmpty(address.getCity())) {
            context.buildConstraintViolationWithTemplate("City is required")
                    .addPropertyNode("city").addConstraintViolation();
            isValid = false;
        }

        if (isNullOrEmpty(address.getCountry())) {
            context.buildConstraintViolationWithTemplate("Country is required")
                    .addPropertyNode("country").addConstraintViolation();
            isValid = false;
        }

        return isValid;
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}