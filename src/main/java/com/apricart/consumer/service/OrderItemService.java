package com.apricart.consumer.service;


import com.apricart.consumer.enity.OrderItem;
import com.apricart.consumer.enity.Orders;
import com.apricart.consumer.enity.ProductWarehouse;
import com.apricart.consumer.security.dto.request.OrderItemRequestDTO;
import com.apricart.consumer.security.enums.LanguageType;

import java.util.List;

public interface OrderItemService {
    List<OrderItem> getAllOrderItems();

    OrderItem findById(Long id, LanguageType languageType);
    List<OrderItem> findByOrder(Orders orders);
    List<OrderItem> findByProductWarehouse(ProductWarehouse productWarehouse);

    Double calculateTotalAmount(Long id, LanguageType languageType);
    Double calculateTaxAmount(Long id, LanguageType languageType);

    void saveOrderItem(OrderItemRequestDTO orderRequestDTO, LanguageType languageType);
    void saveOrderItem(OrderItem orderItem, Orders orders);

    void updateOrderItemQuantity(OrderItem orderItem, LanguageType languageType, boolean isOrderCancelled);
    boolean isInStockItem(OrderItemRequestDTO orderItem, LanguageType languageType);


    OrderItem updateOrderItem(OrderItemRequestDTO orderRequestDTO, LanguageType languageType);

    void deleteOrderItem(Long id, LanguageType languageType);
}
