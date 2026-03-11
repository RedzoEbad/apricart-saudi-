package com.apricart.consumer.controller;

import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.enity.MissingProduct;
import com.apricart.consumer.generic.GenericResponse;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.security.dto.dto.MissingProductDTO;
import com.apricart.consumer.security.dto.request.MissingProductRequestDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.security.enums.StatusType;
import com.apricart.consumer.service.BaseService;
import com.apricart.consumer.service.MissingProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.MISSING_PRODUCT_SUCCESS_MESSAGE_ARABIC;
import static com.apricart.consumer.security.constants.ResponseMessage.MISSING_PRODUCT_SUCCESS_MESSAGE;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/missing/product")
@Api(value = "Missing Product Controller", tags = {"Missing Product"})
public class MissingProductController {
    protected static final Logger LOGGER = LoggerFactory.getLogger(MissingProductController.class);

    @Autowired
    MissingProductService missingProductService;
    @Autowired
    BaseService baseService;

    @ApiOperation(value = "Save Missing Product", authorizations = {@Authorization(value = "jwtToken")})
    @PostMapping
    public ResponseEntity<?> saveMissingProduct(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestParam(value = "name", required = true) String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "quantity", required = false) Integer quantity,
            @RequestPart(value = "image", required = false) MultipartFile image,
            HttpServletRequest request,
            @RequestHeader("Language") LanguageType lang) throws IOException {

        Customer customer = baseService.resolveUser(request);
        MissingProductRequestDTO missingProduct = MissingProductRequestDTO.builder().name(name).description(description)
                .quantity(quantity).language(lang).customerId(customer.getId()).build();

        missingProductService.addProductRequest(missingProduct, lang);

        if (image != null && !image.isEmpty()) {
            LOGGER.info("Image: {}", image.getSize());
            missingProductService.addOrUpdateMissingProductImage(missingProduct.getId(), image, customer, lang);
        }

        return lang.equals(LanguageType.ARB) ? Response.success(MISSING_PRODUCT_SUCCESS_MESSAGE_ARABIC) : Response.created(MISSING_PRODUCT_SUCCESS_MESSAGE);
    }


    @ApiOperation(value = "Get Missing Product By Id", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<MissingProductDTO>> getMissingProductById(@PathVariable Long id,
                                                                                    @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                    @RequestHeader("Language") LanguageType lang) {

        MissingProductDTO product = MissingProduct.toDTO(missingProductService.findById(id, lang));
        return product != null ? Response.success(product) : Response.notFound();
    }

    @ApiOperation(value = "Get Missing Product By Customer Id", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping("/customer")
    public ResponseEntity<GenericResponse<List<MissingProductDTO>>> getMissingProductsByCustomerId(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                                   HttpServletRequest request,
                                                                                                   @RequestHeader("Language") LanguageType lang) {
        Customer customer = baseService.resolveUser(request);
        List<MissingProductDTO> missingProducts = missingProductService.getByCustomerId(customer.getId());
        return Response.success(missingProducts);
    }

    @ApiOperation(value = "Missing Product Image Update By Id", authorizations = {@Authorization(value = "jwtToken")})
    @PostMapping("/image/update/{id}")
    public ResponseEntity<?> updateMissingProductImage(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                       @PathVariable Long id,
                                                       @RequestParam(value = "image", required = true) MultipartFile image,
                                                       HttpServletRequest request,
                                                       @RequestHeader("Language") LanguageType lang) {

        return missingProductService.addOrUpdateMissingProductImage(id, image, baseService.resolveUser(request), lang);
    }

    @ApiOperation(value = "Update Product Request Status")
    @PutMapping("/status/{product_id}")
    public ResponseEntity<GenericResponse<MissingProductDTO>> updateProductStatus(
            @PathVariable("product_id") Long productId,
            @RequestParam StatusType statusType,
            @RequestHeader("Language") LanguageType lang) {
        MissingProductDTO missingProductDTO = missingProductService.updateProductStatus(statusType, productId, lang);
        return missingProductDTO != null ? Response.created(missingProductDTO) : Response.notFound();
    }
}