package com.apricart.consumer.service.Impl;

import com.apricart.consumer.enity.Banner;
import com.apricart.consumer.enity.Brand;
import com.apricart.consumer.enity.Category;
import com.apricart.consumer.exceptions.ResourceNotFoundException;
import com.apricart.consumer.mapper.ProductMapper;
import com.apricart.consumer.security.dto.dto.ProductDetailDTO;
import com.apricart.consumer.security.dto.response.HomeDTO;
import com.apricart.consumer.security.dto.response.ProductWarehouseResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.BannerService;
import com.apricart.consumer.service.BrandService;
import com.apricart.consumer.service.HomeService;
import com.apricart.consumer.service.ProductWarehouseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.HOME_DETAILS_FAILED_ARABIC;
import static com.apricart.consumer.security.constants.ResponseMessage.HOME_DETAILS_FAILED;

@Service
@Transactional
public class HomeServiceImpl implements HomeService {
    protected static final Logger LOGGER = LoggerFactory.getLogger(HomeServiceImpl.class);

    @Autowired
    private ProductWarehouseService productWarehouseService;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private BrandService brandService;

    @Autowired
    private BannerService bannerService;

    @Override
    public HomeDTO getHomeDetailsByWarehouseId(Long warehouseId, LanguageType lang, Long customerId, int pageNo, int pageSize, LanguageType languageType) {
        try {
            List<ProductWarehouseResponseDTO> newArrivals = productWarehouseService.findNewArrivalsByWarehouseId(warehouseId, pageNo, pageSize, languageType);
            List<ProductWarehouseResponseDTO> trending = productWarehouseService.findTrendingByWarehouseId(warehouseId, pageNo, pageSize, languageType);
            List<ProductWarehouseResponseDTO> recommended = productWarehouseService.findRecommendedByWarehouseId(warehouseId, pageNo, pageSize, languageType);

            List<Brand> activeBrands = Collections.emptyList();
            try {
                activeBrands = brandService.getActiveBrands(lang);
            } catch (Exception e) {
                LOGGER.warn("Could not fetch active brands for home aggregated response: {}", e.getMessage());
            }

            List<Banner> activeBanners = Collections.emptyList();
            try {
                activeBanners = bannerService.getActiveBanners(lang);
            } catch (Exception e) {
                LOGGER.warn("Could not fetch active banners for home aggregated response: {}", e.getMessage());
            }

            return HomeDTO.builder()
                    .categories(Category.toDTOList(productWarehouseService.findCategoriesByWarehouseId(warehouseId)))
                    .newArrivals(handleProductMapping(newArrivals, customerId, lang))
                    .trending(handleProductMapping(trending, customerId, lang))
                    .recommended(handleProductMapping(recommended, customerId, lang))
                    .brands(Brand.toDTOList(activeBrands))
                    .banners(Banner.toDTOList(activeBanners))
                    .build();
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching home details by warehouse id: {}", warehouseId, e);
            throw new ResourceNotFoundException(LanguageType.ARB.equals(lang) ? HOME_DETAILS_FAILED_ARABIC : HOME_DETAILS_FAILED, true);
        }
    }

    private List<ProductDetailDTO> handleProductMapping(List<ProductWarehouseResponseDTO> products, Long customerId, LanguageType lang) {
        if (products == null || products.isEmpty()) {
            return Collections.emptyList();
        }
        return productMapper.mapAndSortProductDetails(products, customerId, lang);
    }
}
