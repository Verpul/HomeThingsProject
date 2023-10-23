package ru.verpul.model;

import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import java.time.LocalDate;

@Table(name = "weight_records")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeightRecord extends BaseEntity{

    @Column(name = "weight_record_date", unique = true, nullable = false)
    private LocalDate weightRecordDate;

    @Column(name = "weight_record_value", nullable = false, length = 4)
    private String weightRecordValue;
}
