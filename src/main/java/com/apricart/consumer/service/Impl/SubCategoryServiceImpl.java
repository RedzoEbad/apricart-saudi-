package com.apricart.consumer.service.Impl;

import com.apricart.consumer.enity.Category;
import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.enity.SubCategory;
import com.apricart.consumer.exceptions.DuplicateResourceException;
import com.apricart.consumer.exceptions.ResourceNotFoundException;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.repository.jpa.SubCategoryRepository;
import com.apricart.consumer.security.dto.request.SubCategoryRequestDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.security.enums.LevelType;
import com.apricart.consumer.service.CategoryService;
import com.apricart.consumer.service.SubCategoryService;
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
public class SubCategoryServiceImpl implements SubCategoryService {
    protected static final Logger LOGGER = LoggerFactory.getLogger(SubCategoryServiceImpl.class);

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ImageUtils imageUtils;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    private static final String SUB_CATEGORY_ENG = "SubCategory";
    private static final String SUB_CATEGORY_ARB = "الإعدادات";

    @Override
    public List<SubCategory> getAllSubCategories(LanguageType languageType) {
        LOGGER.info("Retrieving all sub categories");
        List<SubCategory> categories = subCategoryRepository.findAll().stream()
                .peek(c -> {
                    if (c.getImage() != null) {
                        c.setImage(imageUtils.getImagePath(c.getImage()));
                    }
                })
                .sorted(Comparator.comparingLong(SubCategory::getPosition))
                .collect(Collectors.toList());

        if (!categories.isEmpty()) {
            return categories;
        } else {
            LOGGER.error("No sub categories found");
            throw LanguageType.ARB.equals(languageType) ? new ResourceNotFoundException(SUB_CATEGORY_NOT_FOUND_ARABIC, true) : new ResourceNotFoundException(SUB_CATEGORY_NOT_FOUND, true);
        }
    }

