package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Customer findCustomerByEmail(String email);

    Customer findCustomerByPhone(String phone);

    List<Customer> findCustomersByFirstNameAndLastName(String firstName, String lastName);

}
