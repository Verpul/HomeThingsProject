package ru.verpul.mapper;

import org.mapstruct.Mapper;
import ru.verpul.DTO.NoteDTO;
import ru.verpul.model.Note;

@Mapper(componentModel = "spring", uses = NoteCategoryMapper.class)
public interface NoteMapper {

    Note noteDTOToNote(NoteDTO noteDTO);

    NoteDTO noteToNoteDTO(Note note);

}
