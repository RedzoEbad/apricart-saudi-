package com.apricart.consumer.service.Impl;

import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.enity.FeedBack;
import com.apricart.consumer.enity.MissingProduct;
import com.apricart.consumer.exceptions.ResourceNotFoundException;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.repository.jpa.MissingProductRepository;
import com.apricart.consumer.security.dto.dto.MissingProductDTO;
import com.apricart.consumer.security.dto.request.MissingProductRequestDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.security.enums.StatusType;
import com.apricart.consumer.service.MissingProductService;
import com.apricart.consumer.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.*;
import static com.apricart.consumer.security.constants.ResponseMessage.*;

@Service
@Transactional
public class MissingProductServiceImpl implements MissingProductService {

    protected static final Logger LOGGER = LoggerFactory.getLogger(MissingProductServiceImpl.class);

    @Autowired
    private MissingProductRepository missingProductRepository;

    @Autowired
    private ImageUtils imageUtils;

    @Override
    public void addProductRequest(MissingProductRequestDTO dto, LanguageType languageType) {
        LOGGER.info("Adding missing product: {}", dto);
        MissingProduct missingProduct = MissingProduct.fromDTO(dto);
        if (dto.getProductStatus() == null) {
            missingProduct.setProductStatus(StatusType.OPEN);
        }
        save(missingProduct);
    }

    @Override
    public MissingProductDTO updateProductStatus(StatusType statusType, Long productRequestId, LanguageType lang) {
        MissingProduct existingProductRequest = findById(productRequestId, lang);
        existingProductRequest.setProductStatus(statusType != null ? statusType : existingProductRequest.getProductStatus());
        return save(existingProductRequest);
    }

    public MissingProductDTO save(MissingProduct missingProduct) {
        LOGGER.info("Adding missing product: {}", missingProduct);
        return MissingProduct.toDTO(missingProductRepository.save(missingProduct));
    }

    @Override
    public MissingProduct findById(Long id, LanguageType languageType) {
        LOGGER.info("Finding customer address by id: {}", id);
        return missingProductRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Customer address with id {} not found", id);
                    return new ResourceNotFoundException(LanguageType.ARB.equals(languageType) ? PRODUCT_NOT_FOUND_ARABIC : PRODUCT_NOT_FOUND, true);
                });
    }

    @Override
    public List<MissingProductDTO> getByCustomerId(Long customerId) {
        List<MissingProduct> missingProducts = missingProductRepository.findByCustomerId(customerId);
        return missingProducts.stream()
                .map(MissingProduct::toDTO)
                .sorted(Comparator.comparingLong(MissingProductDTO::getId))
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<?> addOrUpdateMissingProductImage(Long missingProductId, MultipartFile image, Customer customer, LanguageType lang) {
        LOGGER.info("Adding or updating image for missing product with id: {}", missingProductId);
        final long ALLOWED_FILE_SIZE = (long) (1.5 * 1024);

        String imageFullPath = "";
        LOGGER.info("Missing Product Image - Name: {}, Type: {}", image.getOriginalFilename(), image.getContentType());

        try {
            String imageOriginalName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()).toLowerCase()).replace(" ", "");
            String imageFileName = imageOriginalName.substring(0, imageOriginalName.lastIndexOf("."));
            String imageFileType = imageOriginalName.substring(imageOriginalName.lastIndexOf(".") + 1).toUpperCase();
            double imageFileSize = (image.getSize() / 1024.0);

            LOGGER.info("File name: {}", imageOriginalName);
            LOGGER.info("File type: {}", imageFileType);

            if (!ImageUtils.isValidImageFileType(imageFileType) || imageFileSize > ALLOWED_FILE_SIZE) {
                LOGGER.error("Invalid file type or size for image");
                return lang.equals(LanguageType.ARB) ? Response.notAcceptable(ERROR_IMAGE_FAILED_ARABIC.concat(ERROR_INVALID_FILE_TYPE_OR_SIZE_ARABIC)) : Response.notAcceptable(ERROR_IMAGE_FAILED.concat(ERROR_INVALID_FILE_TYPE_OR_SIZE));
            }

            // Upload the image
            imageFullPath = imageUtils.upload(image, imageFileName, imageOriginalName, imageFileType);
            LOGGER.info("Missing Product Image Full Paths: {}", imageFullPath);

            if (imageFullPath.isEmpty()) {
                LOGGER.error("Failed to upload image");
                return lang.equals(LanguageType.ARB) ? Response.error(ERROR_IMAGE_FAILED_ARABIC) : Response.error(ERROR_IMAGE_FAILED);
            }

            MissingProduct missingProduct = findById(missingProductId, lang);
            missingProduct.setImage(imageFullPath);
            missingProductRepository.save(missingProduct);

            LOGGER.info("Image uploaded successfully");
            return lang.equals(LanguageType.ARB) ? Response.success(IMAGE_UPLOADED_SUCCESSFULLY_ARABIC) : Response.success(IMAGE_UPLOADED_SUCCESSFULLY);
        } catch (Exception e) {
            LOGGER.error("Error processing/updating image: {}", e.getMessage());
            return lang.equals(LanguageType.ARB) ? Response.error(ERROR_IMAGE_FAILED_ARABIC) : Response.error(ERROR_IMAGE_FAILED, e.getMessage());
        }
    }
}
