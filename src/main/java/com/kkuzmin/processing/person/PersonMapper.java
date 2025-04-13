package com.kkuzmin.processing.person;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper(imports = UUID.class)
public interface PersonMapper {

    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);


    PersonDTO toPersonDTO(Person entity);

    Person toPersonEntity(PersonDTO personDTO);


}