    @Override
    public SubCategory findById(Long id, LanguageType languageType) {
        LOGGER.info("Finding sub category by id: {}", id);
        return subCategoryRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Sub category with id {} not found", id);
                    return LanguageType.ARB.equals(languageType) ? new ResourceNotFoundException(SUB_CATEGORY_NOT_FOUND_ARABIC, true) : new ResourceNotFoundException(SUB_CATEGORY_NOT_FOUND, true);
                });
    }

    @Override
    public SubCategory findByName(String name) {
        LOGGER.info("Finding sub category by name: {}", name);
        return subCategoryRepository.findSubCategoryByName(name);
    }

    @Override
    public List<SubCategory> findByCategoryId(Long id, LanguageType languageType, Long warehouseId) {
        LOGGER.info("Finding sub categories by category id: {}", id);
        Category category = categoryService.findById(id, languageType);
//        return category.getIsDiscountedCategory() ? subCategoryRepository.findDiscountedSubCategoriesByCategory(category) : subCategoryRepository.findSubCategoriesByCategory(category, warehouseId);
        return subCategoryRepository.findSubCategoriesByCategory(category, warehouseId);
    }

    @Override
    public List<SubCategory> findByLevel(LevelType level) {
        LOGGER.info("Finding sub categories by level: {}", level);
        return subCategoryRepository.findSubCategoryByLevel(level);
    }

    @Override
    public List<SubCategory> getActiveSubCategories(LanguageType languageType) {
        LOGGER.info("Retrieving active sub categories");
        List<SubCategory> categories = subCategoryRepository.findAll().stream()
                .filter(SubCategory::getStatus)
                .peek(c -> {
                    if (c.getImage() != null) {
                        c.setImage(imageUtils.getImagePath(c.getImage()));
                    }
                })
                .sorted(Comparator.comparingLong(SubCategory::getPosition))
                .collect(Collectors.toList());

        if (!categories.isEmpty()) {
            return categories;
        } else {
            LOGGER.error("No active sub categories found");
            throw LanguageType.ARB.equals(languageType) ? new ResourceNotFoundException(SUB_CATEGORY_NOT_FOUND_ARABIC, true) : new ResourceNotFoundException(SUB_CATEGORY_NOT_FOUND, true);
        }
    }

    @Override
    public String getSubCategoryImage(Long id, LanguageType languageType) {
        LOGGER.info("Retrieving image for sub category with id: {}", id);
        SubCategory subCategory = subCategoryRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Sub category with id {} not found", id);
                    return LanguageType.ARB.equals(languageType) ? new ResourceNotFoundException(SUB_CATEGORY_ARB, id, true) : new ResourceNotFoundException(SUB_CATEGORY_ENG, id, false);
                });
        return imageUtils.getImagePath(subCategory.getImage());
    }

    @Override
    public SubCategory updateSubCategoryLevelById(Long id, LevelType level, LanguageType languageType) {
        LOGGER.info("Updating level for sub category with id: {} to level: {}", id, level);
        SubCategory existingCategory = findById(id, languageType);
        existingCategory.setLevel(level == null ? existingCategory.getLevel() : level);
        return save(existingCategory);
    }

    @Override
    public SubCategory updateSubCategoryPosition(Long id, Integer position, LanguageType languageType) {
        LOGGER.info("Updating position for sub category with id: {} to position: {}", id, position);
        SubCategory existingCategory = findById(id, languageType);
        existingCategory.setPosition(position == null ? existingCategory.getPosition() : position);
        return save(existingCategory);
    }

    @Override
    public void addSubCategory(SubCategoryRequestDTO categoryRequestDTO, LanguageType languageType) {
        LOGGER.info("Adding sub category: {}", categoryRequestDTO);
        Category category = categoryService.findById(categoryRequestDTO.getCategoryId(), languageType);
        validateSubCategoryNameUnique(categoryRequestDTO.getName(), categoryRequestDTO.getArabicName(), category, null, languageType);
        SubCategory subCategory = SubCategory.fromDTO(categoryRequestDTO);
        subCategory.setCategory(category);
        save(subCategory);
    }

    public SubCategory save(SubCategory category) {
        LOGGER.info("Saving sub category: {}", category);
        return subCategoryRepository.save(category);
    }

    @Override
    public SubCategory updateSubCategory(SubCategoryRequestDTO subCategoryRequestDTO, LanguageType languageType) {
        LOGGER.info("Updating sub category: {}", subCategoryRequestDTO);
        SubCategory existingSubCategory = findById(subCategoryRequestDTO.getId(), languageType);
        Category category = subCategoryRequestDTO.getCategoryId() == null
                ? existingSubCategory.getCategory()
                : categoryService.findById(subCategoryRequestDTO.getCategoryId(), languageType);

        String newName = subCategoryRequestDTO.getName() == null ? existingSubCategory.getName() : subCategoryRequestDTO.getName();
        String newArabicName = subCategoryRequestDTO.getArabicName() == null ? existingSubCategory.getArabicName() : subCategoryRequestDTO.getArabicName();
        validateSubCategoryNameUnique(newName, newArabicName, category, existingSubCategory.getId(), languageType);

        existingSubCategory.setName(newName);
        existingSubCategory.setArabicName(newArabicName);
        existingSubCategory.setImage(subCategoryRequestDTO.getImage() == null ? existingSubCategory.getImage() : subCategoryRequestDTO.getImage());
        existingSubCategory.setLevel(subCategoryRequestDTO.getLevel() == null ? existingSubCategory.getLevel() : subCategoryRequestDTO.getLevel());
        existingSubCategory.setPosition(subCategoryRequestDTO.getPosition() == null ? existingSubCategory.getPosition() : subCategoryRequestDTO.getPosition());
        existingSubCategory.setStatus(subCategoryRequestDTO.getStatus() == null ? existingSubCategory.getStatus() : subCategoryRequestDTO.getStatus());
        existingSubCategory.setCategory(category);
        return save(existingSubCategory);
    }

    private void validateSubCategoryNameUnique(String name, String arabicName, Category category,
                                               Long excludeId, LanguageType languageType) {
        boolean arabic = LanguageType.ARB.equals(languageType);
        if (name != null && !name.trim().isEmpty()) {
            boolean exists = excludeId == null
                    ? subCategoryRepository.existsByNameIgnoreCaseAndCategory(name.trim(), category)
                    : subCategoryRepository.existsByNameIgnoreCaseAndCategoryAndIdNot(name.trim(), category, excludeId);
            if (exists) {
                throw new DuplicateResourceException(arabic ? SUB_CATEGORY_NAME_EXISTS_ARABIC : SUB_CATEGORY_NAME_EXISTS);
            }
        }
        if (arabicName != null && !arabicName.trim().isEmpty()) {
            boolean exists = excludeId == null
                    ? subCategoryRepository.existsByArabicNameIgnoreCaseAndCategory(arabicName.trim(), category)
                    : subCategoryRepository.existsByArabicNameIgnoreCaseAndCategoryAndIdNot(arabicName.trim(), category, excludeId);
            if (exists) {
                throw new DuplicateResourceException(arabic ? SUB_CATEGORY_ARABIC_NAME_EXISTS_ARABIC : SUB_CATEGORY_ARABIC_NAME_EXISTS);
            }
        }
    }

    @Override
    public void deleteSubCategory(Long id, LanguageType languageType) {
        LOGGER.info("Deactivating sub category for id: {}", id);
        SubCategory existingSubCategory = findById(id, languageType);
        if(existingSubCategory.getStatus()) {
            existingSubCategory.setStatus(false);
            save(existingSubCategory);
        }
    }

    @Override
    public ResponseEntity<?> addOrUpdateSubCategoryImage(Long subCategoryId, MultipartFile image, Customer customer, LanguageType lang) {
        final long ALLOWED_FILE_SIZE = (long) (1.5 * 1024);

        String imageFullPath = "";
        LOGGER.info("SubCategory Image - Name: {}, Type: {}", image.getOriginalFilename(), image.getContentType());

        try {
            String imageOriginalName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()).toLowerCase()).replace(" ", "");
            String imageFileName = imageOriginalName.substring(0, imageOriginalName.lastIndexOf("."));
            String imageFileType = imageOriginalName.substring(imageOriginalName.lastIndexOf(".") + 1).toUpperCase();
            double imageFileSize = (image.getSize() / 1024.0);

            LOGGER.info("File name: {}", imageOriginalName);
            LOGGER.info("File type: {}", imageFileType);

            if (!ImageUtils.isValidImageFileType(imageFileType) || imageFileSize > ALLOWED_FILE_SIZE) {
                return lang.equals(LanguageType.ARB) ?  Response.notAcceptable(ERROR_IMAGE_FAILED_ARABIC.concat(ERROR_INVALID_FILE_TYPE_OR_SIZE_ARABIC)) : Response.notAcceptable(ERROR_IMAGE_FAILED.concat(ERROR_INVALID_FILE_TYPE_OR_SIZE));
            }

            // Upload the image
            imageFullPath = imageUtils.upload(image, imageFileName, imageOriginalName, imageFileType);
            LOGGER.info("Category Image Full Paths: {}", imageFullPath);

            if (imageFullPath.isEmpty()) {
                return lang.equals(LanguageType.ARB) ?  Response.error(ERROR_IMAGE_FAILED_ARABIC) : Response.error(ERROR_IMAGE_FAILED);
            }

            SubCategory subCategory = findById(subCategoryId, lang);
            subCategory.setImage(imageFullPath);
            save(subCategory);

            return lang.equals(LanguageType.ARB) ?  Response.success(IMAGE_UPLOADED_SUCCESSFULLY_ARABIC) : Response.success(IMAGE_UPLOADED_SUCCESSFULLY);
        } catch (Exception e) {
            LOGGER.error("Error processing/updating image: {}", e.getMessage());
            return lang.equals(LanguageType.ARB) ?  Response.error(ERROR_IMAGE_FAILED_ARABIC) : Response.error(ERROR_IMAGE_FAILED, e.getMessage());
        }
    }
}
