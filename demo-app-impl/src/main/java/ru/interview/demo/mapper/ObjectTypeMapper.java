package ru.interview.demo.mapper;

import org.mapstruct.Mapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.interview.demo.dto.ObjectData;
import ru.interview.demo.dto.ObjectDataRequest;
import ru.interview.demo.entity.ObjectEntity;

@Mapper(componentModel = "spring")
public interface ObjectTypeMapper {

    ObjectData mapEntityToDto(ObjectEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ObjectEntity mapRequestToEntity(ObjectDataRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void mapRequestToEntity(ObjectDataRequest source, @MappingTarget ObjectEntity target);

    ObjectEntity mapDtoToEntity(ObjectData dto);

}
