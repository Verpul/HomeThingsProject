package ru.verpul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.verpul.model.Currency;

import java.util.List;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    @Query("SELECT c FROM Currency c ORDER BY c.exchangeDate ASC")
    List<Currency> findAllAndSortByDate();
}
