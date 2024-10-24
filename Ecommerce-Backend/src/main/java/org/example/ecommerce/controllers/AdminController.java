package org.example.ecommerce.controllers;

import jakarta.validation.Valid;
import org.example.ecommerce.dtos.AdminDTO;
import org.example.ecommerce.dtos.adminConverters.AdminDtoToAdminConverter;
import org.example.ecommerce.dtos.adminConverters.AdminToAdminDtoConverter;
import org.example.ecommerce.models.Admin;
import org.example.ecommerce.services.AdminService;
import org.example.ecommerce.system.Result;
import org.example.ecommerce.system.StatusCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admins")
@Validated
public class AdminController {

    @Autowired
    private AdminService adminService;
    
    @Autowired
    private AdminDtoToAdminConverter adminDtoToAdminConverter;

    @Autowired
    private AdminToAdminDtoConverter adminToAdminDtoConverter;

    // Create a new admin
    @PostMapping
    public Result createAdmin(@Valid @RequestBody Admin admin) {
        Admin savedAdmin = adminService.save(admin);
        // Convert saved entity back to DTO
        AdminDTO savedAdminDto = adminToAdminDtoConverter.convert(savedAdmin);
        return new Result(true, StatusCode.SUCCESS, "Admin created successfully", savedAdminDto);
    }

    // Retrieve all admins
    @GetMapping
    public Result findAllAdmins() {
        List<Admin> admins = adminService.findAll();
        // Convert list of entities to DTOs
        List<AdminDTO> adminDtos = admins.stream()
                .map(adminToAdminDtoConverter::convert)
                .collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS, "Admins retrieved successfully", adminDtos);
    }

    // Retrieve admin by ID
    @GetMapping("/{id}")
    public Result findAdminById(@PathVariable Long id) {
        Admin foundAdmin = adminService.findById(id);
        AdminDTO adminDto = adminToAdminDtoConverter.convert(foundAdmin);
        return new Result(true, StatusCode.SUCCESS, "Admin retrieved successfully", adminDto);
    }

    // Retrieve admin by email
    @GetMapping("/email")
    public Result findAdminByEmail(@RequestParam String email) {
        Admin foundAdmin = adminService.findByEmail(email);
        AdminDTO adminDto = adminToAdminDtoConverter.convert(foundAdmin);
        return new Result(true, StatusCode.SUCCESS, "Admin retrieved successfully", adminDto);
    }

    // Update an existing admin
    @PutMapping("/{id}")
    public Result updateAdmin(@PathVariable Long id, @Valid @RequestBody AdminDTO adminDto) {
        // Convert DTO to entity for update
        Admin admin = adminDtoToAdminConverter.convert(adminDto);
        Admin updatedAdmin = adminService.update(id, admin);
        // Convert updated entity back to DTO
        AdminDTO updatedAdminDto = adminToAdminDtoConverter.convert(updatedAdmin);
        return new Result(true, StatusCode.SUCCESS, "Admin updated successfully", updatedAdminDto);
    }

    // Delete an admin
    @DeleteMapping("/{id}")
    public Result deleteAdmin(@PathVariable Long id) {
        adminService.delete(id);
        return new Result(true, StatusCode.SUCCESS, "Admin deleted successfully", null);
    }
}