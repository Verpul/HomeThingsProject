package ru.verpul.DTO;

import lombok.Data;

@Data
public class NoteDTO {
    private Long id;
    private String text;
    private NoteCategoryDTO category;
}
