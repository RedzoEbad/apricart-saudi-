package com.apricart.consumer.security.validation;

import com.apricart.consumer.enity.Coupon;
import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.exceptions.CouponValidationException;
import com.apricart.consumer.security.constants.CouponErrorConstants;
import com.apricart.consumer.security.dto.response.CouponResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.CouponDetailService;
import com.apricart.consumer.utils.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CouponValidation {
    protected static final Logger LOGGER = LoggerFactory.getLogger(CouponValidation.class);

    @Autowired
    private CouponDetailService couponDetailService;

    public void validateCoupon(CouponResponseDTO coupon, Customer customer, Double subtotal, LanguageType language) throws CouponValidationException {

        String phoneNumber = Utilities.cleanSaudiPhoneNumber(customer.getPhoneNumber());

        validateCustomer(customer, language);
        validateCouponExistence(coupon, language);
        validateCouponStatus(coupon, language);
        validateCouponExpiry(coupon, language);
        validateSubtotal(subtotal, coupon, language);
        validateUsageLimit(customer.getId(), phoneNumber, coupon, language);
    }

    private void validateCustomer(Customer customer, LanguageType lang) {
        if (customer == null) {
            throw new CouponValidationException(CouponErrorConstants.CUSTOMER_NOT_FOUND_CODE,
                    LanguageType.ARB.equals(lang) ? CouponErrorConstants.CUSTOMER_NOT_FOUND_MSG_ARABIC : CouponErrorConstants.CUSTOMER_NOT_FOUND_MSG);
        }
    }

    private void validateCouponExistence(CouponResponseDTO coupon, LanguageType lang) throws CouponValidationException {
        if (coupon == null) {
            throw new CouponValidationException(CouponErrorConstants.COUPON_NOT_FOUND_CODE,
                    LanguageType.ARB.equals(lang) ? CouponErrorConstants.COUPON_NOT_FOUND_MSG_ARABIC : CouponErrorConstants.COUPON_NOT_FOUND_MSG);
        }
    }

    private void validateCouponStatus(CouponResponseDTO coupon, LanguageType lang) throws CouponValidationException {
        if (!coupon.getStatus()) {
            throw new CouponValidationException(CouponErrorConstants.COUPON_INACTIVE_CODE,
                    LanguageType.ARB.equals(lang) ? CouponErrorConstants.COUPON_INACTIVE_MSG_ARABIC : CouponErrorConstants.COUPON_INACTIVE_MSG);
        }
    }

    private void validateCouponExpiry(CouponResponseDTO coupon, LanguageType lang) throws CouponValidationException {
        if (coupon.getExpiry().before(new Date())) {
            throw new CouponValidationException(CouponErrorConstants.COUPON_EXPIRED_CODE,
                    LanguageType.ARB.equals(lang) ? CouponErrorConstants.COUPON_EXPIRED_MSG_ARABIC : CouponErrorConstants.COUPON_EXPIRED_MSG);
        }
    }

    private void validateSubtotal(Double subtotal, CouponResponseDTO coupon, LanguageType lang) throws CouponValidationException {
        if (subtotal < coupon.getMinSubTotal()) {
            throw new CouponValidationException(CouponErrorConstants.MIN_SUBTOTAL_NOT_MET_CODE,
                    LanguageType.ARB.equals(lang) ? CouponErrorConstants.MIN_SUBTOTAL_NOT_MET_MSG_ARABIC : CouponErrorConstants.MIN_SUBTOTAL_NOT_MET_MSG);
        }
    }

    private void validateUsageLimit(Long customerId, String phoneNumber, CouponResponseDTO coupon, LanguageType lang) throws CouponValidationException {
        LOGGER.info("Getting UsageCount By PhoneNumber & Customer ID");
        Integer usageCount = couponDetailService.countByCustomerIdAndPhoneNumberAndCoupon(customerId, phoneNumber, Coupon.fromDTO(coupon));
        LOGGER.info("UsageCount: {}", usageCount);

        if (usageCount >= Integer.parseInt(coupon.getUsageLimit())) {
            throw new CouponValidationException(CouponErrorConstants.USAGE_LIMIT_REACHED_CODE,
                    LanguageType.ARB.equals(lang) ? CouponErrorConstants.USAGE_LIMIT_REACHED_MSG_ARABIC : CouponErrorConstants.USAGE_LIMIT_REACHED_MSG);
        }
    }
}
