package com.spribe.currencyexchange.service;

import com.spribe.currencyexchange.model.Currency;
import com.spribe.currencyexchange.model.ExchangeRate;
import com.spribe.currencyexchange.model.external.ExchangeRateApiResponse;
import com.spribe.currencyexchange.repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ExchangeRateFetcherService {

    private final RestTemplate restTemplate;
    private final CurrencyService currencyService;
    private final ExchangeRateRepository exchangeRateRepository;

    @Value("${exchange.api.url}")
    private String exchangeApiUrl;

    @Value("${exchange.api.key}")
    private String exchangeApiKey;

    @Value("${exchange.api.base}")
    private String baseCurrency;

    @Autowired
    public ExchangeRateFetcherService(RestTemplate restTemplate,
                                      CurrencyService currencyService,
                                      ExchangeRateRepository exchangeRateRepository) {
        this.restTemplate = restTemplate;
        this.currencyService = currencyService;
        this.exchangeRateRepository = exchangeRateRepository;
    }

    /**
     * Fetch exchange rates every hour.
     * Cron Expression Breakdown:
     * 0 0 * * * * - At minute 0 past every hour
     */
    @Scheduled(cron = "0 0 * * * *")
    public void fetchExchangeRates() {
        System.out.println("Fetching exchange rates at " + LocalDateTime.now());

        // Construct the API endpoint URL
        String url = String.format("%s/%s/latest/%s", exchangeApiUrl, exchangeApiKey, baseCurrency);

        try {
            // Make the GET request
            ExchangeRateApiResponse response = restTemplate.getForObject(url, ExchangeRateApiResponse.class);

            if (response != null && "success".equalsIgnoreCase(response.getResult())) {
                Map<String, Double> rates = response.getConversionRates();

                // Fetch all currencies from the database
                List<Currency> currencies = currencyService.getAllCurrencies();

                for (Currency currency : currencies) {
                    Double rateValue = rates.get(currency.getCode());

                    if (rateValue != null) {
                        ExchangeRate exchangeRate = ExchangeRate.builder()
                                .currency(currency)
                                .rate(BigDecimal.valueOf(rateValue))
                                .timestamp(LocalDateTime.now())
                                .build();

                        exchangeRateRepository.save(exchangeRate);

                        System.out.println("Updated exchange rate for " + currency.getCode() + ": " + rateValue);
                    } else {
                        System.out.println("No rate found for currency: " + currency.getCode());
                    }
                }
            } else {
                System.err.println("Failed to fetch exchange rates. Response: " + response);
            }

        } catch (HttpClientErrorException.BadRequest e) {
            System.err.println("Bad Request Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        } catch (Exception e) {
            System.err.println("Error fetching exchange rates: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
