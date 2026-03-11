package com.apricart.consumer.enity;

import com.apricart.consumer.security.dto.request.CityRequestDTO;
import com.apricart.consumer.security.dto.response.CityResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.WarehouseService;
import lombok.*;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

import javax.persistence.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on Feb, 2024
 *
 * @author Kashaf Arshad
 */

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "CITY")
public class City  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;
    @Column
    private String country;
    @Column
    private String arabicName;
    @Column
    private String arabicCountry;
    @Column
    private Boolean isActive;
    @Column(name = "image", columnDefinition = "TEXT")
    @Field(index = Index.YES, analyze = Analyze.NO, store = Store.YES)
    private String image;

    public static City fromDTO(CityRequestDTO cityRequestDTO) {
        return City.builder()

                .name(cityRequestDTO.getName())
                .arabicCountry(cityRequestDTO.getArabicCountry())
                .arabicName(cityRequestDTO.getArabicName())
                .country(cityRequestDTO.getCountry())
                .isActive(cityRequestDTO.getIsActive())
                .build();
    }
    public static CityResponseDTO toDTO(City city, WarehouseService warehouseService, LanguageType languageType) {
        return CityResponseDTO.builder()
                .id(city.getId())
                .name(city.getName())
                .arabicCountry(city.getArabicCountry())
                .arabicName(city.getArabicName())
                .country(city.getCountry())
                .isActive(city.getIsActive())
                .image(city.getImage())
                .warehouseDetails(Warehouse.toDTOList(warehouseService.findWarehousesByCityId(city.getId(), languageType)))
                .build();
    }
    public static List<CityResponseDTO> toDTOList(List<City> cities, WarehouseService warehouseService, LanguageType languageType) {
        return cities.stream()
                .map(city -> City.toDTO(city, warehouseService, languageType))
                .filter(dto -> dto.getId() != null)
                .sorted(Comparator.comparing(CityResponseDTO::getId))
                .collect(Collectors.toList());
    }
}