package com.apricart.consumer.service;

import com.apricart.consumer.enity.Coupon;
import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.exceptions.CouponValidationException;
import com.apricart.consumer.security.dto.request.CouponRequestDTO;
import com.apricart.consumer.security.dto.response.CouponResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;

import java.util.List;

public interface CouponService {
    List<Coupon> getAllCoupons();

    Coupon findById(Long id, LanguageType languageType);
    Coupon findByCode(String code, LanguageType languageType);
    List<Coupon> findByStatus(Boolean status);
    List<Coupon> findByMinSubTotal(Double minSubTotal);
    List<Coupon> findByUsageLimit(String usageLimit);
    List<Coupon> findByExpiry(String expiry);

    void validateCoupon(CouponResponseDTO coupon, Customer customer, Double subtotal, LanguageType lang) throws CouponValidationException;

    void addCoupon(CouponRequestDTO couponRequestDTO);
    Coupon save(Coupon coupon);
    Coupon updateCoupon(CouponRequestDTO couponRequestDTO, LanguageType languageType);

    void deleteCoupon(Long id, LanguageType languageType);
}
