package com.apricart.consumer.repository.jpa;

import com.apricart.consumer.enity.Category;
import com.apricart.consumer.security.enums.LevelType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findCategoryByName(String name);
    Category findCategoryByArabicName(String arabicName);
    List<Category> findCategoryByLevel(LevelType level);
    List<Category> findAllByIsDiscountedCategory(Boolean isDiscountedCategory);
}
