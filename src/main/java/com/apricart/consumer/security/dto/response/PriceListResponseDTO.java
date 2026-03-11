package com.apricart.consumer.security.dto.response;

import com.apricart.consumer.security.enums.PriceBookType;
import com.apricart.consumer.security.enums.RoundingType;
import com.apricart.consumer.security.enums.SalesOrPurchaseType;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class PriceListResponseDTO {
    private Long id;
    private String name;
    private String arabicName;
    private Boolean isIncrease;
    private String description;
    private PriceBookType priceBookType;
    private RoundingType roundingType;
    private SalesOrPurchaseType salesOrPurchaseType;
    private boolean isActive;
    private Long currencyId;
}
