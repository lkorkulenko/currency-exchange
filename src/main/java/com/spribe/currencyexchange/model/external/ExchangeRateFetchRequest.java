package com.spribe.currencyexchange.model.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateFetchRequest {
    private String base;
    private String symbols;
}
