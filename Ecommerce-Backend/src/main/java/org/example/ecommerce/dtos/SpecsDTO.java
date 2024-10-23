package org.example.ecommerce.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SpecsDTO {
    private String specKey;
    private String inputType;
    private boolean isRequired;
    private List<String> options;
}
