package com.kkuzmin.processing.task;

import com.kkuzmin.processing.field.FieldDifference;
import com.kkuzmin.processing.field.FieldDifferenceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Map;
import java.util.stream.Collectors;

@Mapper
public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    @Mapping(source = "results", target = "results", qualifiedByName = "mapResults")
    TaskDTO toTaskDTO(Task entity);

    @Named("mapResults")
    static Map<String, FieldDifferenceDTO> mapResults(Map<String, FieldDifference> results) {
        if (results == null) {
            return null;
        }

        return results.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> toFieldDifferenceDTO(e.getValue())
                ));
    }

    private static FieldDifferenceDTO toFieldDifferenceDTO(FieldDifference entity) {
        return new FieldDifferenceDTO(
                entity.getFieldName(),
                entity.getPreviousValue(),
                entity.getNewValue(),
                entity.getClassification(),
                entity.getSimilarity()
        );
    }
}
