package com.apricart.consumer.enity;

import com.apricart.consumer.security.dto.request.WarehouseRequestDTO;
import com.apricart.consumer.security.dto.response.TaxResponseDTO;
import com.apricart.consumer.security.dto.response.WarehouseResponseDTO;
import lombok.*;

import javax.persistence.*;
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
@Table(name = "warehouse")
public class Warehouse extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String arabicName;

    @Column
    private String address;

    @Column
    private String arabicAddress;

    @Column
    private boolean isActive;

    @Column
    private String latitude;

    @Column
    private String longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;

    public static WarehouseResponseDTO toDTO(Warehouse warehouse) {
        return WarehouseResponseDTO.builder()
                .id(warehouse.getId())
                .name(warehouse.getName())
                .arabicName(warehouse.getArabicName())
                .arabicAddress(warehouse.getArabicAddress())
                .isActive(warehouse.isActive())
                .latitude(warehouse.getLatitude())
                .longitude(warehouse.getLongitude())
                .address(warehouse.getAddress())
                .cityId(warehouse.getCity().getId())
                .build();
    }
    public static List<WarehouseResponseDTO> toDTOList(List<Warehouse> warehouses) {
        return warehouses.stream()
                .map(Warehouse::toDTO)
                .filter(dto -> dto.getId() != null)
                .sorted(Comparator.comparing(WarehouseResponseDTO::getId))
                .collect(Collectors.toList());
    }
    public static Warehouse fromDTO(WarehouseRequestDTO dto) {
        return Warehouse.builder()
                .id(dto.getId())
                .name(dto.getName())
                .arabicName(dto.getArabicName())
                .arabicAddress(dto.getArabicAddress())
                .isActive(dto.getIsActive())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .address(dto.getAddress())
                .build();
    }

}
