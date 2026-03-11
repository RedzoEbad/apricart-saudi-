package com.apricart.consumer.service.Impl;

import com.apricart.consumer.enity.Banner;
import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.exceptions.ResourceNotFoundException;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.repository.jpa.BannerRepository;
import com.apricart.consumer.security.dto.request.BannerRequestDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.security.enums.LevelType;
import com.apricart.consumer.security.enums.PositionType;
import com.apricart.consumer.service.BannerService;
import com.apricart.consumer.utils.ImageUtils;
import com.google.protobuf.ServiceException;
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
public class BannerServiceImpl implements BannerService {
    protected static final Logger LOGGER = LoggerFactory.getLogger(BannerServiceImpl.class);
    private static final String BANNER_ENG = "Banner";
    private static final String BANNER_ARB = "اللافتة";
    @Autowired
    private BannerRepository bannerRepository;
    @Autowired
    private ImageUtils imageUtils;

    @Override
    public List<Banner> getAllBanners(LanguageType lang) {
        LOGGER.info("Getting all banners");
        List<Banner> banners = bannerRepository.findAll().stream()
                .peek(banner -> {
                    if (banner.getImage() != null) {
                        banner.setImage(imageUtils.getImagePath(banner.getImage()));
                    }
                })
                .sorted(Comparator.comparingLong(Banner::getId))
                .collect(Collectors.toList());

        if (!banners.isEmpty()) {
            return banners;
        } else {
            LOGGER.error("No banner found");
            throw new ResourceNotFoundException(LanguageType.ARB.equals(lang) ? BANNER_NOT_FOUND_ARABIC : BANNER_NOT_FOUND, true);
        }
    }

    @Override
    public Banner findById(Long id, LanguageType languageType) {
        LOGGER.info("Finding banner by id: {}", id);
        return bannerRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Banner with id {} not found", id);
                    return LanguageType.ARB.equals(languageType) ? new ResourceNotFoundException(BANNER_ARB, id, true) : new ResourceNotFoundException(BANNER_ENG, id, false);
                });
    }

    @Override
    public List<Banner> findByPosition(PositionType positionType) {
        LOGGER.info("Finding banners by position: {}", positionType);
        return bannerRepository.findByPosition(positionType);
    }

    @Override
    public List<Banner> findByLevel(LevelType levelType) {
        LOGGER.info("Finding banners by level: {}", levelType);
        return bannerRepository.findByLevel(levelType);
    }

    @Override
    public List<Banner> getActiveBanners(LanguageType lang) {
        LOGGER.info("Getting active banners");
        List<Banner> banners = bannerRepository.findAll().stream()
                .filter(Banner::getStatus)
                .peek(banner -> {
                    if (banner.getImage() != null) {
                        banner.setImage(imageUtils.getImagePath(banner.getImage()));
                    }
                })
                .sorted(Comparator.comparingLong(Banner::getId))
                .collect(Collectors.toList());

        if (!banners.isEmpty()) {
            return banners;
        } else {
            LOGGER.error("No active banner found");
            throw new ResourceNotFoundException(LanguageType.ARB.equals(lang) ? BANNER_STATUS_NOT_ACTIVE_ARABIC : BANNER_STATUS_NOT_ACTIVE, true);
        }
    }

    @Override
    public String getBannerImage(Long id, LanguageType languageType) {
        LOGGER.info("Getting banner image for banner id: {}", id);
        Banner banner = findById(id, languageType);
        return imageUtils.getImagePath(banner.getImage());
    }

    @Override
    public void addBanner(BannerRequestDTO bannerRequestDTO) throws ServiceException {
        LOGGER.info("Adding Banner: {}", bannerRequestDTO);
        try {
            save(Banner.fromDTO(bannerRequestDTO));
        } catch (Exception e) {
            LOGGER.error("Error occurred while adding banner: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    public Banner save(Banner banner) {
        LOGGER.info("Saving banner: {}", banner);
        return bannerRepository.save(banner);
    }

    @Override
    public Banner updateBanner(BannerRequestDTO bannerRequestDTO, LanguageType languageType) {
        LOGGER.info("Updating Banner: {}", bannerRequestDTO);
        Banner existingBanner = findById(bannerRequestDTO.getId(), languageType);

        existingBanner.setName(bannerRequestDTO.getName() == null ? existingBanner.getName() : bannerRequestDTO.getName());
        existingBanner.setPosition(bannerRequestDTO.getPosition() == null ? existingBanner.getPosition() : bannerRequestDTO.getPosition());
        existingBanner.setLevel(bannerRequestDTO.getLevel() == null ? existingBanner.getLevel() : bannerRequestDTO.getLevel());
        existingBanner.setImage(bannerRequestDTO.getImage() == null ? existingBanner.getImage() : bannerRequestDTO.getImage());
        existingBanner.setStatus(bannerRequestDTO.getStatus() == null ? existingBanner.getStatus() : bannerRequestDTO.getStatus());
        return save(existingBanner);
    }

    @Override
    public void deleteBanner(Long id, LanguageType languageType) {
        LOGGER.info("Deleting  for id: {}", id);
        if (!bannerRepository.existsById(id)) {
            LOGGER.error("Banner with id {} not found for removing", id);
            throw LanguageType.ARB.equals(languageType) ? new ResourceNotFoundException(BANNER_ARB, id, true) : new ResourceNotFoundException(BANNER_ENG, id, false);
        }
        bannerRepository.deleteById(id);

    }

    @Override
    public ResponseEntity<?> addOrUpdateBannerImage(Long bannerId, MultipartFile image, Customer customer, LanguageType lang) {
        LOGGER.info("Adding or updating image for banner with id: {}", bannerId);
        final long ALLOWED_FILE_SIZE = (long) (1.5 * 1024);

        String imageFullPath = "";
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
            LOGGER.info("Banner Image Full Paths: {}", imageFullPath);

            if (imageFullPath.isEmpty()) {
                LOGGER.error("Failed to upload image");
                return lang.equals(LanguageType.ARB) ? Response.error(ERROR_IMAGE_FAILED_ARABIC) : Response.error(ERROR_IMAGE_FAILED);
            }

            Banner banner = findById(bannerId, lang);
            banner.setImage(imageFullPath);
            save(banner);

            LOGGER.info("Image uploaded successfully");
            return lang.equals(LanguageType.ARB) ? Response.success(IMAGE_UPLOADED_SUCCESSFULLY_ARABIC) : Response.success(IMAGE_UPLOADED_SUCCESSFULLY);
        } catch (Exception e) {
            LOGGER.error("Error processing/updating image: {}", e.getMessage());
            return lang.equals(LanguageType.ARB) ? Response.error(ERROR_IMAGE_FAILED_ARABIC) : Response.error(ERROR_IMAGE_FAILED, e.getMessage());
        }
    }
}
