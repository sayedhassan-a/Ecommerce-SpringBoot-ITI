package org.example.ecommerce.services;

import org.example.ecommerce.models.Admin;
import org.example.ecommerce.repositories.AdminRepository;
import org.example.ecommerce.system.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    AdminRepository adminRepository;

    @InjectMocks
    AdminService adminService;

    List<Admin> admins;

    @BeforeEach
    void setUp() {
        Admin a1 = new Admin();
        a1.setFirstName("Alice");
        a1.setEmail("alice@example.com");

        Admin a2 = new Admin();
        a2.setFirstName("Bob");
        a2.setEmail("bob@example.com");

        Admin a3 = new Admin();
        a3.setFirstName("Charlie");
        a3.setEmail("charlie@example.com");

        a1.setId(1L);
        a2.setId(2L);
        a3.setId(3L);

        this.admins = new ArrayList<>();
        this.admins.add(a1);
        this.admins.add(a2);
        this.admins.add(a3);
    }

    @Test
    void testFindAllSuccess() {
        // Given
        given(this.adminRepository.findAll()).willReturn(this.admins);

        // When
        List<Admin> actualAdmins = this.adminService.findAll();

        // Then
        assertThat(actualAdmins.size()).isEqualTo(this.admins.size());
        verify(this.adminRepository, times(1)).findAll();
    }

    @Test
    void testFindByIdSuccess() {
        // Given
        Admin a = new Admin();
        a.setFirstName("Alice");
        a.setEmail("alice@example.com");
        a.setId(1L);

        given(this.adminRepository.findById(1L)).willReturn(Optional.of(a));

        // When
        Admin returnedAdmin = this.adminService.findById(1L);

        // Then
        assertThat(returnedAdmin.getId()).isEqualTo(a.getId());
        assertThat(returnedAdmin.getFirstName()).isEqualTo(a.getFirstName());
        assertThat(returnedAdmin.getEmail()).isEqualTo(a.getEmail());
        verify(this.adminRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        // Given
        given(this.adminRepository.findById(Mockito.any(Long.class))).willReturn(Optional.empty());

        // When
        Throwable thrown = catchThrowable(() -> {
            Admin returnedAdmin = this.adminService.findById(1L);
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find Admin with Id 1");
        verify(this.adminRepository, times(1)).findById(Mockito.any(Long.class));
    }

    @Test
    void testSaveSuccess() {
        // Given
        Admin newAdmin = new Admin();
        newAdmin.setFirstName("David");
        newAdmin.setEmail("david@example.com");
        newAdmin.setId(4L);

        given(this.adminRepository.save(newAdmin)).willReturn(newAdmin);

        // When
        Admin returnedAdmin = this.adminService.save(newAdmin);

        // Then
        assertThat(returnedAdmin.getFirstName()).isEqualTo(newAdmin.getFirstName());
        assertThat(returnedAdmin.getEmail()).isEqualTo(newAdmin.getEmail());
        verify(this.adminRepository, times(1)).save(newAdmin);
    }

    @Test
    void testUpdateSuccess() {
        // Given
        Admin oldAdmin = new Admin();
        oldAdmin.setId(1L);
        oldAdmin.setFirstName("Alice");
        oldAdmin.setEmail("alice@example.com");

        Admin update = new Admin();
        update.setFirstName("Alice - updated");
        update.setEmail("alice_updated@example.com");

        given(this.adminRepository.findById(1L)).willReturn(Optional.of(oldAdmin));
        given(this.adminRepository.save(oldAdmin)).willReturn(oldAdmin);

        // When
        Admin updatedAdmin = this.adminService.update(1L, update);

        // Then
        assertThat(updatedAdmin.getId()).isEqualTo(1);
        assertThat(updatedAdmin.getFirstName()).isEqualTo(update.getFirstName());
        verify(this.adminRepository, times(1)).findById(1L);
        verify(this.adminRepository, times(1)).save(oldAdmin);
    }

    @Test
    void testUpdateNotFound() {
        // Given
        Admin update = new Admin();
        update.setFirstName("Alice - updated");
        update.setEmail("alice_updated@example.com");

        given(this.adminRepository.findById(1L)).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.adminService.update(1L, update);
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find Admin with Id 1");
        verify(this.adminRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteSuccess() {
        // Given
        Admin admin = new Admin();
        admin.setId(1L);
        admin.setFirstName("Alice");
        admin.setEmail("alice@example.com");

        given(this.adminRepository.findById(1L)).willReturn(Optional.of(admin));

        // When
        doNothing().when(this.adminRepository).delete(admin);
        this.adminService.delete(1L);

        // Then
        verify(this.adminRepository, times(1)).delete(admin);
    }

    @Test
    void testDeleteNotFound() {
        // Given
        given(this.adminRepository.findById(1L)).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.adminService.delete(1L);
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find Admin with Id 1");
        verify(this.adminRepository, times(1)).findById(1L);
    }
}