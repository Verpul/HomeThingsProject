package ru.verpul.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class VKNewsDateDTO {
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate beginDate;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime beginTime;
}
