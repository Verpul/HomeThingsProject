package ru.verpul.model;

import lombok.Getter;
import lombok.Setter;
import ru.verpul.enums.CurrencyType;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "currency")
public class Currency extends BaseEntity{

    @Column(name = "currency_from", nullable = false, length = 3)
    @Enumerated(EnumType.STRING)
    private CurrencyType currencyFrom;

    @Column(name = "from_amount", nullable = false)
    private Double currencyFromAmount;

    @Column(name = "currency_to", nullable = false, length = 3)
    @Enumerated(EnumType.STRING)
    private CurrencyType currencyTo;

    @Column(name = "to_amount", nullable = false)
    private Double currencyToAmount;

    @Column(name = "rate", nullable = false)
    private Double rate;

    @Column(name = "exchange_date", nullable = false)
    private LocalDate exchangeDate;
}
