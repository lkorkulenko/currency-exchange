package com.spribe.currencyexchange.service;

import com.spribe.currencyexchange.model.Currency;
import com.spribe.currencyexchange.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurrencyService {

    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public List<Currency> getAllCurrencies() {
        return currencyRepository.findAll();
    }

    public Currency addCurrency(Currency currency) {
        Optional<Currency> existing = currencyRepository.findByCode(currency.getCode());
        if (existing.isPresent()) {
            throw new RuntimeException("Currency with code " + currency.getCode() + " already exists.");
        }
        return currencyRepository.save(currency);
    }

    public Currency getCurrencyByCode(String code) {
        return currencyRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Currency with code " + code + " not found."));
    }
}
