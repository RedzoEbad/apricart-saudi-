package com.apricart.consumer.enity;

import com.apricart.consumer.security.dto.request.SalesPersonRequestDTO;
import com.apricart.consumer.security.dto.response.SalesPersonResponseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
@Table(name = "SALE_PERSON")
public class SalePerson extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @Column
    private String arabicName;

    @Column
    private String email;

    @Nullable
    private String description;

    @Column
    private String arabicDescription;

    private Boolean isActive ;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;

    public static SalesPersonResponseDTO toDTO(SalePerson salePerson) {
        return SalesPersonResponseDTO.builder()
                .id(salePerson.getId())
                .name(salePerson.getName())
                .arabicName(salePerson.getArabicName())
                .email(salePerson.getEmail())
                .arabicDescription(salePerson.getArabicDescription())
                .description(salePerson.getDescription())
                .cityId(salePerson.getCity().getId())
                .isActive(salePerson.getIsActive())
                .build();
    }
    public static List<SalesPersonResponseDTO> toDTOList(List<SalePerson> salePersonList) {
        return salePersonList.stream()
                .map(SalePerson::toDTO)
                .collect(Collectors.toList());
    }
    public static SalePerson fromDTO(SalesPersonRequestDTO dto) {
        return SalePerson.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .description(dto.getDescription())
                .arabicName(dto.getArabicName())
                .arabicDescription(dto.getArabicDescription())
                .isActive(dto.getIsActive())
                .build();
    }

}
