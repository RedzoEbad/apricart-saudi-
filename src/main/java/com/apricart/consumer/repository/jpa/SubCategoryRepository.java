package com.apricart.consumer.repository.jpa;

import com.apricart.consumer.enity.Category;
import com.apricart.consumer.enity.SubCategory;
import com.apricart.consumer.security.enums.LevelType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    SubCategory findSubCategoryByName(String name);

    List<SubCategory> findSubCategoryByLevel(LevelType level);
    @Query("SELECT sc FROM SubCategory sc WHERE sc.category = :category "
            + "AND (sc.isDeleted = false OR sc.isDeleted IS NULL) "
            + "AND EXISTS (SELECT pw FROM ProductWarehouse pw WHERE pw.subCategory = sc AND pw.warehouse.id = :warehouseId AND pw.inStock = true AND pw.quantityInStock > 0 "
            + "AND (pw.product.isDeleted = false OR pw.product.isDeleted IS NULL))")
    List<SubCategory> findSubCategoriesByCategory(Category category, @Param("warehouseId") Long warehouseId);


    @Query("SELECT sc FROM SubCategory sc " +
            "WHERE sc.category = :category " +
            "OR sc.category.isDiscountedCategory = true " )
    List<SubCategory> findDiscountedSubCategoriesByCategory(@Param("category") Category category);

    boolean existsByNameIgnoreCaseAndCategory(String name, Category category);
    boolean existsByArabicNameIgnoreCaseAndCategory(String arabicName, Category category);
    boolean existsByNameIgnoreCaseAndCategoryAndIdNot(String name, Category category, Long id);
    boolean existsByArabicNameIgnoreCaseAndCategoryAndIdNot(String arabicName, Category category, Long id);
}