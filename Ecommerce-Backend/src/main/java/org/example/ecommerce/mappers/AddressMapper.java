package org.example.ecommerce.mappers;

import org.example.ecommerce.dtos.AddressDto;
import org.example.ecommerce.models.Address;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper  extends GenericMapper<Address, AddressDto> {
    Address toEntity(AddressDto dto);
    AddressDto toDTO(Address entity);
    List<AddressDto> toDTOList(List<Address> addresses);
}
