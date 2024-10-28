package org.example.ecommerce.dtos;

import org.example.ecommerce.models.Image;
import org.example.ecommerce.models.SubCategory;
import org.example.ecommerce.specifications.ProductSpecs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductUpdateDTO {
    private String name;
    private int price;
    private String description;
    private int stock;
    private Set<Image> images= new HashSet<>();
    private String brandName;
    private int salePercentage;
    private SubCategory subCategory;
    private ProductSpecs productSpecs;
}
