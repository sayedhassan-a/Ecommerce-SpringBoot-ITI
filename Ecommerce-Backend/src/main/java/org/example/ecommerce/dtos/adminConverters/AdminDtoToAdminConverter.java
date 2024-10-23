package org.example.ecommerce.dtos.adminConverters;

import org.example.ecommerce.dtos.AdminDTO;
import org.example.ecommerce.models.Admin;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AdminDtoToAdminConverter implements Converter<AdminDTO, Admin> {

    @Override
    public Admin convert(AdminDTO dto) {
        Admin admin = new Admin();
        admin.setId(dto.id());
        admin.setFirstName(dto.firstName());
        admin.setMiddleName(dto.middleName());
        admin.setLastName(dto.lastName());
        admin.setEmail(dto.email());
        admin.setRole(dto.role());
        return admin;
    }
}