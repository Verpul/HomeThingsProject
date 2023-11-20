package ru.verpul.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "vk_news_date")
public class VKNewsDate extends BaseEntity{

    @Column(name = "begin_date", nullable = false)
    private LocalDate beginDate;

    @Column(name = "begin_time", nullable = false)
    private LocalTime beginTime;
}
