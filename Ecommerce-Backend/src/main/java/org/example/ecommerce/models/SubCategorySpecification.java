package org.example.ecommerce.models;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.annotation.Collation;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "subCategorySpecification")
@Getter
@Setter
@NoArgsConstructor

public class SubCategorySpecification {
    @Id
    private String id;

    private String name;

    private List<Specs> specs;

}

