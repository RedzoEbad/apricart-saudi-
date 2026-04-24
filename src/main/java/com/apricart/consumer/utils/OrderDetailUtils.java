package com.apricart.consumer.utils;

import com.apricart.consumer.enity.OrderItem;
import com.apricart.consumer.enity.Orders;
import com.apricart.consumer.enity.Tax;
import com.apricart.consumer.exceptions.OrderException;
import com.apricart.consumer.security.constants.CouponErrorConstants;
import com.apricart.consumer.security.dto.request.OrderItemRequestDTO;
import com.apricart.consumer.security.dto.request.OrderRequestDTO;
import com.apricart.consumer.security.enums.DiscountType;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.OptionService;
import com.apricart.consumer.service.ProductWarehouseService;
import com.apricart.consumer.service.TaxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.apricart.consumer.mapper.ProductMapper.calculateTaxAmount;
import static com.apricart.consumer.security.constants.Constants.DELIVERY_CHARGES_KEY;
import static com.apricart.consumer.security.constants.Constants.MIN_ORDER_VALUE_KEY;

@Component
public class OrderDetailUtils {

    protected static final Logger LOGGER = LoggerFactory.getLogger(OrderDetailUtils.class);

    @Autowired
    private ProductWarehouseService productWarehouseService;

    @Autowired
    private OptionService optionService;

    @Autowired
    private TaxService taxService;


    void validateOrderId(String orderId, LanguageType lang) throws OrderException {
        if (orderId != null) {
            LOGGER.info("Order Id already taken: {}", orderId);

            throw new OrderException(LanguageType.ARB.equals(lang) ? CouponErrorConstants.ORDER_ID_ALREADY_TAKEN_MSG_ARABIC
                    : CouponErrorConstants.ORDER_ID_ALREADY_TAKEN_MSG);
        }
    }

    OrderItem setSingleOrderItem(OrderItemRequestDTO itemDTO, Orders orders, LanguageType lang) {
        if (itemDTO.getId() != null) {
            LOGGER.error("Order Item Id already taken: {}", itemDTO.getId());
            throw new OrderException(LanguageType.ARB.equals(lang) ? CouponErrorConstants.ORDER_ID_ALREADY_TAKEN_MSG_ARABIC
                    : CouponErrorConstants.ORDER_ID_ALREADY_TAKEN_MSG);
        }
        OrderItem orderItem = OrderItem.fromDTO(itemDTO);
        itemDTO.setOrderId(orders.getId());
        orderItem.setProductWarehouse(productWarehouseService.findById(itemDTO.getProductWarehouseId(), lang));
        Tax tax = getTax(orderItem.getProductWarehouse().getTax().getId(), lang);
        double taxAmount = calculateTaxAmount(tax.getTaxPercentage(), orderItem.getProductWarehouse().getCurrentRate());
        LOGGER.info("TAX AMOUNT: {}", taxAmount);
        orderItem.setTaxAmount(taxAmount);
        orderItem.setTaxType(tax.getTaxType());
        orderItem.setOrders(orders);

        LOGGER.info("Processed order item: {}", orderItem);
        return orderItem;
    }

    void validateSubtotal(double subtotal, double minOrderValue, LanguageType lang) {
        if (subtotal < minOrderValue) {
            throw new OrderException(LanguageType.ARB.equals(lang) ? CouponErrorConstants.SUBTOTAL_LESS_MSG_ARABIC : CouponErrorConstants.SUBTOTAL_LESS_MSG);
        }
    }

    void validateShippingCharges(double deliveryCharges, double orderDeliveryCharges, LanguageType lang) {
        if (orderDeliveryCharges < deliveryCharges) {
            throw new OrderException(LanguageType.ARB.equals(lang) ? CouponErrorConstants.DELIVERY_CHARGES_LESS_MSG_ARABIC : CouponErrorConstants.DELIVERY_CHARGES_LESS_MSG);
        }
    }

    void validateDiscountTypeCouponCode(OrderRequestDTO orders, LanguageType lang) {
        if (DiscountType.COUPON_CODE.equals(orders.getDiscountType()) && orders.getCouponCode() == null) {
            LOGGER.error("Coupon code is not provided for order: {}", orders);
            throw new OrderException(LanguageType.ARB.equals(lang) ? CouponErrorConstants.COUPON_CODE_REQUIRED_MSG_ARABIC : CouponErrorConstants.COUPON_CODE_REQUIRED_MSG);
        }
        if ((orders.getDiscountType() != null && !orders.getDiscountType().toString().isEmpty()) && (orders.getCouponCode() != null && !orders.getCouponCode().isEmpty())) {
            if (!DiscountType.COUPON_CODE.equals(orders.getDiscountType())) {
                LOGGER.error("Discount Type not Correct: {}", orders.getCouponCode());
                throw new OrderException(LanguageType.ARB.equals(lang) ? CouponErrorConstants.DISCOUNT_TYPE_NOT_CORRECT_MSG_ARABIC : CouponErrorConstants.DISCOUNT_TYPE_NOT_CORRECT_MSG);
            }
        }
    }


    double getDeliveryCharges(long warehouseId) {
        String key = DELIVERY_CHARGES_KEY + warehouseId;
        com.apricart.consumer.enity.Option option = optionService.findByKey(key);
        if (option == null) {
            LOGGER.error("CRITICAL ERROR: Missing configuration key in 'option' table: {}", key);
            throw new OrderException("Missing configuration key: " + key);
        }
        return Double.parseDouble(option.getValue());
    }

    double getMinOrderValue(long warehouseId) {
        String key = MIN_ORDER_VALUE_KEY + warehouseId;
        com.apricart.consumer.enity.Option option = optionService.findByKey(key);
        if (option == null) {
            LOGGER.error("CRITICAL ERROR: Missing configuration key in 'option' table: {}", key);
            throw new OrderException("Missing configuration key: " + key);
        }
        return Double.parseDouble(option.getValue());
    }

    double calculateSubtotal(Orders orders) {
        return orders.getOrderItems().stream().mapToDouble(OrderItem::getTotalAmount).sum();
    }

    public Tax getTax (Long taxId, LanguageType languageType) {
        return taxService.findById(taxId, languageType);
    }
}
