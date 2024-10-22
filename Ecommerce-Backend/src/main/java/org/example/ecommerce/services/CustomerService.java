package org.example.ecommerce.services;

import org.example.ecommerce.models.Customer;
import org.example.ecommerce.repositories.CustomerRepository;
import org.example.ecommerce.system.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    //implement crud operations
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    public void delete(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Customer", id));
        customerRepository.delete(customer);
    }

    public Customer findById(Long id) {
        //if not found throw ObjectNotFoundException
        return customerRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Customer", id));
    }

    public Customer findUserByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException(email));
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Customer update(Long id, Customer newCustomer) {
        Customer foundCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Customer", id));
        foundCustomer.setFirstName(newCustomer.getFirstName());
        foundCustomer.setMiddleName(newCustomer.getMiddleName());
        foundCustomer.setLastName(newCustomer.getLastName());
        foundCustomer.setEmail(newCustomer.getEmail());
        foundCustomer.setPhone(newCustomer.getPhone());
        foundCustomer.setAddress(newCustomer.getAddress());
        foundCustomer.setDateOfBirth(newCustomer.getDateOfBirth());
        foundCustomer.setActive(newCustomer.isActive());

        return customerRepository.save(foundCustomer);
    }
}
