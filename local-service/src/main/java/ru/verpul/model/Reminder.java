package ru.verpul.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "reminders")
public class Reminder extends BaseEntity{
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "expire_date")
    private LocalDate expireDate;

    @Column(name = "remind_date")
    private LocalDate remindDate;

    @Column(name = "remind_time")
    private LocalTime remindTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private ReminderCategory category;

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
