package com.apricart.consumer.service;


import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.enity.WishList;
import com.apricart.consumer.security.dto.request.WishListRequestDTO;
import com.apricart.consumer.security.dto.response.WishListResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;

import java.util.List;

public interface WishListService {
    WishList findById(Long id, LanguageType lang);
    List<WishListResponseDTO> findByCustomerId(Customer customer, Long warehouseId, LanguageType languageType);
    void addWishList(WishListRequestDTO warehouseRequestDTO, Customer customer, LanguageType lang);
    void clearWishList(Customer customer);
    void removeFromWishList(Long id, LanguageType lang);
    void removeFromWishListByCustomerAndProductId(Customer customer, Long id);
    boolean isProductInWishlist(Long customerId, Long productId);
}
