package org.example.ecommerce.models;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "admin")
@Getter
@Setter
@Valid
@NoArgsConstructor
public class Admin extends User{

}
