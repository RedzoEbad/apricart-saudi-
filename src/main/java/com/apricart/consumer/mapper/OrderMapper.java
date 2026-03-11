package com.apricart.consumer.mapper;

import com.apricart.consumer.enity.*;
import com.apricart.consumer.security.dto.request.OrderItemRequestDTO;
import com.apricart.consumer.security.dto.request.OrderRequestDTO;
import com.apricart.consumer.security.dto.response.OrderItemResponseDTO;
import com.apricart.consumer.security.dto.response.OrderResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.security.enums.OrderTypeArabic;
import com.apricart.consumer.service.CurrencyService;
import com.apricart.consumer.service.CustomerAddressService;
import com.apricart.consumer.service.ProductService;
import com.apricart.consumer.utils.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    protected static final Logger LOGGER = LoggerFactory.getLogger(OrderMapper.class);

    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CustomerAddressService customerAddressService;

    public Orders mapToOrders(OrderRequestDTO orderRequestDTO) {
        Orders orders = Orders.fromDTO(orderRequestDTO);
        orders.setId(Utilities.getOrderId());
        LOGGER.info("Order created: {}", orders);

        return orders;
    }

    public OrderItem mapToOrderItem(OrderItemRequestDTO itemDTO) {
        return OrderItem.fromDTO(itemDTO);
    }

    public void updateOrder(Orders orders, OrderRequestDTO orderRequestDTO, LanguageType languageType) {
        LOGGER.info("Updating order from DTO: {}", orderRequestDTO);
        orders.setCurrency(currencyService.findById(orderRequestDTO.getCurrencyId(), languageType));
        orders.setCustomerAddress(customerAddressService.findById(orderRequestDTO.getCustomerAddressId(), languageType));
        LOGGER.info("Updated order from DTO: {}", orders);
    }

    public Orders updateOrderFromDTO(Orders existingOrders, OrderRequestDTO orderRequestDTO, LanguageType lang) {

        String translation = OrderTypeArabic.getTranslationForStatus(existingOrders.getOrderStatus());

        existingOrders.setCurrency(orderRequestDTO.getCurrencyId() == null ? existingOrders.getCurrency() : currencyService.findById(orderRequestDTO.getCurrencyId(), lang));
        existingOrders.setCustomerAddress(orderRequestDTO.getCustomerAddressId() == null ? existingOrders.getCustomerAddress() : customerAddressService.findById(orderRequestDTO.getCustomerAddressId(), lang));
        existingOrders.setNotes(orderRequestDTO.getNotes() == null ? existingOrders.getNotes() : orderRequestDTO.getNotes());
        existingOrders.setDiscountType(orderRequestDTO.getDiscountType() == null ? existingOrders.getDiscountType() : orderRequestDTO.getDiscountType());
        existingOrders.setPaymentMode(orderRequestDTO.getPaymentMode() == null ? existingOrders.getPaymentMode() : orderRequestDTO.getPaymentMode());
        existingOrders.setPaymentStatus(orderRequestDTO.getPaymentStatus() == null ? existingOrders.getPaymentStatus() : orderRequestDTO.getPaymentStatus());
        existingOrders.setSubTotal(orderRequestDTO.getSubTotal() == null ? existingOrders.getSubTotal() : orderRequestDTO.getSubTotal());
        existingOrders.setTotalDiscount(orderRequestDTO.getTotalDiscount() == null ? existingOrders.getTotalDiscount() : orderRequestDTO.getTotalDiscount());
        existingOrders.setTotalTax(orderRequestDTO.getTotalTax() == null ? existingOrders.getTotalTax() : orderRequestDTO.getTotalTax());
        existingOrders.setCouponCode(orderRequestDTO.getCouponCode() == null ? existingOrders.getCouponCode() : orderRequestDTO.getCouponCode());
        existingOrders.setGrandTotal(orderRequestDTO.getGrandTotal() == null ? existingOrders.getGrandTotal() : orderRequestDTO.getGrandTotal());
        existingOrders.setOrderStatus(orderRequestDTO.getOrderStatus() == null ? existingOrders.getOrderStatus() : orderRequestDTO.getOrderStatus());
        existingOrders.setOrderTypeArabic(orderRequestDTO.getOrderStatus() == null ? existingOrders.getOrderTypeArabic() : translation);
        existingOrders.setDeliveryCharges(orderRequestDTO.getDeliveryCharges() == null ? existingOrders.getDeliveryCharges() : orderRequestDTO.getDeliveryCharges());
        existingOrders.setMinOrderValue(orderRequestDTO.getMinOrderValue() == null ? existingOrders.getMinOrderValue() : orderRequestDTO.getMinOrderValue());

        return existingOrders;
    }

    public List<OrderResponseDTO> toOrderList(List<Orders> orders, LanguageType languageType) {
        return orders.stream()
                .map(ordersDTO -> toOrderDTO(ordersDTO, languageType))
                .filter(dto -> dto.getCreatedDateTime() != null)
                .sorted(Comparator.comparing(OrderResponseDTO::getCreatedDateTime).reversed())
                .collect(Collectors.toList());
    }

    public List<OrderItemResponseDTO> toOrderItemList(List<OrderItem> orderItems, LanguageType languageType) {
        return orderItems.stream()
                .map(orderDTO -> toOrderItemDTO(orderDTO, languageType))
                .filter(dto -> dto.getDeliveryDate() != null)
                .sorted(Comparator.comparing(OrderItemResponseDTO::getDeliveryDate).reversed())
                .collect(Collectors.toList());
    }


    public OrderResponseDTO toOrderDTO(Orders orders, LanguageType languageType) {
        return OrderResponseDTO.builder()
                .id(orders.getId())
                .notes(orders.getNotes())
                .couponCode(orders.getCouponCode())
                .grandTotal(orders.getGrandTotal())
                .netTotal(orders.getSubTotal())
                .paymentMode(orders.getPaymentMode())
                .discountType(orders.getDiscountType())
                .minOrderValue(orders.getMinOrderValue())
                .deliveryCharges(orders.getDeliveryCharges())
                .paymentStatus(orders.getPaymentStatus())
                .zohoSalesOrderNum(orders.getZohoSalesOrderNum())
                .totalTax(orders.getTotalTax())
                .totalDiscount(orders.getTotalDiscount())
                .deliveryDate(orders.getDeliveryDate())
                .deliveryTime(orders.getDeliveryTime())
                .orderStatus(orders.getOrderStatus())
                .arabicOrderStatus(orders.getOrderTypeArabic())
                .orderItems(this.toOrderItemList(orders.getOrderItems(), languageType))
                .status(orders.getStatus())
                .customerId(orders.getCustomer().getId())
                .currency(Currency.toDTO(orders.getCurrency()))
                .customerAddress(CustomerAddress.toDTO(orders.getCustomerAddress()))
                .warehouseId(orders.getWarehouseId())
                .createdDateTime(orders.getCreateDateTime())
                .updateDateTime(orders.getUpdateDateTime())
                .build();
    }
    public OrderItemResponseDTO toOrderItemDTO(OrderItem orderItem, LanguageType languageType) {
        Product product = getProduct(orderItem.getProductWarehouse().getProduct().getId(), languageType);
        return OrderItemResponseDTO.builder()
                .id(orderItem.getId())
                .title(orderItem.getTitle())
                .arabicTitle(orderItem.getArabicTitle())
                .quantity(orderItem.getQuantity())
                .taxAmount(orderItem.getTaxAmount())
                .taxType(orderItem.getTaxType())
                .totalAmount(orderItem.getTotalAmount())
                .orderId(orderItem.getOrders().getId())
                .productWarehouseId(orderItem.getProductWarehouse().getId())
                .deliveryDate(orderItem.getOrders().getDeliveryDate())
                .deliveryTime(orderItem.getOrders().getDeliveryTime())

                // Product Details
                .productId(product.getId())
                .image(product.getImage())
                .weight(product.getWeight())
                .build();
    }

    public Product getProduct (Long productId, LanguageType languageType) {
        return productService.findById(productId, languageType);
    }
}
