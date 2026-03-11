package com.apricart.consumer.security.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class CurrencyResponseDTO {
    private Long id;
    private String currencyCode;
    private String currencySymbol;
    private String currencyPrecision;
    private String currencyFormat;
    private boolean isActive;
}
