package com.apricart.consumer.service.Impl;

import com.apricart.consumer.enity.City;
import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.exceptions.ResourceNotFoundException;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.repository.jpa.CityRepository;
import com.apricart.consumer.security.dto.request.CityRequestDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.CityService;
import com.apricart.consumer.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.*;
import static com.apricart.consumer.security.constants.ResponseMessage.*;

@Service
public class CityServiceImpl implements CityService {
    protected static final Logger LOGGER = LoggerFactory.getLogger(CityServiceImpl.class);

    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private ImageUtils imageUtils;
    private static final String CITY_ENG = "City";
    private static final String CITY_ARB = "مدينة";

    @Override
    public List<City> getAllCities() {
        LOGGER.info("Getting all cities");
        return cityRepository.findAll().stream()
                .peek(c -> {
                    if (c.getImage() != null) {
                        c.setImage(imageUtils.getImagePath(c.getImage()));
                    }
                })
                .sorted(Comparator.comparingLong(City::getId))
                .collect(Collectors.toList());
    }

    @Override
    public List<City> getActiveCities() {
        LOGGER.info("Getting active cities");
        try {
            return cityRepository.findAll().stream()
                    .filter(city -> city.getIsActive() != null && city.getIsActive())
                    .peek(c -> {
                        if (c.getImage() != null) {
                            c.setImage(imageUtils.getImagePath(c.getImage()));
                        }
                    })
                    .sorted(Comparator.comparingLong(City::getId))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error("Error fetching active cities", e);
            throw new RuntimeException("Failed to fetch active cities: " + e.getMessage(), e);
        }
    }

    @Override
    public City findById(Long id, LanguageType languageType) {
        LOGGER.info("Getting city by id: {}", id);
        return cityRepository.findById(id)
                .orElseThrow(() -> LanguageType.ARB.equals(languageType) ? new ResourceNotFoundException(CITY_ARB, id, true) : new ResourceNotFoundException(CITY_ENG, id, false));
    }

    public City save(City city) {
        LOGGER.info("Saving city: {}", city);
        return cityRepository.save(city);
    }

    @Override
    public void addCity(CityRequestDTO cityRequestDTO) {
        LOGGER.info("Adding city: {}", cityRequestDTO);
        City city = City.fromDTO(cityRequestDTO);
        save(city);
    }

    @Override
    public City updateCity(CityRequestDTO city, LanguageType languageType) {
        LOGGER.info("Updating city: {}", city);
        City existingCity = findById(city.getId(), languageType);
        existingCity.setName(city.getName() == null ? existingCity.getName() : city.getName());
        existingCity.setCountry(city.getCountry() == null ? existingCity.getCountry() : city.getCountry());
        existingCity.setIsActive(city.getIsActive() == null ? existingCity.getIsActive() : city.getIsActive());
        return save(existingCity);
    }


    @Override
    public void deleteCity(Long id, LanguageType languageType) {
        LOGGER.info("Disabling city for id: {}", id);
        City existingCity = findById(id, languageType);
        if (existingCity != null && existingCity.getIsActive()) {
            existingCity.setIsActive(false);
            cityRepository.save(existingCity);
        }
    }

    @Override
    public ResponseEntity<?> addOrUpdateCityImage(Long cityId, MultipartFile image, Customer customer, LanguageType lang) {
        LOGGER.info("Adding or updating image for city with id: {}", cityId);
        final long ALLOWED_FILE_SIZE = (long) (1.5 * 1024);

        String imageFullPath = "";
        LOGGER.info("City Image - Name: {}, Type: {}", image.getOriginalFilename(), image.getContentType());

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
            LOGGER.info("Category Image Full Paths: {}", imageFullPath);

            if (imageFullPath.isEmpty()) {
                LOGGER.error("Failed to upload image");
                return lang.equals(LanguageType.ARB) ? Response.error(ERROR_IMAGE_FAILED_ARABIC) : Response.error(ERROR_IMAGE_FAILED);
            }

            City city = findById(cityId, lang);
            city.setImage(imageFullPath);
            save(city);

            LOGGER.info("Image uploaded successfully");
            return lang.equals(LanguageType.ARB) ? Response.success(IMAGE_UPLOADED_SUCCESSFULLY_ARABIC) : Response.success(IMAGE_UPLOADED_SUCCESSFULLY);
        } catch (Exception e) {
            LOGGER.error("Error processing/updating image: {}", e.getMessage());
            return lang.equals(LanguageType.ARB) ? Response.error(ERROR_IMAGE_FAILED_ARABIC) : Response.error(ERROR_IMAGE_FAILED, e.getMessage());
        }
    }

    @Override
    public String getCityImage(Long id, LanguageType languageType) {
        LOGGER.info("Getting city image for city id: {}", id);
        City city = findById(id, languageType);
        return imageUtils.getImagePath(city.getImage());
    }
}
