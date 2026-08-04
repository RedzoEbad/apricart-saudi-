package com.apricart.consumer.service.Impl;

import com.apricart.consumer.enity.*;
import com.apricart.consumer.exceptions.ResourceNotFoundException;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.repository.jpa.ProductRepository;
import com.apricart.consumer.repository.jpa.ProductWarehouseRepository;
import com.apricart.consumer.security.dto.request.ProductRequestDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.*;
import com.apricart.consumer.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.*;
import static com.apricart.consumer.security.constants.ResponseMessage.*;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    protected static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ImageUtils imageUtils;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SubCategoryService subCategoryService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private ProductWarehouseService productWarehouseService;
    @Autowired
    private ProductWarehouseRepository productWarehouseRepository;
    private static final String PRODUCT_ENG = "Product";
    private static final String PRODUCT_ARB = "المنتج";

    @Override
    public void addProduct(ProductRequestDTO productRequestDTO, LanguageType languageType) {
        LOGGER.info("Adding product: {}", productRequestDTO.toString());
        Product product;
        product = Product.fromDTO(productRequestDTO);
        product.setCategory(categoryService.findById(productRequestDTO.getCategoryId(), languageType));
        product.setSubCategory(subCategoryService.findById(productRequestDTO.getSubCategoryId(), languageType));
        product.setBrand(brandService.findById(productRequestDTO.getBrandId(), languageType));
        save(product);
    }

    @Override
    public Product updateProduct(ProductRequestDTO productRequestDTO, LanguageType languageType) {
        LOGGER.info("Updating product: {}", productRequestDTO);
        Product existingProduct = findById(productRequestDTO.getId(),languageType);
        existingProduct.setTitle(productRequestDTO.getTitle() == null ? existingProduct.getTitle() : productRequestDTO.getTitle());
        existingProduct.setArabicTitle(productRequestDTO.getArabicTitle() == null ? existingProduct.getArabicTitle() : productRequestDTO.getArabicTitle());
        existingProduct.setImage(productRequestDTO.getImage() == null ? existingProduct.getImage() : productRequestDTO.getImage());
        existingProduct.setDescription(productRequestDTO.getDescription() == null ? existingProduct.getDescription() : productRequestDTO.getDescription());
        existingProduct.setArabicDescription(productRequestDTO.getArabicDescription() == null ? existingProduct.getArabicDescription() : productRequestDTO.getArabicDescription());
        existingProduct.setSku(productRequestDTO.getSku() == null ? existingProduct.getSku() : productRequestDTO.getSku());
        existingProduct.setWeight(productRequestDTO.getWeight() == null ? existingProduct.getWeight() : productRequestDTO.getWeight());
        existingProduct.setCategory(productRequestDTO.getCategoryId() == null ? existingProduct.getCategory() : categoryService.findById(productRequestDTO.getCategoryId(), languageType));
        existingProduct.setSubCategory(productRequestDTO.getSubCategoryId() == null ? existingProduct.getSubCategory() : subCategoryService.findById(productRequestDTO.getSubCategoryId(), languageType));
        existingProduct.setBrand(productRequestDTO.getBrandId() == null ? existingProduct.getBrand() : brandService.findById(productRequestDTO.getBrandId(), languageType));
        existingProduct.setPosition(productRequestDTO.getPosition() == null ? existingProduct.getPosition() : productRequestDTO.getPosition());
        existingProduct.setIsFeatured(productRequestDTO.getIsFeatured() == null ? existingProduct.getIsFeatured() : productRequestDTO.getIsFeatured());
        existingProduct.setIsNewArrivals(productRequestDTO.getIsNewArrivals() == null ? existingProduct.getIsNewArrivals() : productRequestDTO.getIsNewArrivals());
        existingProduct.setIsDiscounted(productRequestDTO.getIsDiscounted() == null ? existingProduct.getIsDiscounted() : productRequestDTO.getIsDiscounted());
        existingProduct.setZohoId(productRequestDTO.getZohoId() == null ? existingProduct.getZohoId() : productRequestDTO.getZohoId());
        existingProduct.setIsTrending(productRequestDTO.getIsTrending() == null ? existingProduct.getIsTrending() : productRequestDTO.getIsTrending());
        existingProduct.setIsRecommended(productRequestDTO.getIsRecommended() == null ? existingProduct.getIsRecommended() : productRequestDTO.getIsRecommended());
        existingProduct.setUpdateDateTime(LocalDateTime.now());
        save(existingProduct);

        LOGGER.info("Getting productWarehouse: {}", productRequestDTO.getId());

        ProductWarehouse productWarehouse = productWarehouseService.findProductWarehouseByProductId(productRequestDTO.getId(), languageType);
        productWarehouse.setCategory(productRequestDTO.getCategoryId() == null ? existingProduct.getCategory() : categoryService.findById(productRequestDTO.getCategoryId(), languageType));
        productWarehouse.setSubCategory(productRequestDTO.getSubCategoryId() == null ? existingProduct.getSubCategory() : subCategoryService.findById(productRequestDTO.getSubCategoryId(), languageType));
        productWarehouseRepository.save(productWarehouse);

        return existingProduct;
    }

    @Override
    public void deleteProduct(Long id, LanguageType languageType) {
        LOGGER.info("Deactivating product status for id: {}", id);
        Product existingProduct = findById(id, languageType);
        if (existingProduct.getIsActive()) {
            existingProduct.setIsActive(false);
            save(existingProduct);
        }
    }

    @Override
    public Page<Product> findAll(PageRequest pageRequest) {
        return productRepository.findAll(pageRequest);
    }

    @Override
    public List<Product> findAllProducts(LanguageType lang, int page, int size) {
        LOGGER.info("Getting all products");
        PageRequest pageRequest = PageRequest.of(page, size);
        List<Product> products = productRepository.findAll(pageRequest).stream()
                .peek(p -> {
                    if (p.getImage() != null) {
                        p.setImage(imageUtils.getImagePath(p.getImage()));
                    }
                })
                .sorted(Comparator.comparingLong(Product::getPosition))
                .collect(Collectors.toList());

        if (!products.isEmpty()) {
            return products;
        } else {
            LOGGER.error("No products found");
            throw new ResourceNotFoundException(LanguageType.ARB.equals(lang) ? PRODUCT_NOT_FOUND_ARABIC : PRODUCT_NOT_FOUND, true);
        }
    }


    @Override
    public Product findById(Long id, LanguageType languageType) {
        LOGGER.info("Finding product by id: {}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Product with id {} not found", id);
                    return LanguageType.ARB.equals(languageType) ? new ResourceNotFoundException(PRODUCT_ARB, id, true) : new ResourceNotFoundException(PRODUCT_ENG, id, false);
                });
    }

    @Override
    public Product findProductBySKU(String sku) {
       return productRepository.findProductBySku(sku);
    }

    @Override
    public List<Product> findByCategoryId(Long id, int page, int size, LanguageType languageType) {
        Category category = categoryService.findById(id, languageType);
        PageRequest pageRequest = PageRequest.of(page, size);
        return productRepository.findProductByCategory(category, pageRequest).getContent();
    }

    @Override
    public List<Product> findBySubCategoryId(Long id, int page, int size, LanguageType languageType) {
        SubCategory subCategory = subCategoryService.findById(id, languageType);
        PageRequest pageRequest = PageRequest.of(page, size);
        return productRepository.findProductBySubCategory(subCategory, pageRequest).getContent();
    }

    @Override
    public List<Product> findByBrandId(Long id, int page, int size, LanguageType languageType) {
        Brand brand = brandService.findById(id, languageType);
        PageRequest pageRequest = PageRequest.of(page, size);
        return productRepository.findProductByBrand(brand, pageRequest).getContent();
    }

    @Override
    public List<Product> findByZohoId(Long id, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return productRepository.findProductByZohoId(id, pageRequest).getContent();
    }


    @Override
    public List<Product> getTrendingProducts(LanguageType lang, int page, int size) {
        LOGGER.info("Getting trending products");
        PageRequest pageRequest = PageRequest.of(page, size);
        List<Product> products = productRepository.findAll(pageRequest).stream()
                .filter(Product::getIsTrending)
                .sorted(Comparator.comparingInt(Product::getPosition))
                .collect(Collectors.toList());

        if (!products.isEmpty()) {
            return products;
        } else {
            LOGGER.error("No trending product found");
            throw new ResourceNotFoundException(LanguageType.ARB.equals(lang) ? TRENDING_PRODUCT_NOT_FOUND_ARABIC : TRENDING_PRODUCT_NOT_FOUND, true);
        }
    }

    @Override
    public List<Product> getFeaturedProducts(LanguageType lang, int page, int size) {
        LOGGER.info("Getting featured products");
        PageRequest pageRequest = PageRequest.of(page, size);
        List<Product> products = productRepository.findAll(pageRequest).stream()
                .filter(Product::getIsFeatured)
                .sorted(Comparator.comparingInt(Product::getPosition))
                .collect(Collectors.toList());

        if (!products.isEmpty()) {
            return products;
        } else {
            LOGGER.error("No featured product found");
            throw new ResourceNotFoundException(LanguageType.ARB.equals(lang) ? FEATURED_PRODUCT_NOT_FOUND_ARABIC : FEATURED_PRODUCT_NOT_FOUND, true);
        }
    }

    @Override
    public List<Product> getDiscountedProducts(LanguageType lang, int pageNo, int pageSize) {
        LOGGER.info("Getting discounted products");
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        List<Product> products = productRepository.findProductByIsDiscounted(Boolean.TRUE, pageable).stream()
                .sorted(Comparator.comparingInt(Product::getPosition))
                .collect(Collectors.toList());

        if (!products.isEmpty()) {
            return products;
        } else {
            LOGGER.error("No discounted product found");
            throw new ResourceNotFoundException(LanguageType.ARB.equals(lang) ? DISCOUNTED_PRODUCT_NOT_FOUND_ARABIC : DISCOUNTED_PRODUCT_NOT_FOUND, true);
        }
    }
    @Override
    public List<Product> getNewArrivalsProducts(LanguageType lang, int page, int size) {
        LOGGER.info("Getting new arrivals products");
        PageRequest pageRequest = PageRequest.of(page, size);
        List<Product> products = productRepository.findAll(pageRequest).stream()
                .filter(Product::getIsNewArrivals)
                .sorted(Comparator.comparingInt(Product::getPosition))
                .collect(Collectors.toList());

        if (!products.isEmpty()) {
            return products;
        } else {
            LOGGER.error("No new arrival product found");
            throw new ResourceNotFoundException(LanguageType.ARB.equals(lang) ? NEW_ARRIVAL_PRODUCT_NOT_FOUND_ARABIC : NEW_ARRIVAL_PRODUCT_NOT_FOUND, true);
        }
    }

    @Override
    public Product updateProductStatusById(Long id, Boolean status, LanguageType languageType) {
        LOGGER.info("Updating product status for product id: {} to {}", id, status);
        Product existingProduct = findById(id, languageType);
        existingProduct.setIsActive(status == null ? existingProduct.getIsActive() : status);
        return save(existingProduct);
    }

    @Override
    public Product updateProductPosition(Long id, Integer position, LanguageType languageType) {
        LOGGER.info("Updating product position for product id: {} to {}", id, position);
        Product existingProduct = findById(id, languageType);
        existingProduct.setPosition(position == null ? existingProduct.getPosition() : position);
        return save(existingProduct);
    }

    @Override
    public String getProductImage(Long id, LanguageType languageType) {
        LOGGER.info("Getting product image for category id: {}", id);
        Product product = findById(id, languageType);
        return imageUtils.getImagePath(product.getImage());
    }

    public Product save(Product product) {
        LOGGER.info("Saving Product: {}", product.toString());
        return productRepository.save(product);
    }

    @Override
    public ResponseEntity<?> addOrUpdateProductImage(Long productId, MultipartFile image, LanguageType lang) {
        LOGGER.info("Adding or updating image for product with id: {}", productId);
        final long ALLOWED_FILE_SIZE = (long) (1.5 * 1024);

        String imageFullPath = "";
        LOGGER.info("Product Image - Name: {}, Type: {}", image.getOriginalFilename(), image.getContentType());

        try {
            if (image == null || image.isEmpty() || image.getOriginalFilename() == null) {
                return lang.equals(LanguageType.ARB) ? Response.notAcceptable(ERROR_IMAGE_FAILED_ARABIC.concat(ERROR_INVALID_FILE_TYPE_OR_SIZE_ARABIC)) : Response.notAcceptable(ERROR_IMAGE_FAILED.concat(ERROR_INVALID_FILE_TYPE_OR_SIZE));
            }

            String imageOriginalName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()).toLowerCase()).replace(" ", "");
            int lastDotIndex = imageOriginalName.lastIndexOf(".");
            if (lastDotIndex <= 0 || lastDotIndex == imageOriginalName.length() - 1) {
                return lang.equals(LanguageType.ARB) ? Response.notAcceptable(ERROR_IMAGE_FAILED_ARABIC.concat(ERROR_INVALID_FILE_TYPE_OR_SIZE_ARABIC)) : Response.notAcceptable(ERROR_IMAGE_FAILED.concat(ERROR_INVALID_FILE_TYPE_OR_SIZE));
            }

            String imageFileName = imageOriginalName.substring(0, lastDotIndex);
            String imageFileType = imageOriginalName.substring(lastDotIndex + 1).toUpperCase();
            double imageFileSize = (image.getSize() / 1024.0);

            LOGGER.info("File name: {}", imageOriginalName);
            LOGGER.info("File type: {}", imageFileType);

            if (!ImageUtils.isValidImageFileType(imageFileType) || imageFileSize > ALLOWED_FILE_SIZE) {
                LOGGER.error("Invalid file type or size for image");
                return lang.equals(LanguageType.ARB) ? Response.notAcceptable(ERROR_IMAGE_FAILED_ARABIC.concat(ERROR_INVALID_FILE_TYPE_OR_SIZE_ARABIC)) : Response.notAcceptable(ERROR_IMAGE_FAILED.concat(ERROR_INVALID_FILE_TYPE_OR_SIZE));
            }

            // Upload the image
            imageFullPath = imageUtils.upload(image, imageFileName, imageOriginalName, imageFileType);
            LOGGER.info("Category Image Full Paths: {}", imageFullPath);

            if (imageFullPath.isEmpty()) {
                LOGGER.error("Failed to upload image");
                return lang.equals(LanguageType.ARB) ? Response.error(ERROR_IMAGE_FAILED_ARABIC) : Response.error(ERROR_IMAGE_FAILED);
            }

            Product product = findById(productId, lang);
            product.setImage(imageFullPath);
            save(product);

            LOGGER.info("Image uploaded successfully");
            return lang.equals(LanguageType.ARB) ? Response.success(IMAGE_UPLOADED_SUCCESSFULLY_ARABIC) : Response.success(IMAGE_UPLOADED_SUCCESSFULLY);
        } catch (Exception e) {
            LOGGER.error("Error processing/updating image: {}", e.getMessage());
            return lang.equals(LanguageType.ARB) ? Response.error(ERROR_IMAGE_FAILED_ARABIC) : Response.error(ERROR_IMAGE_FAILED, e.getMessage());
        }
    }

    @Override
    public List<Product> findOrderedProducts(int page, int size) {
        LOGGER.info("Finding ordered products");
        PageRequest pageRequest = PageRequest.of(page, size);
        return productRepository.findOrderedProducts(pageRequest).getContent();
    }
}
