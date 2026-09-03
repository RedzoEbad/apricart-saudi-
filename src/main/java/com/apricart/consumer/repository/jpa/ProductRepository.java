package com.apricart.consumer.repository.jpa;

import com.apricart.consumer.enity.Brand;
import com.apricart.consumer.enity.Category;
import com.apricart.consumer.enity.Product;
import com.apricart.consumer.enity.SubCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findProductByCategory(Category category, Pageable pageable);
    Page<Product> findProductBySubCategory(SubCategory subCategory, Pageable pageable);
    Page<Product> findProductByBrand(Brand brand, Pageable pageable);
    Page<Product> findProductByZohoId(Long id, Pageable pageable);
    Product findProductBySku(String sku);
    Page<Product> findProductByIsDiscounted(Boolean isDiscounted, Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Product p "
            + "WHERE p.category.id = :categoryId AND p.isDiscounted = true AND p.isActive = true "
            + "AND (p.isDeleted = false OR p.isDeleted IS NULL)")
    boolean existsByCategory_IdAndIsDiscountedTrueAndIsActiveTrueAndIsDeletedFalse(@Param("categoryId") Long categoryId);

    Long findTopByOrderByIdDesc();

    boolean existsByTitleIgnoreCaseAndSubCategory(String title, SubCategory subCategory);
    boolean existsByArabicTitleIgnoreCaseAndSubCategory(String arabicTitle, SubCategory subCategory);
    boolean existsByTitleIgnoreCaseAndSubCategoryAndIdNot(String title, SubCategory subCategory, Long id);
    boolean existsByArabicTitleIgnoreCaseAndSubCategoryAndIdNot(String arabicTitle, SubCategory subCategory, Long id);
    boolean existsBySkuIgnoreCase(String sku);
    boolean existsBySkuIgnoreCaseAndIdNot(String sku, Long id);

    @Query("SELECT DISTINCT pw.product FROM OrderItem oi JOIN oi.productWarehouse pw")
    Page<Product> findOrderedProducts(Pageable pageable);

    /** Native update bypasses Hibernate Search listeners that can fail commits on image URL changes. */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE PRODUCT SET image = :image WHERE id = :id", nativeQuery = true)
    int updateProductImage(@Param("id") Long id, @Param("image") String image);
}
