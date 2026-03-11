package com.apricart.consumer.controller;

import com.apricart.consumer.enity.ProductWarehouse;
import com.apricart.consumer.generic.GenericResponse;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.mapper.ProductMapper;
import com.apricart.consumer.security.dto.request.ProductWarehouseRequestDTO;
import com.apricart.consumer.security.dto.response.ProductWarehouseResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.ProductWarehouseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.PRODUCTS_WAREHOUSE_REMOVED_SUCCESSFULLY_ARABIC;
import static com.apricart.consumer.security.constants.ResponseMessage.PRODUCTS_WAREHOUSE_REMOVED_SUCCESSFULLY;

@RestController
@RequestMapping("/v1/productswarehouse")
@Api(value = "Product Warehouse Controller", tags = {"ProductWarehouse"})
public class ProductWarehouseController{

    @Autowired
    private ProductWarehouseService productWarehouseService;

    @Autowired
    private ProductMapper productMapper;


    @ApiOperation(value = "Get all Products Warehouse", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping
    public ResponseEntity<GenericResponse<List<ProductWarehouseResponseDTO>>> getAllProductsWarehouse(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                                      @RequestHeader("Language") LanguageType lang) {
        List<ProductWarehouseResponseDTO> productsWarehouse = productMapper.toProductWarehouseDTOList(productWarehouseService.getAllProductsWarehouse(), lang);
        return !productsWarehouse.isEmpty() ? Response.success(productsWarehouse) : Response.notFound();
    }

    @ApiOperation(value = "Get Products Warehouse by Id", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<ProductWarehouseResponseDTO>> findProductWarehouseById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                                 @RequestHeader("Language") LanguageType lang, @PathVariable Long id) {
        ProductWarehouse productWarehouse = productWarehouseService.findById(id, lang);
        return productWarehouse != null ? Response.success(ProductWarehouse.toDTO(productWarehouse, productMapper, lang)) : Response.notFound();
    }

    @ApiOperation(value = "Add Product Warehouse", authorizations = { @Authorization(value="jwtToken") })
    @PostMapping
    public ResponseEntity<GenericResponse<ProductWarehouseResponseDTO>> addProductWarehouse(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                 @Valid @RequestBody ProductWarehouseRequestDTO productWarehouseRequestDTO, @RequestHeader("Language") LanguageType lang) {
        productWarehouseService.addProductWarehouse(productWarehouseRequestDTO, lang);
        return Response.created();
    }

    @ApiOperation(value = "Update Product Warehouse", authorizations = { @Authorization(value="jwtToken") })
    @PutMapping
    public ResponseEntity<GenericResponse<ProductWarehouseResponseDTO>> updateProductWarehouse(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                               @RequestHeader("Language") LanguageType lang, @Valid @RequestBody ProductWarehouseRequestDTO productWarehouseRequestDTO) {
        ProductWarehouse updateProductWarehouse = productWarehouseService.updateProductWarehouse(productWarehouseRequestDTO, lang);
        return updateProductWarehouse != null ? Response.success(ProductWarehouse.toDTO(updateProductWarehouse, productMapper, lang)) : Response.notFound();
    }
    @ApiOperation(value = "Delete Product Warehouse By Id", authorizations = { @Authorization(value="jwtToken") })
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<String>> deleteBrand(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                               @RequestHeader("Language") LanguageType lang, @PathVariable Long id) {
        productWarehouseService.deleteProductWarehouse(id, lang);
        return lang.equals(LanguageType.ARB) ? Response.success(PRODUCTS_WAREHOUSE_REMOVED_SUCCESSFULLY_ARABIC) : Response.success(PRODUCTS_WAREHOUSE_REMOVED_SUCCESSFULLY);
    }

    @ApiOperation(value = "Get Products Warehouse by Product Id", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping("/product/{id}")
    public ResponseEntity<GenericResponse<ProductWarehouseResponseDTO>> findProductWarehouseByProductId(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                                              @RequestHeader("Language") LanguageType lang, @PathVariable Long id) {
        ProductWarehouseResponseDTO productsWarehouse = productWarehouseService.findByProductId(id, lang);
        return productsWarehouse != null ? Response.success(productsWarehouse) : Response.notFound();
    }

    @ApiOperation(value = "Get Products Warehouse by Warehouse Id", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping("/warehouse/{id}")
    public ResponseEntity<GenericResponse<List<ProductWarehouseResponseDTO>>> findProductWarehouseByWarehouseId(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                                                @RequestHeader("Language") LanguageType lang, @PathVariable Long id) {
        List<ProductWarehouseResponseDTO> productsWarehouse = productMapper.toProductWarehouseDTOList(productWarehouseService.findByWarehouseId(id, lang), lang);
        return !productsWarehouse.isEmpty() ? Response.success(productsWarehouse) : Response.notFound();
    }

    @ApiOperation(value = "Get Products Warehouse by Price list Id", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping("/pricelist/{id}")
    public ResponseEntity<GenericResponse<List<ProductWarehouseResponseDTO>>> findProductWarehouseByPriceListId(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                                                @RequestHeader("Language") LanguageType lang, @PathVariable Long id) {
        List<ProductWarehouseResponseDTO> productsWarehouse = productMapper.toProductWarehouseDTOList(productWarehouseService.findByPriceListId(id, lang), lang);
        return !productsWarehouse.isEmpty() ? Response.success(productsWarehouse) : Response.notFound();
    }

    @ApiOperation(value = "Update Product Warehouse Status By Id", authorizations = { @Authorization(value="jwtToken") })
    @PutMapping("/{id}/{status}")
    public ResponseEntity<GenericResponse<ProductWarehouseResponseDTO>> updateWarehouseStatusById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                                  @RequestHeader("Language") LanguageType lang, @PathVariable Long id, @PathVariable boolean status) {
        ProductWarehouse updatedWarehouse = productWarehouseService.updateProductWarehouseStatusById(id, status, lang);
        return updatedWarehouse != null ? Response.success(ProductWarehouse.toDTO(updatedWarehouse, productMapper, lang)) : Response.notFound();
    }
    @ApiOperation(value = "Update Product Warehouse Quantity", authorizations = { @Authorization(value="jwtToken") })
    @PutMapping("/quantity")
    public ResponseEntity<GenericResponse<ProductWarehouseResponseDTO>> updateProductWarehouseQuantity(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                               @RequestHeader("Language") LanguageType lang, @RequestParam Long warehouseId, @RequestParam String sku, @RequestParam int quantity) {
        ProductWarehouse updateProductWarehouse = productWarehouseService.updateQuantityByWarehouseAndSku(sku, warehouseId, quantity, lang);
        return updateProductWarehouse != null ? Response.success(ProductWarehouse.toDTO(updateProductWarehouse, productMapper, lang)) : Response.notFound();
    }

}

