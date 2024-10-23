package org.example.ecommerce.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

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
