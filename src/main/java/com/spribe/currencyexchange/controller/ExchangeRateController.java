package com.spribe.currencyexchange.controller;

import com.spribe.currencyexchange.model.Currency;
import com.spribe.currencyexchange.model.ExchangeRate;
import com.spribe.currencyexchange.service.CurrencyService;
import com.spribe.currencyexchange.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/currencies/{currencyCode}/rates")
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;
    private final CurrencyService currencyService;

    @Autowired
    public ExchangeRateController(ExchangeRateService exchangeRateService,
                                  CurrencyService currencyService) {
        this.exchangeRateService = exchangeRateService;
        this.currencyService = currencyService;
    }

    @GetMapping
    public ResponseEntity<List<ExchangeRate>> getExchangeRates(@PathVariable String currencyCode) {
        Currency currency = currencyService.getCurrencyByCode(currencyCode);
        List<ExchangeRate> rates = exchangeRateService.getExchangeRates(currency);
        return new ResponseEntity<>(rates, HttpStatus.OK);
    }

    @GetMapping("/latest")
    public ResponseEntity<ExchangeRate> getLatestExchangeRate(@PathVariable String currencyCode) {
        Currency currency = currencyService.getCurrencyByCode(currencyCode);
        List<ExchangeRate> rates = exchangeRateService.getExchangeRates(currency);

        if (rates.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        // Assuming the latest rate is the one with the most recent timestamp
        ExchangeRate latestRate = rates.stream()
                .max((r1, r2) -> r1.getTimestamp().compareTo(r2.getTimestamp()))
                .orElse(null);

        return new ResponseEntity<>(latestRate, HttpStatus.OK);
    }
}
