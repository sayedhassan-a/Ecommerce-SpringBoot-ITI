package org.example.ecommerce.services;

import org.example.ecommerce.models.Customer;
import org.example.ecommerce.models.Role;
import org.example.ecommerce.repositories.CustomerRepository;
import org.example.ecommerce.system.exceptions.ObjectNotFoundException;
import org.example.ecommerce.system.exceptions.ValidationException;
import org.example.ecommerce.system.validations.UserValidator;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerService implements UserDetailsService {
    private final CustomerRepository customerRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserValidator customerValidator;

//    private final JwtService jwtService;


    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder, UserValidator customerValidator) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.customerValidator = customerValidator;
//        this.jwtService = jwtService;
    }

    //implement crud operations
    public Customer save(Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer.setRole(Role.ROLE_USER);
        return this.customerRepository.save(customer);
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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void changePassword(Customer user, String oldPassword, String newPassword) {
        List<String> validationErrors = customerValidator.validateChangePassword(user, oldPassword, newPassword);
        if (!validationErrors.isEmpty()) {
            throw new ValidationException(validationErrors); // Custom exception for validation failures
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        this.customerRepository.save(user);
    }

    // Login
//    public String login(String email, String password) {
//        Customer user = this.customerRepository.findByEmail(email)
//                .orElseThrow(() -> new ObjectNotFoundException("Customer", email));
//        // Use passwordEncoder to check the password against the stored hash
//        if (!passwordEncoder.matches(password, user.getPassword())) {
//            throw new ObjectNotFoundException("Customer", email);  // Password doesn't match, user unauthorized
//        } else {
//            return jwtService.generateToken(user);
//        }
//    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.customerRepository.findByEmail(email)
                .map(customer -> new MyUserPrincipal(customer))
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found with email: " + email));
    }
}
