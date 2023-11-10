package ru.verpul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.verpul.model.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
}
