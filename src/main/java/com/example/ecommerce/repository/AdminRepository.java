package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin,Integer> {

    Admin findAdminByEmail(String email);

}
