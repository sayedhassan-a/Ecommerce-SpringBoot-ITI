package org.example.ecommerce.services;

import org.example.ecommerce.models.Admin;
import org.example.ecommerce.models.Role;
import org.example.ecommerce.repositories.AdminRepository;
import org.example.ecommerce.system.exceptions.ObjectNotFoundException;
import org.example.ecommerce.system.validations.UserValidator;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService implements UserDetailsService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserValidator customerValidator;

    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder, UserValidator customerValidator) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.customerValidator = customerValidator;
    }

    // Save Admin
    public Admin save(Admin admin) {
        // Validate Admin password
        List<String> errors = customerValidator.validateCustomer(admin);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Invalid admin data: " + String.join(", ", errors));
        }

        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setRole(Role.ROLE_ADMIN);
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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.adminRepository.findByEmail(email)
                .map(admin -> new MyUserPrincipal(admin))
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found with email: " + email));
    }



}