package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Customer findCustomerByEmail(String email);

    Customer findCustomerByPhone(String phone);

    List<Customer> findCustomersByFirstNameAndLastName(String firstName, String lastName);

}
