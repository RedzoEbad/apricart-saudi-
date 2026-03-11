package com.apricart.consumer.service.Impl;

import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.enity.Product;
import com.apricart.consumer.enity.WishList;
import com.apricart.consumer.exceptions.ResourceNotFoundException;
import com.apricart.consumer.exceptions.WishListException;
import com.apricart.consumer.mapper.ProductMapper;
import com.apricart.consumer.repository.jpa.WishListRepository;
import com.apricart.consumer.security.dto.dto.ProductDetailDTO;
import com.apricart.consumer.security.dto.request.WishListRequestDTO;
import com.apricart.consumer.security.dto.response.WishListResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.CustomerService;
import com.apricart.consumer.service.ProductService;
import com.apricart.consumer.service.ProductWarehouseService;
import com.apricart.consumer.service.WishListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.WISHLIST_NOT_FOUND_ARABIC;
import static com.apricart.consumer.security.constants.ResponseMessage.WISHLIST_NOT_FOUND;

@Service
@Transactional
public class WishListServiceImpl implements WishListService {
    protected static final Logger LOGGER = LoggerFactory.getLogger(WishListServiceImpl.class);

    @Autowired
    WishListRepository wishListRepository;
    @Autowired
    CustomerService customerService;
    @Autowired
    private ProductWarehouseService productWarehouseService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductMapper productMapper;
    private static final String WISHLIST_ARB = "قائمة الأمنيات";
    private static final String WISHLIST_ENG = "WishList";

    @Override
    public WishList findById(Long id, LanguageType lang) {
        LOGGER.info("Finding wishlist by id: {}", id);
        return wishListRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("WishList with id {} not found", id);
                    return new ResourceNotFoundException(getWishListNotFoundText(lang, id), true);
                });
    }

    @Override
    public List<WishListResponseDTO> findByCustomerId(Customer customer, Long warehouseId, LanguageType languageType) {
        LOGGER.info("Finding wishlist by customer id: {}", customer.getId());

        List<WishList> wishLists = wishListRepository.findWishListByCustomer(customer);
        Map<Long, ProductDetailDTO> productDetailCache = new HashMap<>();

        return wishLists.stream()
                .map(wishList -> createWishListResponseDTO(wishList, warehouseId, productDetailCache, customer.getId(), languageType))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isProductInWishlist(Long customerId, Long productId) {
        return customerId != null && customerId > 0 && wishListRepository.existsByCustomerIdAndProductId(customerId, productId);
    }

    private WishListResponseDTO createWishListResponseDTO(WishList wishList, Long warehouseId, Map<Long, ProductDetailDTO> productDetailCache, Long customerId, LanguageType languageType) {
        Long productId = wishList.getProduct().getId();


        ProductDetailDTO productDetail = productDetailCache.computeIfAbsent(productId, id ->
                productWarehouseService.findByProductIdAndWarehouseId(id, warehouseId,languageType) != null ?
                        productMapper.mapToProductDetailDTO(productWarehouseService.findByProductIdAndWarehouseId(id, warehouseId, languageType), customerId, languageType) : null
        );

        WishListResponseDTO responseDTO = WishList.toDTO(wishList);
        responseDTO.setProductDetail(productDetail);

        return responseDTO;
    }

    @Override
    public void removeFromWishList(Long id, LanguageType lang) {
        LOGGER.info("Removing wishlist by id: {}", id);
        if (!wishListRepository.existsById(id)) {
            LOGGER.error("WishList with id {} not found for removing", id);
            throw new ResourceNotFoundException(getWishListNotFoundText(lang, id), true);
        }
        wishListRepository.deleteById(id);
    }

    @Override
    public void removeFromWishListByCustomerAndProductId(Customer customer, Long productId) {
        LOGGER.info("Deleting wishlist for customer: {}, Product Id: {}", customer.getId(), productId);
        wishListRepository.deleteByCustomerAndProductId(customer, productId);
    }

    @Override
    public void addWishList(WishListRequestDTO wishListRequestDTO, Customer customer, LanguageType lang) {
        Product product = productService.findById(wishListRequestDTO.getProductId(), lang);
        Optional<WishList> existingWishList = wishListRepository.findWishListByCustomerAndProduct(customer, product);
        if (existingWishList.isPresent()) {
            LOGGER.info("WishList with product name {} and customer {} already exists", product.getTitle(), customer);
            if (LanguageType.ARB.equals(lang)) {
                throw new WishListException(WISHLIST_ARB, product.getArabicTitle(), customer.getArabicName(), lang);
            } else {
                throw new WishListException(WISHLIST_ENG, product.getTitle(), customer.getName(), lang);
            }

        }
        LOGGER.info("Adding wishlist: {}", wishListRequestDTO);
        WishList wishList;
        wishList = WishList.fromDTO(wishListRequestDTO);
        wishList.setCustomer(customer);
        wishList.setProduct(product);
        save(wishList);
    }

    @Override
    public void clearWishList(Customer customer) {
        LOGGER.info("Clearing wishlist for customer: {}", customer.getId());
        wishListRepository.deleteByCustomer(customer);
    }


    public WishList save(WishList wishList) {
        LOGGER.info("Saving wishlist: {}", wishList);
        return wishListRepository.save(wishList);
    }

    String getWishListNotFoundText(LanguageType languageType, Long id) {
        if (LanguageType.ARB.equals(languageType)) {
            return String.format(WISHLIST_NOT_FOUND_ARABIC, id);
        } else {
            return String.format(WISHLIST_NOT_FOUND, id);
        }
    }
}
