package com.apricart.consumer.service;

import com.apricart.consumer.enity.Cart;
import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.security.dto.request.CartRequestDTO;
import com.apricart.consumer.security.enums.LanguageType;

import java.util.List;

public interface CartService {
    List<Cart> getAllCartItems();
    List<Cart> findByCustomerId(Customer customer);
    String calculateTotal(Customer customer);
    void addToCart(CartRequestDTO cartRequestDTO, Customer customer, LanguageType languageType);
    Cart updateCart(Customer customer, Long productId, Long productWarehouseId, Integer quantity);
    void removeCartItem(Customer customer, Long productId);
    void clearCart(Customer customer);
}
