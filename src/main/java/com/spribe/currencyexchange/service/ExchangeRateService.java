package com.spribe.currencyexchange.service;

import com.spribe.currencyexchange.model.Currency;
import com.spribe.currencyexchange.model.ExchangeRate;
import com.spribe.currencyexchange.repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;

    @Autowired
    public ExchangeRateService(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }

    public List<ExchangeRate> getExchangeRates(Currency currency) {
        return exchangeRateRepository.findByCurrency(currency);
    }

    public ExchangeRate saveExchangeRate(ExchangeRate exchangeRate) {
        return exchangeRateRepository.save(exchangeRate);
    }
}
