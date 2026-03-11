package com.apricart.consumer.service;

import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.enity.MissingProduct;
import com.apricart.consumer.security.dto.dto.MissingProductDTO;
import com.apricart.consumer.security.dto.request.MissingProductRequestDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.security.enums.StatusType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MissingProductService {
    void addProductRequest(MissingProductRequestDTO dto, LanguageType lang);
    MissingProductDTO updateProductStatus(StatusType statusType, Long productRequestId, LanguageType lang);
    MissingProduct findById(Long id, LanguageType languageType);
    List<MissingProductDTO> getByCustomerId(Long customerId);
    ResponseEntity<?> addOrUpdateMissingProductImage(Long missingProductId, MultipartFile image, Customer customer, LanguageType lang);

}