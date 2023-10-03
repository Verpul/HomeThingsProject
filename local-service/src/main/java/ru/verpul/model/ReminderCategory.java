package ru.verpul.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "reminder_category")
public class ReminderCategory extends BaseEntity{

    @Column(name = "title", length = 100)
    private String title;

}
