package com.apricart.consumer.controller;

import com.apricart.consumer.enity.Product;
import com.apricart.consumer.enity.SubCategory;
import com.apricart.consumer.generic.GenericResponse;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.security.dto.request.ProductRequestDTO;
import com.apricart.consumer.security.dto.response.ProductResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.CategoryService;
import com.apricart.consumer.service.ProductService;
import com.apricart.consumer.service.SubCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.PRODUCT_REMOVED_SUCCESSFULLY_ARABIC;
import static com.apricart.consumer.security.constants.ResponseMessage.PRODUCT_REMOVED_SUCCESSFULLY;

@RestController
@RequestMapping("/v1/products")
@Api(value = "Product Controller", tags = {"Product"})
public class ProductController {
    protected static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SubCategoryService subCategoryService;

    @ApiOperation(value = "Get all products", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping
    public ResponseEntity<GenericResponse<List<ProductResponseDTO>>> getAllProducts(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                    @RequestHeader("Language") LanguageType lang,
                                                                                    @RequestParam(defaultValue = "0", required = false) int pageNo,
                                                                                    @RequestParam(defaultValue = "10", required = false) int pageSize) {
        List<ProductResponseDTO> products = Product.toDTOList(productService.findAllProducts(lang, pageNo, pageSize));
        return !products.isEmpty() ? Response.success(products) : Response.notFound();
    }

    @ApiOperation(value = "Get products by Category Id", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping("/category/{id}")
    public ResponseEntity<GenericResponse<List<?>>> findProductsByCategoryId(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                             @RequestHeader("Language") LanguageType lang,
                                                                             @PathVariable Long id,
                                                                             @RequestParam(defaultValue = "0", required = false) int pageNo,
                                                                             @RequestParam(defaultValue = "10", required = false) int pageSize) {

//        if (isDiscountedCategory(id, lang)) {
//            return getDiscountedProductsResponse(lang, pageNo, pageSize);
//        } else {
            return getProductsResponseCategory(id, pageNo, pageSize, lang);
//        }
    }

    @ApiOperation(value = "Get products by Zoho Id", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping("/zoho/{id}")
    public ResponseEntity<GenericResponse<List<ProductResponseDTO>>> findProductsByZohoId(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                          @RequestHeader("Language") LanguageType lang,
                                                                                          @PathVariable Long id,
                                                                                          @RequestParam(defaultValue = "0", required = false) int pageNo,
                                                                                          @RequestParam(defaultValue = "10", required = false) int pageSize) {
        List<ProductResponseDTO> products = Product.toDTOList(productService.findByZohoId(id, pageNo, pageSize));
        return !products.isEmpty() ? Response.success(products) : Response.notFound();
    }

    @ApiOperation(value = "Get products by Sub Category Id", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping("/subcategory/{id}")
    public ResponseEntity<GenericResponse<List<?>>> findProductsBySubCategoryId(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                @RequestHeader("Language") LanguageType lang,
                                                                                @PathVariable Long id,
                                                                                @RequestParam(defaultValue = "0", required = false) int pageNo,
                                                                                @RequestParam(defaultValue = "10", required = false) int pageSize) {
        SubCategory subCategory = subCategoryService.findById(id, lang);
//        if (isDiscountedCategory(subCategory.getCategory().getId(), lang)) {
//            return getDiscountedProductsResponse(lang, pageNo, pageSize);
//        } else {
            return getProductsResponseSubCategory(id, pageNo, pageSize, lang);
//        }
    }

    @ApiOperation(value = "Get products by Brand Id", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping("/brands/{id}")
    public ResponseEntity<GenericResponse<List<ProductResponseDTO>>> findProductsByBrandId(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                           @RequestHeader("Language") LanguageType lang,
                                                                                           @PathVariable Long id,
                                                                                           @RequestParam(defaultValue = "0", required = false) int pageNo,
                                                                                           @RequestParam(defaultValue = "10", required = false) int pageSize) {
        List<ProductResponseDTO> products = Product.toDTOList(productService.findByBrandId(id, pageNo, pageSize, lang));
        return !products.isEmpty() ? Response.success(products) : Response.notFound();
    }

    @ApiOperation(value = "Get product image by Id", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping("/image/{id}")
    public ResponseEntity<GenericResponse<String>> getProductImageById(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable Long id,
            @RequestHeader("Language") LanguageType lang) {
        String productImage = productService.getProductImage(id, lang);
        return !productImage.isEmpty() ? Response.success(productImage) : Response.notFound();
    }

    @ApiOperation(value = "Get Trending Products", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping("/trending")
    public ResponseEntity<GenericResponse<List<ProductResponseDTO>>> findTrendingProducts(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                          @RequestHeader("Language") LanguageType lang,
                                                                                          @RequestParam(defaultValue = "0", required = false) int pageNo,
                                                                                          @RequestParam(defaultValue = "10", required = false) int pageSize) {
        List<ProductResponseDTO> products = Product.toDTOList(productService.getTrendingProducts(lang, pageNo, pageSize));
        return !products.isEmpty() ? Response.success(products) : Response.notFound();
    }

    @ApiOperation(value = "Get Discounted Products", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping("/discounted")
    public ResponseEntity<GenericResponse<List<ProductResponseDTO>>> findDiscountedProducts(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                            @RequestHeader("Language") LanguageType lang,
                                                                                            @RequestParam(defaultValue = "0", required = false) int pageNo,
                                                                                            @RequestParam(defaultValue = "10", required = false) int pageSize) {
        List<ProductResponseDTO> products = Product.toDTOList(productService.getDiscountedProducts(lang, pageNo, pageSize));
        return !products.isEmpty() ? Response.success(products) : Response.notFound();
    }

    @ApiOperation(value = "Get New Arrival Products", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping("/newarrivals")
    public ResponseEntity<GenericResponse<List<ProductResponseDTO>>> findNewArrivalsProducts(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                             @RequestHeader("Language") LanguageType lang,
                                                                                             @RequestParam(defaultValue = "0", required = false) int pageNo,
                                                                                             @RequestParam(defaultValue = "10", required = false) int pageSize) {
        List<ProductResponseDTO> products = Product.toDTOList(productService.getNewArrivalsProducts(lang, pageNo, pageSize));
        return !products.isEmpty() ? Response.success(products) : Response.notFound();
    }

    @ApiOperation(value = "Get Featured Products", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping("/featured")
    public ResponseEntity<GenericResponse<List<ProductResponseDTO>>> findFeaturedProducts(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                          @RequestHeader("Language") LanguageType lang,
                                                                                          @RequestParam(defaultValue = "0", required = false) int pageNo,
                                                                                          @RequestParam(defaultValue = "10", required = false) int pageSize) {
        List<ProductResponseDTO> products = Product.toDTOList(productService.getFeaturedProducts(lang, pageNo, pageSize));
        return !products.isEmpty() ? Response.success(products) : Response.notFound();
    }

    @ApiOperation(value = "Get product by Id", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<ProductResponseDTO>> findProductById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                               @RequestHeader("Language") LanguageType lang, @PathVariable Long id) {
        Product product = productService.findById(id, lang);
        return product != null ? Response.success(Product.toDTO(product)) : Response.notFound();
    }
    @ApiOperation(value = "Get product by SKU", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping("/sku/{sku}")
    public ResponseEntity<GenericResponse<ProductResponseDTO>> findProductBySKU(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                               @RequestHeader("Language") LanguageType lang, @PathVariable String sku) {
        Product product = productService.findProductBySKU(sku);
        return product != null ? Response.success(Product.toDTO(product)) : Response.notFound();
    }

    @ApiOperation(value = "Add Product", authorizations = {@Authorization(value = "jwtToken")})
    @PostMapping
    public ResponseEntity<GenericResponse<ProductResponseDTO>> addProduct(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                          @Valid @RequestBody ProductRequestDTO productRequestDTO, @RequestHeader("Language") LanguageType lang) {
        productService.addProduct(productRequestDTO, lang);
        return Response.created();
    }

    @ApiOperation(value = "Update Product", authorizations = {@Authorization(value = "jwtToken")})
    @PutMapping
    public ResponseEntity<GenericResponse<ProductResponseDTO>> updateProduct(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                             @RequestHeader("Language") LanguageType lang, @Valid @RequestBody ProductRequestDTO productRequestDTO) {
        Product updatedProduct = productService.updateProduct(productRequestDTO, lang);
        return updatedProduct != null ? Response.success(Product.toDTO(updatedProduct)) : Response.notFound();
    }

    @ApiOperation(value = "Update Product Position By Id", authorizations = {@Authorization(value = "jwtToken")})
    @PutMapping("/{id}/{position}")
    public ResponseEntity<GenericResponse<ProductResponseDTO>> updateProductPositionById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                         @PathVariable Long id, @PathVariable Integer position, @RequestHeader("Language") LanguageType lang) {
        Product updateProductPosition = productService.updateProductPosition(id, position, lang);
        return updateProductPosition != null ? Response.success(Product.toDTO(updateProductPosition)) : Response.notFound();
    }

    @ApiOperation(value = "Update Product Status By Id", authorizations = {@Authorization(value = "jwtToken")})
    @PutMapping("/status/{id}/{status}")
    public ResponseEntity<GenericResponse<ProductResponseDTO>> updateProductStatusById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                       @PathVariable Long id, @PathVariable Boolean status, @RequestHeader("Language") LanguageType lang) {
        Product updateProductStatus = productService.updateProductStatusById(id, status, lang);
        return updateProductStatus != null ? Response.success(Product.toDTO(updateProductStatus)) : Response.notFound();
    }

    @ApiOperation(value = "Delete Product By Id", authorizations = {@Authorization(value = "jwtToken")})
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<String>> deleteCategory(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                  @PathVariable Long id, @RequestHeader("Language") LanguageType lang) {
        productService.deleteProduct(id, lang);
        return lang.equals(LanguageType.ARB) ? Response.success(PRODUCT_REMOVED_SUCCESSFULLY_ARABIC) : Response.success(PRODUCT_REMOVED_SUCCESSFULLY);
    }

    @ApiOperation(value = "Product Image Update By Id", authorizations = {@Authorization(value = "jwtToken")})
    @PostMapping("/image/update/{id}")
    public ResponseEntity<?> updateProductImage(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @PathVariable Long id,
                                                @RequestParam(value = "image", required = true) MultipartFile image, @RequestHeader("Language") LanguageType lang) {
        LOGGER.info("Image: " + image.getSize());
        return productService.addOrUpdateProductImage(id, image, lang);
    }

    private boolean isDiscountedCategory(Long categoryId, LanguageType languageType) {
        return categoryService.checkIsDiscountedCategory(categoryId, languageType);
    }

    private ResponseEntity<GenericResponse<List<?>>> getDiscountedProductsResponse(LanguageType lang, Integer pageNo, Integer pageSize) {
        List<ProductResponseDTO> products = Product.toDTOList(productService.getDiscountedProducts(lang, pageNo, pageSize));
        return products.isEmpty() ? Response.notFound() : Response.success(products);
    }

    private ResponseEntity<GenericResponse<List<?>>> getProductsResponseCategory(Long categoryId, int pageNo, int pageSize, LanguageType languageType) {
        List<ProductResponseDTO> products = Product.toDTOList(productService.findByCategoryId(categoryId, pageNo, pageSize, languageType));
        return !products.isEmpty() ? Response.success(products) : Response.notFound();
    }

    private ResponseEntity<GenericResponse<List<?>>> getProductsResponseSubCategory(Long subCategoryId, int pageNo, int pageSize, LanguageType languageType) {
        List<ProductResponseDTO> products = Product.toDTOList(productService.findBySubCategoryId(subCategoryId, pageNo, pageSize, languageType));
        return !products.isEmpty() ? Response.success(products) : Response.notFound();
    }
}

