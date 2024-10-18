package com.example.ecommerce.service;

import com.example.ecommerce.dto.AdminDTO;
import com.example.ecommerce.dto.AuthRequest;
import com.example.ecommerce.dto.RegistrationRequest;
import com.example.ecommerce.entity.Admin;
import com.example.ecommerce.repository.AdminRepository;
import com.example.ecommerce.security.JwtService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AdminAuthService {
    private final AdminRepository adminRepository;
    public AdminAuthService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }
    public AdminDTO register(RegistrationRequest request) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<RegistrationRequest>> violations =
                validator.validate(request);
        ///Handle Exception
        if(!violations.isEmpty()){
            violations.forEach(violation -> System.out.println(violation.getMessage()));
            return null;
        }
        Admin admin = new Admin();
        admin.setEmail(request.getEmail());
        admin.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        Admin saved = adminRepository.save(admin);
        if(saved == null){
            return null;
        }

        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setId(saved.getId());
        adminDTO.setEmail(admin.getEmail());

        return adminDTO;
    }
    public String loginWithToken(AuthRequest request) {
        try {
            Admin admin = adminRepository.findAdminByEmail(request.getEmail());

            if (BCrypt.checkpw(request.getPassword(), admin.getPassword())) {
                String token = JwtService.generate(admin.getId(),
                        "ADMIN");
                System.out.println(token);
                return token;
            } else
                return null;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean login(AuthRequest request) {
        try {

            Admin admin = adminRepository.findAdminByEmail(request.getEmail());
            return BCrypt.checkpw(request.getPassword(), admin.getPassword());

        } catch (Exception e) {
            return false;
        }

    }
}
