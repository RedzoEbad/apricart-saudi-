package com.apricart.consumer.service.Impl;

import com.apricart.consumer.enity.*;
import com.apricart.consumer.exceptions.ResourceNotFoundException;
import com.apricart.consumer.mapper.ProductMapper;
import com.apricart.consumer.repository.jpa.ProductWarehouseRepository;
import com.apricart.consumer.security.dto.request.ProductWarehouseRequestDTO;
import com.apricart.consumer.security.dto.response.ProductWarehouseResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.*;
import static com.apricart.consumer.security.constants.ResponseMessage.*;

@Service
@Transactional
public class ProductWarehouseServiceImpl implements ProductWarehouseService {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ProductWarehouseServiceImpl.class);

    @Autowired
    private ProductWarehouseRepository productWarehouseRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private PriceListService priceListService;

    @Autowired
    private TaxService taxService;

    @Autowired
    private ProductMapper productMapper;
    private static final String PRODUCT_WAREHOUSE_ENG = "Product Warehouse";
    private static final String PRODUCT_WAREHOUSE_ARB = "مستودع المنتجات";

    @Override
    public List<ProductWarehouse> getAllProductsWarehouse() {
        LOGGER.info("Getting all products warehouse");
        return productWarehouseRepository.findAll();
    }

    @Override
    public ProductWarehouse findById(Long id, LanguageType languageType) {
        LOGGER.info("Finding product warehouse by id: {}", id);
        return productWarehouseRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("ProductsWarehouse with id {} not found", id);
                    return LanguageType.ARB.equals(languageType) ? new ResourceNotFoundException(PRODUCT_WAREHOUSE_ARB, id, true) : new ResourceNotFoundException(PRODUCT_WAREHOUSE_ENG, id, false);
                });
    }

    @Override
    public ProductWarehouseResponseDTO findByProductId(Long id, LanguageType languageType) {
        LOGGER.info("Finding product warehouse by product Id]: {}", id);
        Product product = productService.findById(id, languageType);
        ProductWarehouse productWarehouse = productWarehouseRepository.findProductWarehouseByProduct(product);
        return ProductWarehouse.toDTO(productWarehouse, productMapper, languageType);
    }

    @Override
    public ProductWarehouseResponseDTO findByProductIdAndWarehouseId(Long productId, Long warehouseId, LanguageType languageType) {
        LOGGER.info("Finding product warehouse by product Id: {} and Warehouse Id: {}", productId, warehouseId);
        ProductWarehouse productWarehouse = productWarehouseRepository.findByProductIdAndWarehouseId(productId, warehouseId);
        return productWarehouse != null ? ProductWarehouse.toDTO(productWarehouse, productMapper, languageType) : null;
    }

    @Override
    public ProductWarehouse findProductWarehouseByProductId(Long id, LanguageType languageType) {
        LOGGER.info("Finding product warehouse by product Id]: {}", id);
        Product product = productService.findById(id, languageType);
        return productWarehouseRepository.findProductWarehouseByProduct(product);
    }

    @Override
    public List<ProductWarehouse> findByWarehouseId(Long id, LanguageType languageType) {
        LOGGER.info("Finding product warehouse by warehouse Id]: {}", id);
        Warehouse warehouse = warehouseService.findById(id, languageType);
        return productWarehouseRepository.findProductWarehouseByWarehouse(warehouse);
    }

    @Override
    public List<ProductWarehouse> findByPriceListId(Long id, LanguageType languageType) {
        LOGGER.info("Finding product warehouse by priceList Id: {}", id);
        PriceList priceList = priceListService.findById(id, languageType);
        return productWarehouseRepository.findProductWarehouseByPriceList(priceList);
    }

    @Override
    public ProductWarehouse updateProductWarehouseStatusById(Long id, Boolean status, LanguageType languageType) {
        LOGGER.info("Updating product warehouse status for id: {} to {}", id, status);
        ProductWarehouse existingProductWarehouse = findById(id, languageType);
        existingProductWarehouse.setIsActive(status == null ? existingProductWarehouse.getIsActive() : status);
        return save(existingProductWarehouse);
    }

    @Override
    public void addProductWarehouse(ProductWarehouseRequestDTO productWarehouseRequestDTO, LanguageType lang) {
        LOGGER.info("Adding products warehouse: {}", productWarehouseRequestDTO.toString());
        ProductWarehouse productWarehouse;
        if (productWarehouseRequestDTO.getId() != null) {
            productWarehouse = findById(productWarehouseRequestDTO.getId(), lang);
        }
        validateProductWarehouseReferences(productWarehouseRequestDTO, lang);

        Product product = productService.findById(productWarehouseRequestDTO.getProductId(), lang);
        Optional<ProductWarehouse> existingProductWarehouse = productWarehouseRepository.findByProductAndWarehouseId(product, productWarehouseRequestDTO.getWarehouseId());

        if (existingProductWarehouse.isPresent()) {
            LOGGER.info("ProductWarehouse with product name {} and warehouse ID {} already exists", product.getTitle(), productWarehouseRequestDTO.getWarehouseId());
            throw new IllegalStateException(LanguageType.ARB.equals(lang) ? PRODUCT_WAREHOUSE_DUPLICATE_ARABIC : PRODUCT_WAREHOUSE_DUPLICATE);
        }
        productWarehouse = ProductWarehouse.fromDTO(productWarehouseRequestDTO);
        productWarehouse.setProduct(product);
        productWarehouse.setWarehouse(warehouseService.findById(productWarehouseRequestDTO.getWarehouseId(), lang));
        productWarehouse.setPriceList(priceListService.findById(productWarehouseRequestDTO.getPriceListId(), lang));
        productWarehouse.setTax(taxService.findById(productWarehouseRequestDTO.getTaxId(), lang));
        productWarehouse.setCategory(product.getCategory());
        productWarehouse.setSubCategory(product.getSubCategory());
        save(productWarehouse);
    }

    @Override
    public ProductWarehouse updateProductWarehouse(ProductWarehouseRequestDTO productWarehouseRequestDTO, LanguageType languageType) {
        LOGGER.info("Updating product warehouse: {}", productWarehouseRequestDTO.toString());
        ProductWarehouse existingProductWarehouse = findById(productWarehouseRequestDTO.getId(), languageType);

        existingProductWarehouse.setRate(productWarehouseRequestDTO.getRate() == null ? existingProductWarehouse.getRate() : productWarehouseRequestDTO.getRate());
        existingProductWarehouse.setInStock(productWarehouseRequestDTO.getInStock() == null ? existingProductWarehouse.getInStock() : productWarehouseRequestDTO.getInStock());
        existingProductWarehouse.setCurrentRate(productWarehouseRequestDTO.getCurrentRate() == null ? existingProductWarehouse.getCurrentRate() : productWarehouseRequestDTO.getCurrentRate());
        existingProductWarehouse.setSpecialRate(productWarehouseRequestDTO.getSpecialRate() == null ? existingProductWarehouse.getSpecialRate() : productWarehouseRequestDTO.getSpecialRate());
        existingProductWarehouse.setQuantityInStock(productWarehouseRequestDTO.getQuantityInStock() == null ? existingProductWarehouse.getQuantityInStock() : productWarehouseRequestDTO.getQuantityInStock());
        existingProductWarehouse.setPriceList(productWarehouseRequestDTO.getPriceListId() == null ? existingProductWarehouse.getPriceList() : priceListService.findById(productWarehouseRequestDTO.getPriceListId(), languageType));
        existingProductWarehouse.setWarehouse(productWarehouseRequestDTO.getWarehouseId() == null ? existingProductWarehouse.getWarehouse() : warehouseService.findById(productWarehouseRequestDTO.getWarehouseId(), languageType));
        existingProductWarehouse.setTax(productWarehouseRequestDTO.getTaxId() == null ? existingProductWarehouse.getTax() : taxService.findById(productWarehouseRequestDTO.getTaxId(), languageType));
        existingProductWarehouse.setProduct(productWarehouseRequestDTO.getProductId() == null ? existingProductWarehouse.getProduct() : productService.findById(productWarehouseRequestDTO.getProductId(), languageType));

        return save(existingProductWarehouse);
    }

    @Override
    public void deleteProductWarehouse(Long id, LanguageType languageType) {
        LOGGER.info("Deactivating product warehouse status for id: {}", id);
        ProductWarehouse existingProductWarehouse = findById(id, languageType);
        if (existingProductWarehouse != null && existingProductWarehouse.getIsActive()) {
            existingProductWarehouse.setIsActive(false);
            save(existingProductWarehouse);
        }
    }

    public ProductWarehouse save(ProductWarehouse productWarehouse) {
        LOGGER.info("Saving products warehouse: {}", productWarehouse.toString());
        return productWarehouseRepository.save(productWarehouse);
    }

    @Override
    public List<ProductWarehouseResponseDTO> findByCategoryIdAndWarehouseId(Long categoryId, Long warehouseId, int page, int size, LanguageType languageType) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<ProductWarehouse> productWarehouses = productWarehouseRepository.findProductByCategoryIdAndWarehouseId(categoryId, warehouseId, pageRequest).getContent();
        return ProductWarehouse.toDTOList(productWarehouses, productMapper, languageType);
    }

    @Override
    public List<ProductWarehouseResponseDTO> findBySubCategoryIdAndWarehouseId(Long subCategoryId, Long warehouseId, int page, int size, LanguageType languageType) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<ProductWarehouse> productWarehouses = productWarehouseRepository.findProductBySubCategoryIdAndWarehouseId(subCategoryId, warehouseId,pageRequest).getContent();
        return ProductWarehouse.toDTOList(productWarehouses, productMapper, languageType);
    }

    @Override
    public List<ProductWarehouseResponseDTO> findByBrandIdAndWarehouseId(Long brandId, Long warehouseId, int page, int size, LanguageType languageType) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<ProductWarehouse> productWarehouses = productWarehouseRepository.findProductByBrandIdAndWarehouseId(brandId, warehouseId, pageRequest).getContent();
        return ProductWarehouse.toDTOList(productWarehouses, productMapper, languageType);
    }

    @Override
    public List<Category> findCategoriesByWarehouseId(Long warehouseId) {
        LOGGER.info("Fetching categories for warehouseId: {}", warehouseId);
        List<Category> warehouseCategories = productWarehouseRepository.findDistinctCategoriesByWarehouseId(warehouseId);
        LOGGER.info("Found {} warehouse categories: {}", warehouseCategories.size(), warehouseCategories);

        List<Category> discountedCategories = categoryService.getDiscountedCategories(Boolean.TRUE);
        LOGGER.info("Found {} discounted categories: {}", discountedCategories.size(), discountedCategories);

        // Combine categories using a Set to remove duplicates
        Set<Category> uniqueCategories = new LinkedHashSet<>(warehouseCategories);
        uniqueCategories.addAll(discountedCategories);
        LOGGER.info("After deduplication, {} unique categories: {}", uniqueCategories.size(), uniqueCategories);

        return new ArrayList<>(uniqueCategories);
    }

    @Override
    public List<ProductWarehouseResponseDTO> findNewArrivalsByWarehouseId(Long warehouseId, int pageNo, int pageSize, LanguageType languageType) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        List<ProductWarehouse> productWarehouses = productWarehouseRepository.findTopNewArrivalsByWarehouseId(warehouseId, pageable);
        return ProductWarehouse.toDTOList(productWarehouses, productMapper, languageType);
    }

    @Override
    public List<ProductWarehouseResponseDTO> findTrendingByWarehouseId(Long warehouseId, int pageNo, int pageSize, LanguageType languageType) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        List<ProductWarehouse> productWarehouses = productWarehouseRepository.findTopTrendingByWarehouseId(warehouseId, pageable);
        return ProductWarehouse.toDTOList(productWarehouses, productMapper, languageType);
    }

    @Override
    public List<ProductWarehouseResponseDTO> findRecommendedByWarehouseId(Long warehouseId, int pageNo, int pageSize, LanguageType languageType) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        List<ProductWarehouse> productWarehouses = productWarehouseRepository.findTopRecommendedByWarehouseId(warehouseId, pageable);
        return ProductWarehouse.toDTOList(productWarehouses, productMapper, languageType);
    }

    @Override
    public List<ProductWarehouseResponseDTO> findSimilarItems(Long categoryId, Long subCategoryId,
                                                              Long productId, Long warehouseId, int pageNo, int limit, LanguageType languageType) {
        Pageable pageable = PageRequest.of(pageNo, limit);
        List<ProductWarehouse> productWarehouses = productWarehouseRepository.findSimilarItems(categoryId, subCategoryId, productId, warehouseId, pageable);
        return ProductWarehouse.toDTOList(productWarehouses, productMapper, languageType);
    }

    @Override
    public ProductWarehouse updateQuantityByWarehouseAndSku(String sku, Long warehouseId, int quantity, LanguageType languageType) {
        ProductWarehouse existingProductWarehouse = findProductWarehouseByProductId(productService.findProductBySKU(sku).getId(), languageType);
        existingProductWarehouse.setQuantityInStock(quantity);
        return save(existingProductWarehouse);
    }

    public void validateProductWarehouseReferences(ProductWarehouseRequestDTO dto, LanguageType lang) {
        if (dto.getProductId() == null) {
            throw new IllegalArgumentException(LanguageType.ARB.equals(lang) ? PRODUCT_NULL_ARABIC : PRODUCT_NULL);
        }
        if (dto.getPriceListId() == null) {
            throw new IllegalArgumentException(LanguageType.ARB.equals(lang) ? PRICE_LIST_NULL : PRICE_LIST_NULL_ARABIC);
        }
        if (dto.getTaxId() == null) {
            throw new IllegalArgumentException(LanguageType.ARB.equals(lang) ? TAX_NULL_ARABIC : TAX_NULL);
        }
        if (dto.getCategoryId() == null) {
            throw new IllegalArgumentException(LanguageType.ARB.equals(lang) ? CATEGORY_NULL_ARABIC : CATEGORY_NULL);
        }
        if (dto.getSubCategoryId() == null) {
            throw new IllegalArgumentException(LanguageType.ARB.equals(lang) ? SUBCATEGORY_NULL_ARABIC : SUBCATEGORY_NULL);
        }
        if (dto.getWarehouseId() == null) {
            throw new IllegalArgumentException(LanguageType.ARB.equals(lang) ? WAREHOUSE_NULL_ARABIC : WAREHOUSE_NULL);
        }
    }
}
