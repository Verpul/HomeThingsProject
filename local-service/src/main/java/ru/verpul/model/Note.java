package ru.verpul.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "notes")
public class Note extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private NoteCategory category;

    @Column(name = "text")
    private String text;
}
