package com.apricart.consumer.service.Impl;

import com.apricart.consumer.enity.Category;
import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.exceptions.DuplicateResourceException;
import com.apricart.consumer.exceptions.ResourceNotFoundException;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.repository.jpa.CategoryRepository;
import com.apricart.consumer.security.dto.request.CategoryRequestDTO;
import com.apricart.consumer.security.dto.response.CategoryResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.security.enums.LevelType;
import com.apricart.consumer.service.CategoryService;
import com.apricart.consumer.service.ProductWarehouseService;
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
public class CategoryServiceImpl implements CategoryService {
    protected static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductWarehouseService productWarehouseService;
    @Autowired
    private ImageUtils imageUtils;
    private static final String CATEGORY_ENG = "Category";
    private static final String CATEGORY_ARB = "الفئة";

    @Override
    public List<Category> getAllCategories(LanguageType lang){
        LOGGER.info("Getting all categories");
        List<Category> categories = categoryRepository.findAll().stream()
                .filter(Category::getStatus)
                .peek(c -> {
                    if (c.getImage() != null) {
                        c.setImage(imageUtils.getImagePath(c.getImage()));
                    }
                })
                .sorted(Comparator.comparingLong(Category::getPosition))
                .collect(Collectors.toList());

        if (!categories.isEmpty()) {
            return categories;
        } else {
            LOGGER.error("No categories found");
            throw new ResourceNotFoundException(LanguageType.ARB.equals(lang) ? CATEGORY_NOT_FOUND_ARABIC : CATEGORY_NOT_FOUND, true);
        }
    }

