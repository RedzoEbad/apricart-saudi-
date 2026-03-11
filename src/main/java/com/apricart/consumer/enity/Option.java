package com.apricart.consumer.enity;

import com.apricart.consumer.security.dto.request.OptionRequestDTO;
import com.apricart.consumer.security.dto.response.CategoryResponseDTO;
import com.apricart.consumer.security.dto.response.OptionResponseDTO;
import lombok.*;

import javax.persistence.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Table(name = "OPTION")
public class Option extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String key;

    @Column
    private String value;

    @Column
    private Boolean status;


    public static OptionResponseDTO toDTO(Option option) {
        return OptionResponseDTO.builder()
                .id(option.getId())
                .key(option.getKey())
                .value(option.getValue())
                .status(option.getStatus())
                .build();
    }
    public static List<OptionResponseDTO> toDTOList(List<Option> options) {
        return options.stream()
                .map(Option::toDTO)
                .filter(dto -> dto.getId() != null)
                .sorted(Comparator.comparing(OptionResponseDTO::getId))
                .collect(Collectors.toList());
    }
    public static Option fromDTO(OptionRequestDTO optionRequestDTO) {
        return Option.builder()
                .key(optionRequestDTO.getKey())
                .value(optionRequestDTO.getValue())
                .status(optionRequestDTO.getStatus())
                .build();
    }
}

