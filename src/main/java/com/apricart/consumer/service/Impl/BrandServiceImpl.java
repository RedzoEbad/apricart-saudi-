package com.apricart.consumer.service.Impl;

import com.apricart.consumer.enity.Brand;
import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.exceptions.ResourceNotFoundException;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.repository.jpa.BrandRepository;
import com.apricart.consumer.security.dto.request.BrandRequestDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.BrandService;
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
public class BrandServiceImpl implements BrandService {
    protected static final Logger LOGGER = LoggerFactory.getLogger(BrandServiceImpl.class);

    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private ImageUtils imageUtils;
    private static final String BRAND_ENG = "Brand";
    private static final String BRAND_ARB = "العلامة التجارية";

    @Override
    public List<Brand> getAllBrands(LanguageType lang) {
        LOGGER.info("Getting all brands");
        List<Brand> brands = brandRepository.findAll().stream()
                .peek(brand -> {
                    if (brand.getImage() != null) {
                        brand.setImage(imageUtils.getImagePath(brand.getImage()));
                    }
                })
                .sorted(Comparator.comparingLong(Brand::getId))
                .collect(Collectors.toList());

        if (!brands.isEmpty()) {
            return brands;
        } else {
            LOGGER.error("No brands found");
            throw new ResourceNotFoundException(LanguageType.ARB.equals(lang) ? BRAND_NOT_FOUND_ARABIC : BRAND_NOT_FOUND, true);
        }
    }

    @Override
    public Brand findById(Long id, LanguageType lang) {
        LOGGER.info("Finding brand by id: {}", id);
        return brandRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Brand with id {} not found", id);
                    return LanguageType.ARB.equals(lang) ? new ResourceNotFoundException(BRAND_ARB, id, true) : new ResourceNotFoundException(BRAND_ENG, id, false);
                });
    }

    @Override
    public Brand findByName(String name) {
        LOGGER.info("Finding brand by name: {}", name);
        return brandRepository.findBrandByName(name);
    }

    @Override
    public Brand findByArabicName(String arabicName) {
        LOGGER.info("Finding brand by arabic name: {}", arabicName);
        return brandRepository.findBrandByArabicName(arabicName);
    }

    @Override
    public List<Brand> getActiveBrands(LanguageType lang) {
        LOGGER.info("Getting active brands");
        List<Brand> brands = brandRepository.findAll().stream()
                .filter(Brand::getStatus)
                .peek(brand -> {
                    if (brand.getImage() != null) {
                        brand.setImage(imageUtils.getImagePath(brand.getImage()));
                    }
                })
                .sorted(Comparator.comparingLong(Brand::getId))
                .collect(Collectors.toList());

        if (!brands.isEmpty()) {
            return brands;
        } else {
            LOGGER.error("No active brands found");
            throw new ResourceNotFoundException(LanguageType.ARB.equals(lang) ? BRAND_STATUS_NOT_ACTIVE_ARABIC : BRAND_STATUS_NOT_ACTIVE, true);
        }
    }

    @Override
    public String getBrandImage(Long id, LanguageType languageType) {
        LOGGER.info("Getting brand image for brand id: {}", id);
        Brand brand = findById(id, languageType);
        return imageUtils.getImagePath(brand.getImage());
    }

    @Override
    public void addBrand(BrandRequestDTO brandRequestDTO) {
        LOGGER.info("Adding brand: {}", brandRequestDTO);
        save(Brand.fromDTO(brandRequestDTO));
    }

    public Brand save(Brand brand) {
        LOGGER.info("Saving brand: {}", brand);
        return brandRepository.save(brand);
    }

    @Override
    public Brand updateBrand(BrandRequestDTO brandRequestDTO, LanguageType languageType) {
        LOGGER.info("Updating brand: {}", brandRequestDTO);
        Brand existingBrand = findById(brandRequestDTO.getId(), languageType);

        existingBrand.setName(brandRequestDTO.getName() == null ? existingBrand.getName() : brandRequestDTO.getName());
        existingBrand.setArabicName(brandRequestDTO.getArabicName() == null ? existingBrand.getArabicName() : brandRequestDTO.getArabicName());
        existingBrand.setImage(brandRequestDTO.getImage() == null ? existingBrand.getImage() : brandRequestDTO.getImage());
        existingBrand.setStatus(brandRequestDTO.getStatus() == null ? existingBrand.getStatus() : brandRequestDTO.getStatus());
        return save(existingBrand);
    }

    @Override
    public void deleteBrand(Long id, LanguageType languageType) {
        LOGGER.info("Deactivating brand status for id: {}", id);
        Brand existingBrand = findById(id, languageType);
        if(existingBrand.getStatus()) {
            existingBrand.setStatus(false);
            save(existingBrand);
        }
    }

    @Override
    public ResponseEntity<?> addOrUpdateBrandImage(Long brandId, MultipartFile image, Customer customer, LanguageType lang) {
        LOGGER.info("Adding or updating image for brand with id: {}", brandId);
        final long ALLOWED_FILE_SIZE = (long) (1.5 * 1024);

        String imageFullPath = "";
        LOGGER.info("Category Image - Name: {}, Type: {}", image.getOriginalFilename(), image.getContentType());

        try {
            String imageOriginalName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()).toLowerCase()).replace(" ", "");
            String imageFileName = imageOriginalName.substring(0, imageOriginalName.lastIndexOf("."));
            String imageFileType = imageOriginalName.substring(imageOriginalName.lastIndexOf(".") + 1).toUpperCase();
            double imageFileSize = (image.getSize() / 1024.0);

            LOGGER.info("File name: {}", imageOriginalName);
            LOGGER.info("File type: {}", imageFileType);

            if (!ImageUtils.isValidImageFileType(imageFileType) || imageFileSize > ALLOWED_FILE_SIZE) {
                LOGGER.error("Invalid file type or size for image");
                return lang.equals(LanguageType.ARB) ?  Response.notAcceptable(ERROR_IMAGE_FAILED_ARABIC.concat(ERROR_INVALID_FILE_TYPE_OR_SIZE_ARABIC)) : Response.notAcceptable(ERROR_IMAGE_FAILED.concat(ERROR_INVALID_FILE_TYPE_OR_SIZE));
            }

            // Upload the image
            imageFullPath = imageUtils.upload(image, imageFileName, imageOriginalName, imageFileType);
            LOGGER.info("Category Image Full Paths: {}", imageFullPath);

            if (imageFullPath.isEmpty()) {
                LOGGER.error("Failed to upload image");
                return lang.equals(LanguageType.ARB) ?  Response.error(ERROR_IMAGE_FAILED_ARABIC) : Response.error(ERROR_IMAGE_FAILED);
            }

            Brand brand = findById(brandId, lang);
            brand.setImage(imageFullPath);
            save(brand);

            LOGGER.info("Image uploaded successfully");
            return lang.equals(LanguageType.ARB) ?  Response.success(IMAGE_UPLOADED_SUCCESSFULLY_ARABIC) : Response.success(IMAGE_UPLOADED_SUCCESSFULLY);
        } catch (Exception e) {
            LOGGER.error("Error processing/updating image: {}", e.getMessage());
            return lang.equals(LanguageType.ARB) ?  Response.error(ERROR_IMAGE_FAILED_ARABIC) : Response.error(ERROR_IMAGE_FAILED, e.getMessage());
        }
    }
}
