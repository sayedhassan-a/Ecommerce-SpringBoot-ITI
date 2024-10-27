package org.example.ecommerce.dtos;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ecommerce.models.SubCategory;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {
    private String specsId;
    private String name;
    private int price;
    private String description;
    private int stock;
    private List<String> images;
    private String brandName;
    private SubCategory subCategory;
}
