package org.example.ecommerce.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class Specs {
    @Field("spec_key")
    private String specKey;

    @Field("input_type")
    private String inputType;

    @Field("is_required")
    private boolean isRequired;

    private Set<String> options;
}
