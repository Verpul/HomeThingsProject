package ru.verpul.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "note_category")
public class NoteCategory extends BaseEntity {

    @Column(name = "title", nullable = false, unique = true, length = 200)
    private String title;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private List<Note> notes;
}
