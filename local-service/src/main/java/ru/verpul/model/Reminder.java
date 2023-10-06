package ru.verpul.model;

import lombok.Getter;
import lombok.Setter;
import ru.verpul.enums.ReminderPeriod;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "reminders")
public class Reminder extends BaseEntity {
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "expire_date")
    private LocalDate expireDate;

    @Column(name = "remind_date")
    private LocalDate remindDate;

    @Column(name = "remind_time")
    private LocalTime remindTime;

    @Column(name = "comment", length = 500)
    private String comment;

    @JoinColumn(name = "parent_id")
    @ManyToOne
    private Reminder parent;

    @JoinColumn(name = "parent_id")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Reminder> children = new ArrayList<>();

    @Column(name = "nesting_depth")
    private Integer nestingDepth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private ReminderCategory category;

    @Column(name = "periodic", nullable = false)
    private Boolean periodic;

    @Enumerated(EnumType.STRING)
    @Column(name = "period")
    private ReminderPeriod period;

    @Column(name = "periodicity")
    private Integer periodicity;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "parent_entity_id")
//    private ParentEntity parentEntity;
//
//    @Column(name = "parent_entity_id", insertable = false, updatable = false)
//    private Long parentId;

}
