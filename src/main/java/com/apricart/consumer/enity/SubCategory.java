package com.apricart.consumer.enity;

import com.apricart.consumer.security.dto.request.SubCategoryRequestDTO;
import com.apricart.consumer.security.dto.response.CategoryResponseDTO;
import com.apricart.consumer.security.dto.response.SubCategoryResponseDTO;
import com.apricart.consumer.security.enums.LevelType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
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
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SUB_CATEGORY")
public class SubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String arabicName;

    @Enumerated(EnumType.STRING)
    private LevelType level;

    @Column
    private Integer position;

    @Column
    private Boolean status;

    /** Soft delete — hidden from admin and app. Separate from {@link #status} (show/unshow on app). */
    @Column
    private Boolean isDeleted;

    @Lob
    @Type(type="org.hibernate.type.TextType")
    @Column
    private String image;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "create_date_time")
    @CreationTimestamp
    private LocalDateTime createDateTime;

    @Column(name = "update_date_time")
    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    public static SubCategoryResponseDTO toDTO(SubCategory subCategory) {
        return SubCategoryResponseDTO.builder()
                .id(subCategory.getId())
                .name(subCategory.getName())
                .arabicName(subCategory.getArabicName())
                .image(subCategory.getImage())
                .level(subCategory.getLevel())
                .status(subCategory.getStatus())
                .isDeleted(Boolean.TRUE.equals(subCategory.getIsDeleted()))
                .position(subCategory.getPosition())
                .categoryId(subCategory.getCategory().getId())
                .build();
    }
    public static List<SubCategoryResponseDTO> toDTOList(List<SubCategory> subCategories) {
        return subCategories.stream()
                .map(SubCategory::toDTO)
                .filter(dto -> dto.getPosition() != null)
                .sorted(Comparator.comparing(SubCategoryResponseDTO::getPosition))
                .collect(Collectors.toList());
    }
    public static SubCategory fromDTO(SubCategoryRequestDTO dto) {
        return SubCategory.builder()
                .name(dto.getName())
                .arabicName(dto.getArabicName())
                .level(dto.getLevel())
                .image(dto.getImage())
                .status(dto.getStatus())
                .position(dto.getPosition())
                .isDeleted(false)
                .build();
    }
}
