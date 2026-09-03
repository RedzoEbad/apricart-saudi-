package com.apricart.consumer.repository.jpa;

import com.apricart.consumer.enity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductWarehouseRepository extends JpaRepository<ProductWarehouse, Long> {

    @Query("SELECT pw FROM ProductWarehouse pw JOIN pw.product p "
            + "WHERE pw.category.id = :categoryId AND pw.warehouse.id = :warehouseId "
            + "AND (p.isDeleted = false OR p.isDeleted IS NULL) "
            + "AND (p.isActive = true OR :includeInactive = true)")
    Page<ProductWarehouse> findProductByCategoryIdAndWarehouseId(@Param("categoryId") Long categoryId,
                                                                  @Param("warehouseId") Long warehouseId,
                                                                  @Param("includeInactive") boolean includeInactive,
                                                                  Pageable pageable);

    @Query("SELECT pw FROM ProductWarehouse pw JOIN pw.product p "
            + "WHERE pw.subCategory.id = :subcategoryId AND pw.warehouse.id = :warehouseId "
            + "AND (p.isDeleted = false OR p.isDeleted IS NULL) "
            + "AND (p.isActive = true OR :includeInactive = true)")
    Page<ProductWarehouse> findProductBySubCategoryIdAndWarehouseId(@Param("subcategoryId") Long subcategoryId,
                                                                     @Param("warehouseId") Long warehouseId,
                                                                     @Param("includeInactive") boolean includeInactive,
                                                                     Pageable pageable);
    Optional<ProductWarehouse> findByProductAndWarehouseId (Product product, Long warehouseId);
    ProductWarehouse findByProductIdAndWarehouseId(Long productId, Long warehouseId);
    ProductWarehouse findProductWarehouseByProduct (Product product);
    List<ProductWarehouse> findProductWarehouseByWarehouse (Warehouse warehouse);
    List<ProductWarehouse> findProductWarehouseByPriceList (PriceList priceList);

    @Query("SELECT DISTINCT pw.category FROM ProductWarehouse pw WHERE pw.warehouse.id = :warehouseId AND pw.inStock = true AND pw.quantityInStock > 0 "
            + "AND (pw.category.isDeleted = false OR pw.category.isDeleted IS NULL) "
            + "AND (pw.product.isDeleted = false OR pw.product.isDeleted IS NULL)")
    List<Category> findDistinctCategoriesByWarehouseId(@Param("warehouseId") Long warehouseId);

    @Query("SELECT pw FROM ProductWarehouse pw " +
            "JOIN pw.product p " +
            "WHERE p.brand.id = :brandId " +
            "AND p.isActive = true " +
            "AND (p.isDeleted = false OR p.isDeleted IS NULL) " +
            "AND pw.warehouse.id = :warehouseId " +
            "ORDER BY p.position ASC")
    Page<ProductWarehouse> findProductByBrandIdAndWarehouseId(@Param("brandId") Long brandId,
                                                               @Param("warehouseId") Long warehouseId,
                                                               Pageable pageable);

    @Query("SELECT pw FROM ProductWarehouse pw " +
            "JOIN pw.product p " +
            "WHERE p.isNewArrivals = true " +
            "AND p.isActive = true " +
            "AND (p.isDeleted = false OR p.isDeleted IS NULL) " +
            "AND pw.warehouse.id = :warehouseId " +
            "ORDER BY p.updateDateTime DESC")
    List<ProductWarehouse> findTopNewArrivalsByWarehouseId(@Param("warehouseId") Long warehouseId, Pageable pageable);

    @Query("SELECT pw FROM ProductWarehouse pw " +
            "JOIN pw.product p " +
            "WHERE p.isTrending = true " +
            "AND p.isActive = true " +
            "AND (p.isDeleted = false OR p.isDeleted IS NULL) " +
            "AND pw.warehouse.id = :warehouseId " +
            "ORDER BY p.updateDateTime DESC")
    List<ProductWarehouse> findTopTrendingByWarehouseId(@Param("warehouseId") Long warehouseId, Pageable pageable);

    @Query("SELECT pw FROM ProductWarehouse pw " +
            "JOIN pw.product p " +
            "WHERE p.isRecommended = true " +
            "AND p.isActive = true " +
            "AND (p.isDeleted = false OR p.isDeleted IS NULL) " +
            "AND pw.warehouse.id = :warehouseId " +
            "ORDER BY p.updateDateTime DESC")
    List<ProductWarehouse> findTopRecommendedByWarehouseId(@Param("warehouseId") Long warehouseId, Pageable pageable);

    @Query("SELECT pw FROM ProductWarehouse pw " +
            "JOIN pw.product p " +
            "WHERE pw.warehouse.id = :warehouseId " +
            "AND (p.isDeleted = false OR p.isDeleted IS NULL) " +
            "AND (p.isActive = true OR :includeInactive = true) " +
            "AND (pw.isActive = true OR pw.isActive IS NULL) " +
            "ORDER BY p.position ASC, p.id ASC")
    Page<ProductWarehouse> findAllActiveByWarehouseId(@Param("warehouseId") Long warehouseId,
                                                      @Param("includeInactive") boolean includeInactive,
                                                      Pageable pageable);

    @Query("SELECT pw FROM ProductWarehouse pw " +
            "JOIN pw.product p " +
            "WHERE pw.category.id = :categoryId " +
            "AND pw.subCategory.id = :subCategoryId " +
            "AND pw.warehouse.id = :warehouseId " +
            "AND p.id <> :productId " +
            "AND p.isActive = true " +
            "AND (p.isDeleted = false OR p.isDeleted IS NULL) " +
            "ORDER BY p.updateDateTime DESC")
    List<ProductWarehouse> findSimilarItems(@Param("categoryId") Long categoryId,
                                            @Param("subCategoryId") Long subCategoryId,
                                            @Param("productId") Long productId,
                                            @Param("warehouseId") Long warehouseId,
                                            Pageable pageable);
}
