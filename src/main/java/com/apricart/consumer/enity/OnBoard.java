package com.apricart.consumer.enity;

import com.apricart.consumer.security.dto.request.OnBoardRequestDTO;
import com.apricart.consumer.security.dto.response.CategoryResponseDTO;
import com.apricart.consumer.security.dto.response.OnBoardResponseDTO;
import lombok.*;

import javax.persistence.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Table(name = "OnBoard")
public class OnBoard extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String arabicTitle;

    @Column
    private String description;

    @Column
    private String arabicDescription;

    @Lob
    private String image;

    public static OnBoardResponseDTO toDTO(OnBoard onBoard) {
        return OnBoardResponseDTO.builder()
                .id(onBoard.getId())
                .title(onBoard.getTitle())
                .arabicTitle(onBoard.getArabicTitle())
                .description(onBoard.getDescription())
                .arabicDescription(onBoard.getArabicDescription())
                .image(onBoard.getImage())
                .build();
    }

    public static List<OnBoardResponseDTO> toDTOList(List<OnBoard> onBoards) {
        return onBoards.stream()
                .map(OnBoard::toDTO)
                .filter(dto -> dto.getId() != null)
                .sorted(Comparator.comparing(OnBoardResponseDTO::getId))
                .collect(Collectors.toList());
    }

    public static OnBoard fromDTO(OnBoardRequestDTO dto) {
        return OnBoard.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .arabicTitle(dto.getArabicTitle())
                .description(dto.getDescription())
                .arabicDescription(dto.getArabicDescription())
                .image(dto.getImage())
                .build();
    }
}
