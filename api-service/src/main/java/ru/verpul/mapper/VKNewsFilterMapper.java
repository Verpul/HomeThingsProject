package ru.verpul.mapper;

import org.mapstruct.Mapper;
import ru.verpul.DTO.VKNewsFilterDTO;
import ru.verpul.model.VKNewsFilter;

@Mapper(componentModel = "spring")
public interface VKNewsFilterMapper {
    VKNewsFilter VKNewsFilterDTOToVKNewsFilter(VKNewsFilterDTO vkNewsFilterDTO);

    VKNewsFilterDTO VKNewsFilterToVKNewsFilterDTO(VKNewsFilter vkNewsFilter);
}
