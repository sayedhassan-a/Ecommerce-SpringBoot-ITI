package org.example.ecommerce.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class SpecsDTO {
    private String specKey;
    private String inputType;
    private boolean isRequired;
    private Set<String> options;
}
