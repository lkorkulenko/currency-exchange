package com.spribe.currencyexchange.repository;

import com.spribe.currencyexchange.model.Currency;
import com.spribe.currencyexchange.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    List<ExchangeRate> findByCurrency(Currency currency);
}
