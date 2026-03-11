package com.apricart.consumer.enity;

import com.apricart.consumer.security.dto.request.CustomerAddressRequestDTO;
import com.apricart.consumer.security.dto.response.CategoryResponseDTO;
import com.apricart.consumer.security.dto.response.CustomerAddressResponseDTO;
import com.apricart.consumer.security.enums.AddressType;
import com.apricart.consumer.security.enums.LocationType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CUSTOMER_ADDRESS")
public class CustomerAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String addressDetail;

    @Column
    private String addressLatitude;
    @Column
    private String addressLongitude;

    @Enumerated(EnumType.STRING)
    private AddressType addressType;

    @Enumerated(EnumType.STRING)
    private LocationType locationType;

    @Column
    private boolean isActive;

    @Column(name = "create_date_time")
    @CreationTimestamp
    private LocalDateTime createDateTime;

    @Column(name = "update_date_time")
    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;
    public static CustomerAddressResponseDTO toDTO(CustomerAddress customerAddress) {
        return CustomerAddressResponseDTO.builder()
                .id(customerAddress.getId())
                .addressLatitude(customerAddress.getAddressLatitude())
                .addressLongitude(customerAddress.getAddressLongitude())
                .isActive(customerAddress.isActive())
                .addressDetail(customerAddress.getAddressDetail())
                .addressType(customerAddress.getAddressType())
                .locationType(customerAddress.getLocationType())
                .customerId(customerAddress.getCustomer().getId())
                .cityId(customerAddress.getCity().getId())
                .build();
    }
    public static List<CustomerAddressResponseDTO> toDTOList(List<CustomerAddress> customerAddresses) {
        return customerAddresses.stream()
                .map(CustomerAddress::toDTO)
                .filter(dto -> dto.getCustomerId() != null)
                .sorted(Comparator.comparing(CustomerAddressResponseDTO::getCustomerId).reversed())
                .collect(Collectors.toList());
    }
    public static CustomerAddress fromDTO(CustomerAddressRequestDTO dto) {
        return CustomerAddress.builder()
                .id(dto.getId())
                .addressLatitude(dto.getAddressLatitude())
                .addressLongitude(dto.getAddressLongitude())
                .updateDateTime(LocalDateTime.now())
                .createDateTime(dto.getCreateDateTime() == null ? LocalDateTime.now() : dto.getCreateDateTime())
                .addressType(dto.getAddressType())
                .locationType(dto.getLocationType())
                .isActive(dto.isActive())
                .addressDetail(dto.getAddressDetail())
                .build();
    }
}
