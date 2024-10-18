package com.example.ecommerce.service;

import com.example.ecommerce.entity.Customer;
import com.example.ecommerce.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Long countAll() {
        return customerRepository.count();
    }
    public Page<Customer> findAll(int page, int size) {
        return customerRepository.findAll(PageRequest.of(page, size));
    }
    public Boolean checkEmailAvailability(String email) {
        return findByEmail(email) == null;
    }
    public Customer findByEmail(String email) {
        return customerRepository.findCustomerByEmail(email);
    }
    public Customer findById(Integer id) {
        return customerRepository.findById(id).orElse(null);
    }

    public Customer update(Customer customer) {
        try{
            return customerRepository.save(customer);
        }
        catch(Exception e){
            return null;
        }

    }
}
