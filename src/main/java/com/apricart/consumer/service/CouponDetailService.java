package com.apricart.consumer.service;


import com.apricart.consumer.enity.Coupon;
import com.apricart.consumer.enity.CouponDetail;
import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.security.dto.request.CouponDetailRequestDTO;
import com.apricart.consumer.security.enums.LanguageType;

import java.util.List;

public interface CouponDetailService {
    CouponDetail findById(Long id, LanguageType languageType);
    CouponDetail findByCoupon(Coupon coupon);
    Boolean verifyUsageLimit(Long id, LanguageType lang, LanguageType languageType);
    List<CouponDetail> findByWarehouseId(Long warehouseId);
    List<CouponDetail> findByOrderId(String orderId);
    void addCouponDetail(CouponDetailRequestDTO couponDetailRequestDTO, Customer customer, LanguageType languageType);
    Integer countByCustomerIdAndCoupon(Long customerId, Coupon coupon);
    Integer countByCustomerIdAndPhoneNumberAndCoupon(Long customerId, String phoneNumber, Coupon coupon);
    CouponDetail updateCouponDetail(CouponDetailRequestDTO couponDetailRequestDTO, LanguageType languageType);
}
