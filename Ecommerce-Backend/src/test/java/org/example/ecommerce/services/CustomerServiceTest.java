package org.example.ecommerce.services;

import org.example.ecommerce.models.Customer;
import org.example.ecommerce.repositories.CustomerRepository;
import org.example.ecommerce.system.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    CustomerRepository customerRepository;

    @InjectMocks
    CustomerService customerService;

    List<Customer> customers;

    @BeforeEach
    void setUp() {
        Customer c1 = new Customer();
        c1.setFirstName("John");
        c1.setEmail("john@example.com");

        Customer c2 = new Customer();
        c2.setFirstName("Eric");
        c2.setEmail("eric@example.com");

        Customer c3 = new Customer();
        c3.setFirstName("Tom");
        c3.setEmail("tom@example.com");

        c1.setId(1L);
        c2.setId(2L);
        c3.setId(3L);

        this.customers = new ArrayList<>();
        this.customers.add(c1);
        this.customers.add(c2);
        this.customers.add(c3);
    }


//    @Test
//    void testFindAllSuccess() {
//        // Given
//        given(this.customerRepository.findAll()).willReturn(this.customers);
//
//        // When
//        List<Customer> actualCustomers = this.customerService.findAll();
//
//        // Then
//        assertThat(actualCustomers.size()).isEqualTo(this.customers.size());
//        verify(this.customerRepository, times(1)).findAll();
//    }

    @Test
    void testFindByIdSuccess() {
        // Given
        Customer c = new Customer();
        c.setFirstName("John");
        c.setEmail("john@example.com");
        c.setId(1L);

        given(this.customerRepository.findById(1L)).willReturn(Optional.of(c));

        // When
        Customer returnedCustomer = this.customerService.findById(1L);

        // Then
        assertThat(returnedCustomer.getId()).isEqualTo(c.getId());
        assertThat(returnedCustomer.getFirstName()).isEqualTo(c.getFirstName());
        assertThat(returnedCustomer.getEmail()).isEqualTo(c.getEmail());
        verify(this.customerRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        // Given
        given(this.customerRepository.findById(Mockito.any(Long.class))).willReturn(Optional.empty());

        // When
        Throwable thrown = catchThrowable(() -> {
            Customer returnedCustomer = this.customerService.findById(1L);
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find Customer with Id 1");
        verify(this.customerRepository, times(1)).findById(Mockito.any(Long.class));
    }

    @Test
    void testSaveSuccess() {
        // Given
        Customer newCustomer = new Customer();
        newCustomer.setFirstName("Lily");
        newCustomer.setEmail("lily@example.com");
        newCustomer.setId(4L);

        given(this.customerRepository.save(newCustomer)).willReturn(newCustomer);

        // When
        Customer returnedCustomer = this.customerService.save(newCustomer);

        // Then
        assertThat(returnedCustomer.getFirstName()).isEqualTo(newCustomer.getFirstName());
        assertThat(returnedCustomer.getEmail()).isEqualTo(newCustomer.getEmail());
        verify(this.customerRepository, times(1)).save(newCustomer);
    }

    @Test
    void testUpdateSuccess() {
        // Given
        Customer oldCustomer = new Customer();
        oldCustomer.setId(1L);
        oldCustomer.setFirstName("John");
        oldCustomer.setEmail("john@example.com");

        Customer update = new Customer();
        update.setFirstName("John - updated");
        update.setEmail("john_updated@example.com");

        given(this.customerRepository.findById(1L)).willReturn(Optional.of(oldCustomer));
        given(this.customerRepository.save(oldCustomer)).willReturn(oldCustomer);

        // When
        Customer updatedCustomer = this.customerService.update(1L, update);

        // Then
        assertThat(updatedCustomer.getId()).isEqualTo(1);
        assertThat(updatedCustomer.getFirstName()).isEqualTo(update.getFirstName());
        verify(this.customerRepository, times(1)).findById(1L);
        verify(this.customerRepository, times(1)).save(oldCustomer);
    }

    @Test
    void testUpdateNotFound() {
        // Given
        Customer update = new Customer();
        update.setFirstName("John - updated");
        update.setEmail("john_updated@example.com");

        given(this.customerRepository.findById(1L)).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.customerService.update(1L, update);
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find Customer with Id 1");
        verify(this.customerRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteSuccess() {
        // Given
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("John");
        customer.setEmail("john@example.com");

        given(this.customerRepository.findById(1L)).willReturn(Optional.of(customer));

        // When
        doNothing().when(this.customerRepository).delete(customer);
        this.customerService.delete(1L);

        // Then
        verify(this.customerRepository, times(1)).delete(customer);
    }

    @Test
    void testDeleteNotFound() {
        // Given
        given(this.customerRepository.findById(1L)).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.customerService.delete(1L);
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find Customer with Id 1");
        verify(this.customerRepository, times(1)).findById(1L);
    }
}