package org.example.ecommerce.dtos.adminConverters;

import org.example.ecommerce.dtos.AdminDTO;
import org.example.ecommerce.models.Admin;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AdminToAdminDtoConverter implements Converter<Admin, AdminDTO> {

    @Override
    public AdminDTO convert(Admin admin) {
        return new AdminDTO(
            admin.getId(),
            admin.getFirstName(),
            admin.getMiddleName(),
            admin.getLastName(),
            admin.getEmail(),
            admin.getRole()
        );
    }
}