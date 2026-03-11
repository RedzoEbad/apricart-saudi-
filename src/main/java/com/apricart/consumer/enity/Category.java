package com.apricart.consumer.enity;

import com.apricart.consumer.security.dto.request.CategoryRequestDTO;
import com.apricart.consumer.security.dto.response.CategoryResponseDTO;
import com.apricart.consumer.security.enums.LevelType;
import lombok.*;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Store;

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
@Table(name = "category")
@Indexed
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @Field(index = Index.YES, analyze = Analyze.NO, store = Store.YES)
    private String name;

    @Column(unique = true)
    @Field(index = Index.YES, analyze = Analyze.NO, store = Store.YES)
    private String arabicName;

    @Enumerated(EnumType.STRING)
    @Column
    @Field(index = Index.YES, analyze = Analyze.NO, store = Store.YES)
    private LevelType level;

    @Column
    @Field(index = Index.YES, analyze = Analyze.NO, store = Store.YES)
    private Integer position;

    @Column
    private Boolean isDiscountedCategory;

    @Column
    private Boolean status;

    @Column(name = "image", columnDefinition = "TEXT")
    @Field(index = Index.YES, analyze = Analyze.NO, store = Store.YES)
    private String image;

    public static CategoryResponseDTO toDTO(Category category) {
        return CategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .arabicName(category.getArabicName())
                .image(category.getImage())
                .level(category.getLevel())
                .status(category.getStatus())
                .isDiscountedCategory(category.getIsDiscountedCategory())
                .position(category.getPosition())
                .build();
    }

    public static List<CategoryResponseDTO> toDTOList(List<Category> categories) {
        return categories.stream()
                .map(Category::toDTO)
                .filter(dto -> dto.getPosition() != null)
                .sorted(Comparator.comparing(CategoryResponseDTO::getPosition))
                .collect(Collectors.toList());
    }

    public static Category fromDTO(CategoryRequestDTO categoryRequestDTO) {
        return Category.builder()
                .name(categoryRequestDTO.getName())
                .arabicName(categoryRequestDTO.getArabicName())
                .level(categoryRequestDTO.getLevel())
                .image(categoryRequestDTO.getImage())
                .status(categoryRequestDTO.getStatus())
                .position(categoryRequestDTO.getPosition())
                .isDiscountedCategory(categoryRequestDTO.getIsDiscountedCategory())
                .build();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id != null && id.equals(category.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}

