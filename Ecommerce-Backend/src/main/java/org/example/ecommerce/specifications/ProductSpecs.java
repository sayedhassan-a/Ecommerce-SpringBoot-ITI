package org.example.ecommerce.specifications;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSpecs {
    @Id
    private String id;
    private String productId;
    private List<String> key;
    private List<String> value;

}

