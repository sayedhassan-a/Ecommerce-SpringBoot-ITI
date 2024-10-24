package org.example.ecommerce.models;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
@Getter
@Setter
@NoArgsConstructor
public class SubCategorySpecification {
    @Id
    private String id;

    private String name;

    private List<Specs> specs;

}

