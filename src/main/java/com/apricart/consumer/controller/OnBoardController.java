package com.apricart.consumer.controller;

import com.apricart.consumer.enity.OnBoard;
import com.apricart.consumer.generic.GenericResponse;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.security.dto.request.OnBoardRequestDTO;
import com.apricart.consumer.security.dto.response.OnBoardResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.BaseService;
import com.apricart.consumer.service.OnBoardService;
import com.apricart.consumer.utils.ImageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.ONBOARD_REMOVED_SUCCESSFULLY_ARABIC;
import static com.apricart.consumer.security.constants.ResponseMessage.ONBOARD_REMOVED_SUCCESSFULLY;

@RestController
@RequestMapping("/v1/auth/open/onboards")
@Api(value = "OnBoard Controller", tags = {"OnBoard"})
public class OnBoardController{
    protected static final Logger LOGGER = LoggerFactory.getLogger(OnBoardController.class);

    @Autowired
    private OnBoardService onBoardService;
    @Autowired
    private BaseService baseService;

    @Autowired
    private ImageUtils imageUtils;

    @ApiOperation(value = "Get all onBoardings")
    @GetMapping
    public ResponseEntity<GenericResponse<List<OnBoardResponseDTO>>> getAllOnBoardings(
            @RequestHeader("Language") LanguageType lang) {
        List<OnBoardResponseDTO> onBoardings = OnBoard.toDTOList(onBoardService.findAllOnBoardings(lang));
        return !onBoardings.isEmpty() ? Response.success(onBoardings) : Response.notFound();
    }

    @ApiOperation(value = "Get onBoarding by Id")
    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<OnBoardResponseDTO>> findOnBoardingById(
            @PathVariable Long id,
            @RequestHeader("Language") LanguageType lang) {
        OnBoard onBoard = onBoardService.findById(id, lang);
        onBoard.setImage(onBoard.getImage() != null ? imageUtils.getImagePath(onBoard.getImage()) : null);
        return onBoard != null ? Response.success(OnBoard.toDTO(onBoard)) : Response.notFound();
    }

    @ApiOperation(value = "Add OnBoarding")
    @PostMapping
    public ResponseEntity<GenericResponse<OnBoardResponseDTO>> addOnBoarding(
            @Valid @RequestBody OnBoardRequestDTO onBoardRequestDTO,
            @RequestHeader("Language") LanguageType lang) {
        onBoardService.addOnBoarding(onBoardRequestDTO);
        return Response.created();
    }
    @ApiOperation(value = "Delete OnBoarding by Id")
    @DeleteMapping("/{onBoardId}")
    public ResponseEntity<GenericResponse<String>> removeOnBoardingById(@PathVariable Long onBoardId,
                                                                     @RequestHeader("Language") LanguageType lang) {
        onBoardService.removeOnBoardById(onBoardId, lang);
        return lang.equals(LanguageType.ARB) ? Response.success(ONBOARD_REMOVED_SUCCESSFULLY_ARABIC) : Response.success(ONBOARD_REMOVED_SUCCESSFULLY);
    }

    @ApiOperation(value = "Get OnBoarding image by Id")
    @GetMapping("/image/{id}")
    public ResponseEntity<GenericResponse<String>> getOnBoardingImageById(
            @PathVariable Long id,
            @RequestHeader("Language") LanguageType lang) {
        String onBoardImage = onBoardService.getOnBoardingImage(id, lang);
        return !onBoardImage.isEmpty() ? Response.success(onBoardImage) : Response.notFound();
    }

    @ApiOperation(value = "OnBoarding Image Update By Id")
    @PostMapping("/image/update/{id}")
    public ResponseEntity<?> updateOnBoardingImage(
            @PathVariable Long id,
            @RequestParam(value = "image", required = true) MultipartFile image,
            HttpServletRequest request,
            @RequestHeader("Language") LanguageType lang) {
        LOGGER.info("Image: "+ image.getSize());
        return onBoardService.addOrUpdateOnBoardingImage(id, image,  baseService.resolveUser(request), lang);
    }

}

