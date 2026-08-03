package com.apricart.consumer.mapper;

import com.apricart.consumer.enity.Product;
import com.apricart.consumer.enity.ProductWarehouse;
import com.apricart.consumer.enity.Tax;
import com.apricart.consumer.security.dto.dto.ProductDetailDTO;
import com.apricart.consumer.security.dto.response.ProductResponseDTO;
import com.apricart.consumer.security.dto.response.ProductWarehouseResponseDTO;
import com.apricart.consumer.security.dto.response.TaxResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.ProductService;
import com.apricart.consumer.service.TaxService;
import com.apricart.consumer.service.WishListService;
import com.apricart.consumer.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    @Autowired
    private ProductService productService;

    @Autowired
    private WishListService wishListService;

    @Autowired
    private ImageUtils imageUtils;

    @Autowired
    private TaxService taxService;
    private static final String PERCENT_SIGN = "%";
    public static final double DIVIDE_AMOUNT = 100.0;
    private static final int DECIMAL_PLACES = 3;
    protected static final Logger LOGGER = LoggerFactory.getLogger(ProductMapper.class);

    public List<ProductDetailDTO> mapAndSortProductDetails(List<ProductWarehouseResponseDTO> productWarehouses, Long customerId, LanguageType languageType) {
        java.util.Map<Long, TaxResponseDTO> taxCache = new java.util.HashMap<>();
        List<ProductDetailDTO> filteredProducts =  productWarehouses.stream()
                .filter(pw -> Optional.ofNullable(pw.getProduct().getIsActive()).orElse(false))
                .filter(pw -> Optional.ofNullable(pw.getInStock()).orElse(false) && Optional.of(pw.getQuantityInStock() > 0).orElse(false))
                .map(pw -> mapToProductDetailDTO(pw, customerId, languageType, taxCache))
                .sorted(Comparator.comparing(ProductDetailDTO::getPosition))
                .collect(Collectors.toList());

        if (filteredProducts.isEmpty()) {
            return Collections.emptyList();
        }
        return filteredProducts;
    }

    public ProductDetailDTO mapToProductDetailDTO(ProductWarehouseResponseDTO productWarehouse, Long customerId, LanguageType languageType) {
        return mapToProductDetailDTO(productWarehouse, customerId, languageType, new java.util.HashMap<>());
    }

    public ProductDetailDTO mapToProductDetailDTO(ProductWarehouseResponseDTO productWarehouse, Long customerId, LanguageType languageType, java.util.Map<Long, TaxResponseDTO> taxCache) {
        ProductResponseDTO product = productWarehouse.getProduct();
        TaxResponseDTO taxResponseDTO = null;
        if (productWarehouse.getTaxId() != null) {
            TaxResponseDTO originalTax = taxCache.computeIfAbsent(productWarehouse.getTaxId(), id -> Tax.toDTO(taxService.findById(id, languageType)));
            if (originalTax != null) {
                String cleanPct = originalTax.getTaxPercentage() != null ? originalTax.getTaxPercentage().replace(PERCENT_SIGN, "").trim() : "0";
                double taxPercentageVal = Double.parseDouble(cleanPct);
                double taxAmount = calculateTaxAmount(taxPercentageVal, productWarehouse.getCurrentRate());
                taxResponseDTO = TaxResponseDTO.builder()
                        .id(originalTax.getId())
                        .taxName(originalTax.getTaxName())
                        .taxPercentage(cleanPct + PERCENT_SIGN)
                        .taxAmount(taxAmount)
                        .taxType(originalTax.getTaxType())
                        .taxFactor(originalTax.getTaxFactor())
                        .tdsPayableAccountId(originalTax.getTdsPayableAccountId())
                        .taxAuthorityId(originalTax.getTaxAuthorityId())
                        .taxAuthorityName(originalTax.getTaxAuthorityName())
                        .taxSpecificType(originalTax.getTaxSpecificType())
                        .countryCode(originalTax.getCountryCode())
                        .purchaseTaxExpenseAccountId(originalTax.getPurchaseTaxExpenseAccountId())
                        .isValueAdded(originalTax.isValueAdded())
                        .build();
            }
        }
        Boolean isWishList = (customerId != null && customerId > 0) ? wishListService.isProductInWishlist(customerId, product.getId()) : false;
        double discountedPrice = calculateDiscountedPrice(productWarehouse.getCurrentRate(), productWarehouse.getSpecialRate());


        return ProductDetailDTO.builder()
                .id(product.getId() != null ? product.getId() : null)
                .title(product.getTitle() != null ? product.getTitle() : null)
                .arabicTitle(product.getArabicTitle() != null ? product.getArabicTitle() : null)
                .weight(product.getWeight() != null ? product.getWeight() : null)
                .image(product.getImage() != null ? imageUtils.getImagePath(product.getImage()) : null)
                .description(product.getDescription() != null ? product.getDescription() : null)
                .arabicDescription(product.getArabicDescription() != null ? product.getArabicDescription() : null)
                .sku(product.getSku() != null ? product.getSku() : null)
                .isActive(productWarehouse.getIsActive() != null ? productWarehouse.getIsActive() : null)
                .isFeatured(product.getIsFeatured() != null ? product.getIsFeatured() : null)
                .isTrending(product.getIsTrending() != null ? product.getIsTrending() : null)
                .isDiscounted(product.getIsDiscounted() != null ? product.getIsDiscounted() : null)
                .isNewArrivals(product.getIsNewArrivals() != null ? product.getIsNewArrivals() : null)
                .isRecommended(product.getIsRecommended() != null ? product.getIsRecommended() : null)
                .isWishList(isWishList)
                .position(product.getPosition() != null ? product.getPosition() : null)
                .categoryId(product.getCategoryId() != null ? product.getCategoryId() : null)
                .subCategoryId(product.getSubCategoryId() != null ? product.getSubCategoryId() : null)
                .brandId(product.getBrandId() != null ? product.getBrandId() : null)
                .brandName(product.getBrandName() != null ? product.getBrandName() : null)
                .brandNameArabic(product.getBrandNameArabic() != null ? product.getBrandNameArabic() : null)
                .inStockQuantity(productWarehouse.getQuantityInStock())
                .currentRate(productWarehouse.getCurrentRate() != null ? productWarehouse.getCurrentRate() : null)
                .inStock(productWarehouse.getInStock() != null ? productWarehouse.getInStock() : null)
                .rate(productWarehouse.getRate() != null ? productWarehouse.getRate() : null)
                .taxId(taxResponseDTO != null && taxResponseDTO.getId() != null ? taxResponseDTO.getId() : null)
                .tax(taxResponseDTO)
                .productWarehouseId(productWarehouse.getId() != null ? productWarehouse.getId() : null)
                .inStockQuantity(productWarehouse.getQuantityInStock() != null ? productWarehouse.getQuantityInStock() : null)
                .warehouseId(productWarehouse.getWarehouseId() != null ? productWarehouse.getWarehouseId() : null)
                .specialRate(productWarehouse.getSpecialRate() != null ? productWarehouse.getSpecialRate() : null)
                .priceListId(productWarehouse.getPriceListId() != null ? productWarehouse.getPriceListId() : null)
                .discountPercentage(productWarehouse.getDiscountPercentage() == null ?
                        getDiscountPercentage(productWarehouse) : productWarehouse.getDiscountPercentage())
                .discountedPrice(discountedPrice)
                .build();

    }

    public ProductDetailDTO mapToProductDetailDTO(ProductWarehouse productWarehouse, LanguageType languageType) {
        Product product = productWarehouse.getProduct();
        double taxAmount = calculateTaxAmount(productWarehouse.getTax().getTaxPercentage(), productWarehouse.getCurrentRate());
        TaxResponseDTO tax = Tax.toDTO(productWarehouse.getTax());
        setTaxDetails(taxAmount, tax);
        double discountedPrice = calculateDiscountedPrice(productWarehouse.getCurrentRate() ,productWarehouse.getSpecialRate());


        return ProductDetailDTO.builder()
                .id(product.getId() != null ? product.getId() : null)
                .title(product.getTitle() != null ? product.getTitle() : null)
                .arabicTitle(product.getArabicTitle() != null ? product.getArabicTitle() : null)
                .weight(product.getWeight() != null ? product.getWeight() : null)
                .image(product.getImage() != null ? product.getImage() : null)
                .description(product.getDescription() != null ? product.getDescription() : null)
                .arabicDescription(product.getArabicDescription() != null ? product.getArabicDescription() : null)
                .sku(product.getSku() != null ? product.getSku() : null)
                .isActive(product.getIsActive() != null ? product.getIsActive() : null)
                .isFeatured(product.getIsFeatured() != null ? product.getIsFeatured() : null)
                .isTrending(product.getIsTrending() != null ? product.getIsTrending() : null)
                .isDiscounted(product.getIsDiscounted() != null ? product.getIsDiscounted() : null)
                .isNewArrivals(product.getIsNewArrivals() != null ? product.getIsNewArrivals() : null)
                .isRecommended(product.getIsRecommended() != null ? product.getIsRecommended() : null)
                .position(product.getPosition() != null ? product.getPosition() : null)
                .categoryId(product.getCategory().getId() != null ? product.getCategory().getId() : null)
                .subCategoryId(product.getSubCategory().getId() != null ? product.getSubCategory().getId() : null)
                .brandId(product.getBrand().getId() != null ? product.getBrand().getId() : null)
                .brandName(product.getBrand().getName() != null ? product.getBrand().getName() : null)
                .brandNameArabic(product.getBrand().getArabicName() != null ? product.getBrand().getArabicName() : null)
                .currentRate(productWarehouse.getCurrentRate() != null ? productWarehouse.getCurrentRate() : null)
                .inStock(productWarehouse.getInStock() != null ? productWarehouse.getInStock() : null)
                .rate(productWarehouse.getRate() != null ? productWarehouse.getRate() : null)
                .taxId(productWarehouse.getTax().getId() != null ? productWarehouse.getTax().getId() : null)
                .tax(tax)
                .productWarehouseId(productWarehouse.getId() != null ? productWarehouse.getId() : null)
                .inStockQuantity(productWarehouse.getQuantityInStock() != null ? productWarehouse.getQuantityInStock() : null)
                .warehouseId(productWarehouse.getWarehouse().getId() != null ? productWarehouse.getWarehouse().getId() : null)
                .specialRate(productWarehouse.getSpecialRate() != null ? productWarehouse.getSpecialRate() : null)
                .priceListId(productWarehouse.getPriceList().getId() != null ? productWarehouse.getPriceList().getId() : null)
                .discountPercentage(productWarehouse.getDiscountPercentage() == null ?
                        getDiscountPercentage(ProductWarehouse.toDTO(productWarehouse, this, languageType)) : productWarehouse.getDiscountPercentage())
                .discountedPrice(discountedPrice)
                .build();
    }

    private void setTaxDetails(double taxAmount, TaxResponseDTO tax) {
        tax.setTaxAmount(taxAmount);
        tax.setTaxPercentage(!(String.valueOf((tax.getTaxPercentage())).trim().isEmpty()) ? tax.getTaxPercentage() + PERCENT_SIGN : null);
    }
    public static double calculateTaxAmount(double taxPercentage, String currentRate) {
        double taxAmount = (taxPercentage / DIVIDE_AMOUNT) * Double.parseDouble(currentRate);

        double scale = Math.pow(10, DECIMAL_PLACES);
        taxAmount = Math.round(taxAmount * scale) / scale;

        return taxAmount;
    }


    private double calculateDiscountedPrice(String currentRate, String specialRate) {
        return Double.parseDouble(specialRate) != 0.0 ? (Double.parseDouble(currentRate) - Double.parseDouble(specialRate)) : 0.0;
    }
    private String appendPercentSign(String percentage) {
        return percentage != null && !percentage.trim().isEmpty() ? percentage + PERCENT_SIGN : null;
    }

    private String getDiscountPercentage(ProductWarehouseResponseDTO productWarehouse) {
        double discountPercentage = 0.0;

        if (productWarehouse.getSpecialRate() != null && !productWarehouse.getSpecialRate().isEmpty() &&
                productWarehouse.getCurrentRate() != null && !productWarehouse.getCurrentRate().isEmpty()) {

            double currentRate = Double.parseDouble(productWarehouse.getCurrentRate());
            double specialRate = Double.parseDouble(productWarehouse.getSpecialRate());

            if (specialRate != 0.0) {
                discountPercentage = ((currentRate - specialRate) / currentRate) * 100;
            }
        }

        discountPercentage = Math.round(discountPercentage * 10.0) / 10.0;
        return appendPercentSign(String.valueOf(discountPercentage));
    }

    public ProductResponseDTO toProductDTO(ProductWarehouse productWarehouse, LanguageType languageType) {
        Product product = productWarehouse.getProduct();
        if (product == null) {
            return null;
        }
        return ProductResponseDTO.builder()
                .id(product.getId())
                .arabicTitle(product.getArabicTitle())
                .arabicDescription(product.getArabicDescription())
                .description(product.getDescription())
                .weight(product.getWeight())
                .sku(product.getSku())
                .title(product.getTitle())
                .isDiscounted(product.getIsDiscounted())
                .isFeatured(product.getIsFeatured())
                .isNewArrivals(product.getIsNewArrivals())
                .isRecommended(product.getIsRecommended())
                .isTrending(product.getIsTrending())
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .subCategoryId(product.getSubCategory() != null ? product.getSubCategory().getId() : null)
                .brandId(product.getBrand() != null ? product.getBrand().getId() : null)
                .brandName(product.getBrand() != null ? product.getBrand().getName() : null)
                .brandNameArabic(product.getBrand() != null ? product.getBrand().getArabicName() : null)
                .position(product.getPosition())
                .image(product.getImage())
                .isActive(product.getIsActive())
                .build();
    }
    public ProductWarehouseResponseDTO toProductWarehouseDTO(ProductWarehouse productWarehouse, LanguageType languageType) {
        return ProductWarehouseResponseDTO.builder()
                .id(productWarehouse.getId())
                .currentRate(productWarehouse.getCurrentRate())
                .inStock(productWarehouse.getInStock())
                .product(toProductDTO(productWarehouse, languageType))
                .rate(productWarehouse.getRate())
                .taxId(productWarehouse.getTax().getId())
                .quantityInStock(productWarehouse.getQuantityInStock())
                .warehouseId(productWarehouse.getWarehouse().getId())
                .specialRate(productWarehouse.getSpecialRate())
                .priceListId(productWarehouse.getPriceList().getId())
                .categoryId(productWarehouse.getCategory().getId())
                .discountPercentage(productWarehouse.getDiscountPercentage())
                .subCategoryId(productWarehouse.getSubCategory().getId())
                .isActive(productWarehouse.getIsActive())
                .build();
    }
    public List<ProductWarehouseResponseDTO> toProductWarehouseDTOList(List<ProductWarehouse> productWarehouse, LanguageType languageType) {
        return productWarehouse.stream()
                .map(pw -> toProductWarehouseDTO(pw, languageType))
                .collect(Collectors.toList());
    }
    public Product getProduct (Long productId, LanguageType languageType) {
        return productService.findById(productId, languageType);
    }

}
