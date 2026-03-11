package com.apricart.consumer.service.Impl;

import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.enity.OnBoard;
import com.apricart.consumer.exceptions.ResourceNotFoundException;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.repository.jpa.OnBoardRepository;
import com.apricart.consumer.security.dto.request.OnBoardRequestDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.OnBoardService;
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
public class OnBoardServiceImpl implements OnBoardService {
    protected static final Logger LOGGER = LoggerFactory.getLogger(OnBoardServiceImpl.class);

    @Autowired
    private OnBoardRepository onBoardRepository;
    @Autowired
    private ImageUtils imageUtils;
    private static final String ONBOARD_ENG = "OnBoard";
    private static final String ONBOARD_ARB = "الإعدادات الأولية";

    @Override
    public OnBoard findById(Long id, LanguageType languageType) {
        LOGGER.info("Finding OnBoarding by id: {}", id);
        return onBoardRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("OnBoarding with id {} not found", id);
                    return LanguageType.ARB.equals(languageType) ? new ResourceNotFoundException(ONBOARD_ARB, id, true)  : new ResourceNotFoundException(ONBOARD_ENG, id, false);
                });
    }

    @Override
    public List<OnBoard> findAllOnBoardings(LanguageType lang) {
        LOGGER.info("Getting all onBoardings");
        List<OnBoard> onBoards = onBoardRepository.findAll().stream()
                .peek(c -> {
                    if (c.getImage() != null) {
                        c.setImage(imageUtils.getImagePath(c.getImage()));
                    }
                })
                .sorted(Comparator.comparingLong(OnBoard::getId))
                .collect(Collectors.toList());

        if (!onBoards.isEmpty()) {
            return onBoards;
        } else {
            LOGGER.error("No OnBoarding found");
            throw new ResourceNotFoundException(LanguageType.ARB.equals(lang)? ONBOARD_NOT_FOUND : ONBOARD_NOT_FOUND_ARABIC, true);
        }
    }

    @Override
    public void addOnBoarding(OnBoardRequestDTO onBoardRequestDTO) {
        LOGGER.info("Adding OnBoarding: {}", onBoardRequestDTO);
        save(OnBoard.fromDTO(onBoardRequestDTO));
    }

    @Override
    public void removeOnBoardById(Long id, LanguageType languageType) {
        LOGGER.info("Removing OnBoarding by id: {}", id);
        if (!onBoardRepository.existsById(id)) {
            LOGGER.error("OnBoarding with id {} not found for removing", id);
            throw LanguageType.ARB.equals(languageType) ? new ResourceNotFoundException(ONBOARD_ARB, id, true)  : new ResourceNotFoundException(ONBOARD_ENG, id, false);
        }
        onBoardRepository.deleteById(id);
    }

    @Override
    public String getOnBoardingImage(Long id, LanguageType languageType) {
        LOGGER.info("Getting OnBoarding image for OnBoard id: {}", id);
        OnBoard onBoard = findById(id, languageType);
        return imageUtils.getImagePath(onBoard.getImage());
    }

    public OnBoard save(OnBoard onBoard) {
        LOGGER.info("Saving onBoarding: {}", onBoard);
        return onBoardRepository.save(onBoard);
    }

    @Override
    public ResponseEntity<?> addOrUpdateOnBoardingImage(Long onBoardId, MultipartFile image, Customer customer, LanguageType lang) {
        LOGGER.info("Adding or updating image for onBoarding with id: {}", onBoardId);
        final long ALLOWED_FILE_SIZE = (long) (1.5 * 1024);

        String imageFullPath = "";
        LOGGER.info("OnBoarding Image - Name: {}, Type: {}", image.getOriginalFilename(), image.getContentType());

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
            LOGGER.info("onBoarding Image Full Paths: {}", imageFullPath);

            if (imageFullPath.isEmpty()) {
                LOGGER.error("Failed to upload image");
                return lang.equals(LanguageType.ARB) ?  Response.error(ERROR_IMAGE_FAILED_ARABIC) : Response.error(ERROR_IMAGE_FAILED);
            }

            OnBoard onBoard = findById(onBoardId, lang);
            onBoard.setImage(imageFullPath);
            save(onBoard);

            LOGGER.info("Image uploaded successfully");
            return lang.equals(LanguageType.ARB) ?  Response.success(IMAGE_UPLOADED_SUCCESSFULLY_ARABIC) : Response.success(IMAGE_UPLOADED_SUCCESSFULLY);
        } catch (Exception e) {
            LOGGER.error("Error processing/updating image: {}", e.getMessage());
            return lang.equals(LanguageType.ARB) ?  Response.error(ERROR_IMAGE_FAILED_ARABIC) : Response.error(ERROR_IMAGE_FAILED, e.getMessage());
        }
    }
}