    @Override
    public Category findById(Long id, LanguageType languageType) {
        LOGGER.info("Finding category by id: {}", id);
        return categoryRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Category with id {} not found", id);
                    return LanguageType.ARB.equals(languageType) ? new ResourceNotFoundException(CATEGORY_ARB, id, true) : new ResourceNotFoundException(CATEGORY_ENG, id, false);
                });
    }

    @Override
    public Category findByName(String name) {
        LOGGER.info("Finding category by name: {}", name);
        return categoryRepository.findCategoryByName(name);
    }
    @Override
    public Category findByArabicName(String arabicName) {
        LOGGER.info("Finding category by arabic name: {}", arabicName);
        return categoryRepository.findCategoryByArabicName(arabicName);
    }

    @Override
    public List<Category> findByLevel(LevelType level) {
        LOGGER.info("Finding categories by level: {}", level);
        List<Category> categories = categoryRepository.findCategoryByLevel(level);
        LOGGER.info("Found categories by level {}: {}", level, categories);
        return categories;
    }

    @Override
    public List<Category> getActiveCategories(LanguageType lang) {
        LOGGER.info("Getting active categories");
        List<Category> categories = categoryRepository.findAll().stream()
                .filter(Category::getStatus)
                .peek(c -> {
                    if (c.getImage() != null) {
                        c.setImage(imageUtils.getImagePath(c.getImage()));
                    }
                })
                .sorted(Comparator.comparingLong(Category::getPosition))
                .collect(Collectors.toList());

        if (!categories.isEmpty()) {
            return categories;
        } else {
            LOGGER.error("No active categories found");
            throw new ResourceNotFoundException(LanguageType.ARB.equals(lang) ? CATEGORY_STATUS_NOT_ACTIVE_ARABIC : CATEGORY_STATUS_NOT_ACTIVE, true);
        }
    }

    @Override
    public String getCategoryImage(Long id, LanguageType languageType) {
        LOGGER.info("Getting category image for category id: {}", id);
        Category category = findById(id, languageType);
        return imageUtils.getImagePath(category.getImage());
    }

    @Override
    public Category updateCategoryLevelById(Long id, LevelType level, LanguageType languageType) {
        LOGGER.info("Updating category level for category id: {} to {}", id, level);
        Category existingCategory = findById(id, languageType);
        existingCategory.setLevel(level == null ? existingCategory.getLevel() : level);
        return save(existingCategory);
    }

    @Override
    public Category updateCategoryPosition(Long id, Integer position, LanguageType languageType) {
        LOGGER.info("Updating category position for category id: {} to {}", id, position);
        Category existingCategory = findById(id, languageType);
        existingCategory.setPosition(position == null ? existingCategory.getPosition() : position);
        return save(existingCategory);
    }

    @Override
    public void addCategory(CategoryRequestDTO categoryRequestDTO, LanguageType languageType) {
        LOGGER.info("Adding category: {}", categoryRequestDTO);
        validateCategoryNameUnique(categoryRequestDTO.getName(), categoryRequestDTO.getArabicName(), null, languageType);
        save(Category.fromDTO(categoryRequestDTO));
    }

    public Category save(Category category) {
        LOGGER.info("Saving category: {}", category);
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(CategoryRequestDTO categoryRequestDTO, LanguageType languageType) {
        LOGGER.info("Updating category: {}", categoryRequestDTO);
        Category existingCategory = findById(categoryRequestDTO.getId(), languageType);

        String newName = categoryRequestDTO.getName() == null ? existingCategory.getName() : categoryRequestDTO.getName();
        String newArabicName = categoryRequestDTO.getArabicName() == null ? existingCategory.getArabicName() : categoryRequestDTO.getArabicName();
        validateCategoryNameUnique(newName, newArabicName, existingCategory.getId(), languageType);

        existingCategory.setName(newName);
        existingCategory.setArabicName(newArabicName);
        existingCategory.setImage(categoryRequestDTO.getImage() == null ? existingCategory.getImage() : categoryRequestDTO.getImage());
        existingCategory.setLevel(categoryRequestDTO.getLevel() == null ? existingCategory.getLevel() : categoryRequestDTO.getLevel());
        existingCategory.setPosition(categoryRequestDTO.getPosition() == null ? existingCategory.getPosition() : categoryRequestDTO.getPosition());
        existingCategory.setStatus(categoryRequestDTO.getStatus() == null ? existingCategory.getStatus() : categoryRequestDTO.getStatus());
        return save(existingCategory);
    }

    private void validateCategoryNameUnique(String name, String arabicName, Long excludeId, LanguageType languageType) {
        boolean arabic = LanguageType.ARB.equals(languageType);
        if (name != null && !name.trim().isEmpty()) {
            boolean exists = excludeId == null
                    ? categoryRepository.existsByNameIgnoreCase(name.trim())
                    : categoryRepository.existsByNameIgnoreCaseAndIdNot(name.trim(), excludeId);
            if (exists) {
                throw new DuplicateResourceException(arabic ? CATEGORY_NAME_EXISTS_ARABIC : CATEGORY_NAME_EXISTS);
            }
        }
        if (arabicName != null && !arabicName.trim().isEmpty()) {
            boolean exists = excludeId == null
                    ? categoryRepository.existsByArabicNameIgnoreCase(arabicName.trim())
                    : categoryRepository.existsByArabicNameIgnoreCaseAndIdNot(arabicName.trim(), excludeId);
            if (exists) {
                throw new DuplicateResourceException(arabic ? CATEGORY_ARABIC_NAME_EXISTS_ARABIC : CATEGORY_ARABIC_NAME_EXISTS);
            }
        }
    }

    @Override
    public void deleteCategory(Long id, LanguageType languageType) {
        LOGGER.info("Deactivating category for id: {}", id);
        Category existingCategory = findById(id, languageType);
        if(existingCategory.getStatus()) {
            existingCategory.setStatus(false);
            save(existingCategory);
        }
    }

    @Override
    public ResponseEntity<?> addOrUpdateCategoryImage(Long categoryId, MultipartFile image, Customer customer, LanguageType lang) {
        LOGGER.info("Adding or updating image for category with id: {}", categoryId);
        final long ALLOWED_FILE_SIZE = (long) (20 * 1024);

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

            Category category = findById(categoryId, lang);
            category.setImage(imageFullPath);
            save(category);

            LOGGER.info("Image uploaded successfully");
            return lang.equals(LanguageType.ARB) ?  Response.success(IMAGE_UPLOADED_SUCCESSFULLY_ARABIC) : Response.success(IMAGE_UPLOADED_SUCCESSFULLY);
        } catch (Exception e) {
            LOGGER.error("Error processing/updating image: {}", e.getMessage());
            return lang.equals(LanguageType.ARB) ?  Response.error(ERROR_IMAGE_FAILED_ARABIC) : Response.error(ERROR_IMAGE_FAILED, e.getMessage());
        }
    }

    @Override
    public List<CategoryResponseDTO> getCategoriesByWarehouseId(Long warehouseId) {

        List<Category> categories = productWarehouseService.findCategoriesByWarehouseId(warehouseId);
        setCategoryImages(categories);
        categories.sort(Comparator.comparingInt(Category::getPosition));

        return Category.toDTOList(categories);
    }

    @Override
    public List<Category> getDiscountedCategories(Boolean IsDiscountedCategory) {
        return categoryRepository.findAllByIsDiscountedCategory(IsDiscountedCategory);
    }

    @Override
    public Boolean checkIsDiscountedCategory(Long categoryId, LanguageType languageType) {
        return findById(categoryId, languageType).getIsDiscountedCategory();
    }

    public void setCategoryImages(List<Category> categories) {
        categories.stream()
                .peek(c -> {
                    if (c.getImage() != null) {
                        c.setImage(imageUtils.getImagePath(c.getImage()));
                    }
                }).collect(Collectors.toList());
    }

}
