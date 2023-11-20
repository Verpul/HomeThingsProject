package ru.verpul.mapper;

import org.mapstruct.Mapper;
import ru.verpul.DTO.VKNewsDateDTO;
import ru.verpul.model.VKNewsDate;

@Mapper(componentModel = "spring")
public interface VKNewsDateMapper {

    VKNewsDate vkNewsDateDTOToVKNewsDate(VKNewsDateDTO vkNewsDateDTO);

    VKNewsDateDTO vkNewsDateToVkNewsDateDTO(VKNewsDate vkNewsDate);
}
