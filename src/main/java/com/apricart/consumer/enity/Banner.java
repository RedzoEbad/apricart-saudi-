package com.apricart.consumer.enity;

import com.apricart.consumer.security.dto.request.BannerRequestDTO;
import com.apricart.consumer.security.dto.response.BannerResponseDTO;
import com.apricart.consumer.security.dto.response.OrderItemResponseDTO;
import com.apricart.consumer.security.enums.LevelType;
import com.apricart.consumer.security.enums.PositionType;
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
@Table(name = "BANNER")
public class Banner extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String arabicName;

    @Column
    private Boolean status;


    @Enumerated(EnumType.STRING)
    private LevelType level;

    @Enumerated(EnumType.STRING)
    private PositionType position;

    @Column(name = "image", columnDefinition = "TEXT")  
    private String image;

    public static BannerResponseDTO toDTO(Banner banner) {
        return BannerResponseDTO.builder()
                .id(banner.getId())
                .name(banner.getName())
                .image(banner.getImage())
                .position(banner.getPosition())
                .level(banner.getLevel())
                .arabicName(banner.getArabicName())
                .status(banner.getStatus())
                .build();
    }

    public static List<BannerResponseDTO> toDTOList(List<Banner> banners) {
        return banners.stream()
                .map(Banner::toDTO)
                .filter(dto -> dto.getPosition() != null)
                .sorted(Comparator.comparing(BannerResponseDTO::getPosition).reversed())
                .collect(Collectors.toList());
    }

    public static Banner fromDTO(BannerRequestDTO dto) {
        return Banner.builder()
                .name(dto.getName())
                .arabicName(dto.getArabicName())
                .image(dto.getImage())
                .position(dto.getPosition())
                .level(dto.getLevel())
                .status(dto.getStatus())
                .build();
    }
}
