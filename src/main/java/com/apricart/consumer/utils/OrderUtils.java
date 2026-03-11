package com.apricart.consumer.utils;

import com.apricart.consumer.enity.Coupon;
import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.enity.OrderItem;
import com.apricart.consumer.enity.Orders;
import com.apricart.consumer.exceptions.OrderException;
import com.apricart.consumer.mapper.OrderMapper;
import com.apricart.consumer.security.constants.CouponErrorConstants;
import com.apricart.consumer.security.dto.request.CouponDetailRequestDTO;
import com.apricart.consumer.security.dto.request.OrderRequestDTO;
import com.apricart.consumer.security.enums.DiscountType;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderUtils extends OrderDetailUtils {

    protected static final Logger LOGGER = LoggerFactory.getLogger(OrderUtils.class);

    @Autowired
    private ProductWarehouseService productWarehouseService;

    @Autowired
    private CustomerAddressService customerAddressService;

    @Autowired
    private CouponDetailService couponDetailService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private OrderMapper orderMapper;


    public Orders generate(OrderRequestDTO orderRequestDTO, Customer customer, LanguageType lang) {

        LOGGER.info("Validate order");
        validateOrder(orderRequestDTO, customer, lang);

        LOGGER.info("Creating order");
        Orders orders = createOrder(orderRequestDTO, customer, lang);

        LOGGER.info("Apply Coupon");
        orders = applyCouponIfPresent(orders, lang);

        LOGGER.info("Save Coupon Details");
        saveCouponDetails(orders, lang);

        return orders;
    }

    private void validateOrder(OrderRequestDTO order, Customer customer, LanguageType lang) {

        validateOrderId(order.getId(), lang);

        long warehouseId = order.getWarehouseId();
        double deliveryCharges = getDeliveryCharges(warehouseId);
        double minOrderValue = getMinOrderValue(warehouseId);

        validateShippingCharges(deliveryCharges, order.getDeliveryCharges(), lang);
        validateDiscountTypeCouponCode(order, lang);
        validateSubtotal(order.getSubTotal(), minOrderValue, lang);

        if ((order.getCouponCode() != null) && !order.getCouponCode().isEmpty()) {
            if (order.getDiscountType() != null && DiscountType.COUPON_CODE.equals(order.getDiscountType())) {
                Coupon coupon = couponService.findByCode(order.getCouponCode(), lang);
                couponService.validateCoupon(Coupon.toDTO(coupon), customer, order.getSubTotal(), lang);
            } else {
                throw new OrderException(LanguageType.ARB.equals(lang) ? CouponErrorConstants.DISCOUNT_TYPE_NOT_CORRECT_MSG_ARABIC : CouponErrorConstants.DISCOUNT_TYPE_NOT_CORRECT_MSG);
            }
        }
    }

    private Orders createOrder(OrderRequestDTO orderRequest, Customer customer, LanguageType lang) throws OrderException {
        LOGGER.info("Creating order from OrderRequestDTO: {}", orderRequest);

        Orders order = orderMapper.mapToOrders(orderRequest);

        order.setCurrency(currencyService.findById(orderRequest.getCurrencyId(), lang));
        order.setCustomerAddress(customerAddressService.findById(orderRequest.getCustomerAddressId(), lang));
        order.setCreateDateTime(order.getCreateDateTime() == null ? LocalDateTime.now() : order.getCreateDateTime());
        order.setCustomer(customer);

        List<OrderItem> orderItems = setOrderItems(orderRequest, order, lang);
        order.setOrderItems(orderItems);

        double subtotal = calculateSubtotal(order);

        return order;
    }

    private List<OrderItem> setOrderItems(OrderRequestDTO orderRequestDTO, Orders orders, LanguageType lang) {

        List<OrderItem> orderItems = orderRequestDTO.getOrderItems().stream()
                .map(itemDTO -> setSingleOrderItem(itemDTO, orders, lang))
                .collect(Collectors.toList());

        LOGGER.info("Order items processed: {}", orderItems);
        return orderItems;
    }

    private Orders applyCouponIfPresent(Orders order, LanguageType lang) {

        String couponCode = order.getCouponCode();

        if (couponCode != null && !couponCode.isEmpty()) {
            Coupon coupon = couponService.findByCode(order.getCouponCode(), lang);


            if (coupon != null) {
                order.setTotalDiscount(coupon.getCouponDiscount());
                LOGGER.info("Applied coupon: {}", coupon);
            } else {
                LOGGER.info("Coupon not found: {}", couponCode);
                String errorMsg = LanguageType.ARB.equals(lang) ? CouponErrorConstants.COUPON_NOT_FOUND_MSG_ARABIC : CouponErrorConstants.COUPON_NOT_FOUND_MSG;
                throw new OrderException(errorMsg);
            }
        }

        if (DiscountType.COUPON_CODE.equals(order.getDiscountType()) && (couponCode == null || couponCode.isEmpty())) {

            LOGGER.info("Coupon code required but not provided for order: {}", order);
            throw new OrderException(LanguageType.ARB.equals(lang) ? CouponErrorConstants.COUPON_CODE_REQUIRED_MSG_ARABIC
                    : CouponErrorConstants.COUPON_CODE_REQUIRED_MSG);
        }
        return order;
    }

    void saveCouponDetails(Orders orders, LanguageType lang) {
        String couponCode = orders.getCouponCode();

        if (couponCode != null && !couponCode.isEmpty()) {
            Coupon coupon = couponService.findByCode(orders.getCouponCode(), lang);

            LOGGER.info("Handling coupon details for order: {} with coupon: {}", orders, coupon);

            couponDetailService.addCouponDetail(CouponDetailRequestDTO.builder()
                    .customerId(orders.getCustomer().getId())
                    .phoneNumber(orders.getCustomer().getPhoneNumber())
                    .warehouseId(orders.getWarehouseId())
                    .orderId(orders.getId())
                    .couponId(coupon.getId())
                    .build(), orders.getCustomer(), lang);

            LOGGER.info("Coupon details handled for order: {}", orders);
        }
    }
}
