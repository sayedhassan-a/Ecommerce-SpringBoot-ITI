package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AdminRepository extends JpaRepository<Admin,Integer> {

    Admin findAdminByEmail(String email);

}
