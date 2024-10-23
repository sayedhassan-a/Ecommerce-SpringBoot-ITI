package org.example.ecommerce.mappers;


public interface GenericMapper <Entity, DTO>{
    Entity toEntity(DTO dto);
    DTO toDTO(Entity entity);
}
