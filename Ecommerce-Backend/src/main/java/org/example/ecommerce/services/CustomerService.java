package org.example.ecommerce.services;

import org.example.ecommerce.models.Address;
import org.example.ecommerce.models.Customer;
import org.example.ecommerce.models.Role;
import org.example.ecommerce.repositories.CustomerRepository;
import org.example.ecommerce.system.exceptions.ObjectNotFoundException;
import org.example.ecommerce.system.exceptions.ValidationException;
import org.example.ecommerce.system.validations.UserValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final UserValidator userValidator;

    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder, UserValidator userValidator) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.userValidator = userValidator;
    }

    //implement crud operations
    public Customer save(Customer customer) {
        System.out.println("inside save: " + customer);

        List<String> errors = userValidator.validateCustomer(customer);
        if (!errors.isEmpty()) {
            System.out.println("Errors: " + errors);
            throw new ValidationException(errors);
        }

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer.setRole(Role.ROLE_USER);

        return this.customerRepository.save(customer);
    }

    public Customer updateByEmail(String email, Customer customer) {
        // Find the customer in the repository
        Customer foundCustomer = this.customerRepository.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("customer", email));

        System.out.println("inside updateByEmail: " + customer);

        List<String> validationErrors = userValidator.checkName(customer); // Call to validateCustomerInput

        if (!validationErrors.isEmpty()) {
            throw new ValidationException(validationErrors);
        }

        // Update fields only if they are non-null
        if (customer.getFirstName() != null) foundCustomer.setFirstName(customer.getFirstName());
        if (customer.getLastName() != null) foundCustomer.setLastName(customer.getLastName());

        Address address = customer.getAddress();
        Address foundAddress = foundCustomer.getAddress();
        if (address != null && foundAddress != null) {
            if (address.getAddressOne() != null) foundCustomer.getAddress().setAddressOne(address.getAddressOne());
            if (address.getCity() != null) foundCustomer.getAddress().setCity(address.getCity());
            if (address.getCountry() != null) foundCustomer.getAddress().setCountry(address.getCountry());
            if (address.getZipCode() != null) foundCustomer.getAddress().setZipCode(address.getZipCode());
        }
        foundCustomer.setAddress(address);

        if (customer.getDateOfBirth() != null) foundCustomer.setDateOfBirth(customer.getDateOfBirth());
        if (customer.getPhone() != null) foundCustomer.setPhone(customer.getPhone());

        // Handle email update as needed, possibly with additional verification
        if (customer.getEmail() != null) foundCustomer.setEmail(customer.getEmail());

        // Save and return the updated customer
        return customerRepository.save(foundCustomer);
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

    public Page<Customer> findAllByPage(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return customerRepository.findAll(pageRequest);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void changePassword(String email, String newPassword) {

        Customer user = customerRepository.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("Customer", email));

        // Validate the new password
        List<String> validationErrors = userValidator.validateChangePassword(newPassword);
        if (!validationErrors.isEmpty()) {
            System.out.println("Errors: " + validationErrors.get(0));
            throw new ValidationException(validationErrors);
        }

        // Hash and save the new password
        user.setPassword(passwordEncoder.encode(newPassword));
        customerRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found with email: " + email));

        if (customer.getProvider() != null) {
            throw new UsernameNotFoundException("Customer registered with OAuth provider: " + customer.getProvider());
        }

        return new MyUserPrincipal(customer);
    }

    public boolean existsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }

    public Customer update(Long id, Customer customer) {
        // Find the customer in the repository
        Customer foundCustomer = this.customerRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("customer", id));

        List<String> validationErrors = userValidator.validateCustomer(customer); // Call to validateCustomerInput

        if (!validationErrors.isEmpty()) {
            throw new ValidationException(validationErrors); // Handle validation failures
        }

        // Update fields only if they are non-null
        if (customer.getFirstName() != null) foundCustomer.setFirstName(customer.getFirstName());
        if (customer.getLastName() != null) foundCustomer.setLastName(customer.getLastName());
        if (customer.getAddress().getAddressOne() != null) foundCustomer.getAddress().setAddressOne(customer.getAddress().getAddressOne());
        if (customer.getAddress().getCity() != null) foundCustomer.getAddress().setCity(customer.getAddress().getCity());
        if (customer.getAddress().getCountry() != null) foundCustomer.getAddress().setCountry(customer.getAddress().getCountry());
        if (customer.getAddress().getZipCode() != null) foundCustomer.getAddress().setZipCode(customer.getAddress().getZipCode());
        if (customer.getDateOfBirth() != null) foundCustomer.setDateOfBirth(customer.getDateOfBirth());
        if (customer.getPhone() != null) foundCustomer.setPhone(customer.getPhone());

        // Handle email update as needed, possibly with additional verification
        if (customer.getEmail() != null) foundCustomer.setEmail(customer.getEmail());

        // Save and return the updated customer
        return customerRepository.save(foundCustomer);
    }

    public List<String> isCustomerInfoComplete(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("Customer", email));

        System.out.println("inside isCustomerInfoComplete: " + customer);

        List<String> validationErrors = userValidator.vaildateCheckout(customer);

        if (!validationErrors.isEmpty()) {
            System.out.println("Errors: " + validationErrors.get(0));
        }

        return validationErrors;
    }

    public Page<Customer> searchByEmail(String email, int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return customerRepository.findByEmailContaining(email, pageRequest);
    }
}
