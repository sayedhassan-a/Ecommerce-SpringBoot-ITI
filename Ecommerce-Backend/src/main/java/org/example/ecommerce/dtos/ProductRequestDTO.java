package org.example.ecommerce.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ecommerce.models.SubCategory;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {
    private String specsId;
    private String name;
    private int price;
    private String description;
    private int stock;
    private List<String> images= new ArrayList<>();
    private String brandName;
    private int salePercentage;
    private SubCategory subCategory;
}
