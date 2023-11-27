package ru.verpul.mapper;

import org.mapstruct.Mapper;
import ru.verpul.DTO.NoteCategoryDTO;
import ru.verpul.model.NoteCategory;

@Mapper(componentModel = "spring")
public interface NoteCategoryMapper {

    NoteCategoryDTO noteCategoryToNoteCategoryDTO(NoteCategory noteCategory);

    NoteCategory noteCategoryDTOToNoteCategory(NoteCategoryDTO noteCategoryDTO);
}
