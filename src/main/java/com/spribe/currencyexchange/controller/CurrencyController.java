package com.spribe.currencyexchange.controller;

import com.spribe.currencyexchange.model.Currency;
import com.spribe.currencyexchange.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;

    @Autowired
    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping
    public ResponseEntity<List<Currency>> getAllCurrencies() {
        List<Currency> currencies = currencyService.getAllCurrencies();
        return new ResponseEntity<>(currencies, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Currency> addCurrency(@RequestBody Currency currency) {
        Currency savedCurrency = currencyService.addCurrency(currency);
        return new ResponseEntity<>(savedCurrency, HttpStatus.CREATED);
    }
}
