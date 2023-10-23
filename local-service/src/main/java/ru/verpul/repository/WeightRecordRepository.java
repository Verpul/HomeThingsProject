package ru.verpul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.verpul.model.WeightRecord;

import java.time.LocalDate;
import java.util.Optional;

public interface WeightRecordRepository extends JpaRepository<WeightRecord, Long> {

    @Query("SELECT w.id FROM WeightRecord as w WHERE w.weightRecordDate = :recordDate")
    Optional<Long> findByRecordDate(LocalDate recordDate);

}
