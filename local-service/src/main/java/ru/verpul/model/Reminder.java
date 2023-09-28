package ru.verpul.model;

import lombok.Getter;
import lombok.Setter;
import ru.verpul.enums.Period;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "reminders")
public class Reminder extends BaseEntity{
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "expire_date")
    private LocalDate expireDate;

//    @Column(name = "expire_time")
//    private LocalTime reminderTime;

//    @Column(name = "repeatable", nullable = false)
//    private Boolean repeatable;
//
//    @Column(name = "save_history", nullable = false)
//    private Boolean saveHistory;

//    @Enumerated(EnumType.STRING)
//    @Column(name = "period")
//    private Period period;
//
//    @Column(name = "period_length")
//    private Integer periodLength;
//
//    @Column(name = "price", length = 10)
//    private String price;

//    @OneToMany(mappedBy = "reminder", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<ReminderDate> reminderDates = new ArrayList<>();
}
