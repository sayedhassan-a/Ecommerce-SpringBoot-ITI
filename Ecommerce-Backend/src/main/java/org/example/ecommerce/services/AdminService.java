package org.example.ecommerce.services;

import org.example.ecommerce.models.Admin;
import org.example.ecommerce.repositories.AdminRepository;
import org.example.ecommerce.system.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    // Save Admin
    public Admin save(Admin admin) {
        return adminRepository.save(admin);
    }

    // Find Admin by ID
    public Admin findById(Long id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Admin", id));
    }

    // Find Admin by Email
    public Admin findByEmail(String email) {
        return adminRepository.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException(email));
    }

    // Find all Admins
    public List<Admin> findAll() {
        return adminRepository.findAll();
    }

    // Update Admin details
    public Admin update(Long id, Admin newAdmin) {
        Admin foundAdmin = adminRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Admin", id));
        
        foundAdmin.setFirstName(newAdmin.getFirstName());
        foundAdmin.setMiddleName(newAdmin.getMiddleName());
        foundAdmin.setLastName(newAdmin.getLastName());
        foundAdmin.setEmail(newAdmin.getEmail());
        foundAdmin.setPassword(newAdmin.getPassword());
        
        return adminRepository.save(foundAdmin);
    }

    // Delete Admin by ID
    public void delete(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Admin", id));
        adminRepository.delete(admin);
    }
}