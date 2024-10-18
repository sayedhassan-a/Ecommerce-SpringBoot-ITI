package com.example.ecommerce.service;

import com.example.ecommerce.entity.Admin;
import com.example.ecommerce.repository.AdminRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }


    public Admin findById(Integer id) {
        return adminRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public Admin findByEmail(String email) {
        return adminRepository.findAdminByEmail(email);
    }

    public Admin update(Admin admin) {
        try{
            return adminRepository.save(admin);
        }
        catch(Exception e){
            return null;
        }

    }
}
