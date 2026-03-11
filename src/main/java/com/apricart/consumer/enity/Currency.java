package com.apricart.consumer.enity;

import com.apricart.consumer.security.dto.request.CurrencyRequestDTO;
import com.apricart.consumer.security.dto.response.CityResponseDTO;
import com.apricart.consumer.security.dto.response.CurrencyResponseDTO;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CURRENCY")
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String currencyCode;

    @Column
    private String currencySymbol;

    @Column
    private String currencyPrecision;

    @Column
    private String currencyFormat;

    @Column
    private boolean isActive;

    @Column(name = "create_date_time")
    @CreationTimestamp
    private LocalDateTime createDateTime;

    @Column(name = "update_date_time")
    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    public static CurrencyResponseDTO toDTO(Currency currency) {
        return CurrencyResponseDTO.builder()
                .id(currency.getId())
                .currencyCode(currency.getCurrencyCode())
                .currencyPrecision(currency.getCurrencyPrecision())
                .currencyFormat(currency.getCurrencyFormat())
                .currencySymbol(currency.getCurrencySymbol())
                .isActive(currency.isActive())
                .build();
    }
    public static List<CurrencyResponseDTO> toDTOList(List<Currency> currencies) {
        return currencies.stream()
                .map(Currency::toDTO)
                .filter(dto -> dto.getId() != null)
                .sorted(Comparator.comparing(CurrencyResponseDTO::getId))
                .collect(Collectors.toList());
    }
    public static Currency fromDTO(CurrencyRequestDTO dto) {
        return Currency.builder()
                .id(dto.getId())
                .currencyCode(dto.getCurrencyCode())
                .currencyPrecision(dto.getCurrencyPrecision())
                .currencyFormat(dto.getCurrencyFormat())
                .currencySymbol(dto.getCurrencySymbol())
                .isActive(dto.isActive())
                .build();
    }
}
