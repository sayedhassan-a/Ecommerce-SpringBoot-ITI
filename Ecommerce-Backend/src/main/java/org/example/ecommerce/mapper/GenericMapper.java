package org.example.ecommerce.mapper;

import org.mapstruct.Mapper;


public interface GenericMapper <Entity, DTO>{
    Entity toEntity(DTO dto);
    DTO toDTO(Entity entity);
}
