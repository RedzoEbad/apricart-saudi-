package com.apricart.consumer.controller;

import com.apricart.consumer.enity.Banner;
import com.apricart.consumer.generic.GenericResponse;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.security.dto.request.BannerRequestDTO;
import com.apricart.consumer.security.dto.response.BannerResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.security.enums.LevelType;
import com.apricart.consumer.security.enums.PositionType;
import com.apricart.consumer.service.BannerService;
import com.apricart.consumer.service.BaseService;
import com.google.protobuf.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.BANNER_REMOVED_SUCCESSFULLY_ARABIC;
import static com.apricart.consumer.security.constants.ResponseMessage.BANNER_REMOVED_SUCCESSFULLY;

@RestController
@RequestMapping("/v1/auth/open/banners")
@Api(value = "Banner Controller", tags = {"Banner"})
public class BannerController {

    @Autowired
    private BannerService bannerService;
    @Autowired
    BaseService baseService;

    @ApiOperation(value = "Get all banners")
    @GetMapping
    public ResponseEntity<GenericResponse<List<BannerResponseDTO>>> getAllBanners(
            @RequestHeader("Language") LanguageType lang) {
        List<BannerResponseDTO> banner = Banner.toDTOList(bannerService.getAllBanners(lang));
        return !banner.isEmpty() ? Response.success(banner) : Response.notFound();
    }

    @ApiOperation(value = "Get Banner by Id")
    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<BannerResponseDTO>> findBannerById(
            @PathVariable Long id,
            @RequestHeader("Language") LanguageType lang) {
        Banner banner = bannerService.findById(id, lang);
        return banner != null ? Response.success(Banner.toDTO(banner)) : Response.notFound();
    }

    @ApiOperation(value = "Get banners by position")
    @GetMapping("/position/{positionType}")
    public ResponseEntity<GenericResponse<List<BannerResponseDTO>>> getBannersByPosition(
            @PathVariable PositionType positionType,
            @RequestHeader("Language") LanguageType lang) {
        List<BannerResponseDTO> banner = Banner.toDTOList(bannerService.findByPosition(positionType));
        return !banner.isEmpty() ? Response.success(banner) : Response.notFound();
    }

    @ApiOperation(value = "Get banners by level")
    @GetMapping("/level/{levelType}")
    public ResponseEntity<GenericResponse<List<BannerResponseDTO>>> getBannersByLevel(
            @PathVariable LevelType levelType,
            @RequestHeader("Language") LanguageType lang) {
        List<BannerResponseDTO> banner = Banner.toDTOList(bannerService.findByLevel(levelType));
        return !banner.isEmpty() ? Response.success(banner) : Response.notFound();
    }

    @ApiOperation(value = "Get all active banners")
    @GetMapping("/active")
    public ResponseEntity<GenericResponse<List<BannerResponseDTO>>> getActiveBanners(
            @RequestHeader("Language") LanguageType lang) {
        List<BannerResponseDTO> banners = Banner.toDTOList(bannerService.getActiveBanners(lang));
        return !banners.isEmpty() ? Response.success(banners) : Response.notFound();
    }

    @ApiOperation(value = "Add Banner")
    @PostMapping
    public ResponseEntity<GenericResponse<BannerResponseDTO>> addBanner(
            @Valid @RequestBody BannerRequestDTO bannerRequestDTO,
            @RequestHeader("Language") LanguageType lang) throws ServiceException {
        bannerService.addBanner(bannerRequestDTO);
        return Response.created();
    }

    @ApiOperation(value = "Update Banner")
    @PutMapping
    public ResponseEntity<GenericResponse<BannerResponseDTO>> updateBanner(
            @Valid @RequestBody BannerRequestDTO brandRequestDTO,
            @RequestHeader("Language") LanguageType lang) {
        Banner updateBanner = bannerService.updateBanner(brandRequestDTO, lang);
        return updateBanner != null ? Response.success(Banner.toDTO(updateBanner)) : Response.notFound();
    }

    @ApiOperation(value = "Delete Banner By Id")
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<String>> deleteBanner(
            @PathVariable Long id,
            @RequestHeader("Language") LanguageType lang) {
        bannerService.deleteBanner(id, lang);
        return lang.equals(LanguageType.ARB) ? Response.success(BANNER_REMOVED_SUCCESSFULLY_ARABIC) : Response.success(BANNER_REMOVED_SUCCESSFULLY);
    }

    @ApiOperation(value = "Get banner image by Id")
    @GetMapping("/image/{id}")
    public ResponseEntity<GenericResponse<String>> getBannerImageById(
            @PathVariable Long id,
            @RequestHeader("Language") LanguageType lang) {
        String categoryImage = bannerService.getBannerImage(id, lang);
        return !categoryImage.isEmpty() ? Response.success(categoryImage) : Response.notFound();
    }

    @ApiOperation(value = "Banner Image Update By Id")
    @PostMapping("/image/update/{id}")
    public ResponseEntity<?> updateBrandImage(
            @PathVariable Long id,
            @RequestParam(value = "image", required = true) MultipartFile image,
            HttpServletRequest request,
            @RequestHeader("Language") LanguageType lang) {
        return bannerService.addOrUpdateBannerImage(id, image, baseService.resolveUser(request), lang);
    }

}
