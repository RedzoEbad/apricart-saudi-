package com.apricart.consumer.service.Impl;

import com.apricart.consumer.enity.Coupon;
import com.apricart.consumer.enity.CouponDetail;
import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.exceptions.ResourceNotFoundException;
import com.apricart.consumer.repository.jpa.CouponDetailRepository;
import com.apricart.consumer.security.dto.request.CouponDetailRequestDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.CouponDetailService;
import com.apricart.consumer.service.CouponService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.COUPON_USAGE_LIMIT_EXCEEDS_ARABIC;
import static com.apricart.consumer.security.constants.ResponseMessage.COUPON_USAGE_LIMIT_EXCEEDS;

@Service
public class CouponDetailServiceImpl implements CouponDetailService {
    protected static final Logger LOGGER = LoggerFactory.getLogger(CouponDetailServiceImpl.class);

    @Autowired
    private CouponDetailRepository couponDetailRepository;
    @Autowired
    private CouponService couponService;
    private static final String COUPON_DETAIL_ENG = "Coupon Detail";
    private static final String COUPON_DETAIL_ARB = "القسيمة تفاصيل";
    @Override
    public CouponDetail findById(Long id, LanguageType languageType) {
        LOGGER.info("Finding coupon detail by id: {}", id);
        return couponDetailRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Coupon detail with id {} not found", id);
                    return LanguageType.ARB.equals(languageType) ? new ResourceNotFoundException(COUPON_DETAIL_ARB, id, true) : new ResourceNotFoundException(COUPON_DETAIL_ENG, id, false);
                });
    }

    @Override
    public CouponDetail findByCoupon(Coupon coupon) {
        LOGGER.info("Getting coupon detail by coupon");
        return couponDetailRepository.findByCoupon(coupon);
    }

    @Override
    public Boolean verifyUsageLimit(Long id, LanguageType lang, LanguageType languageType) {
        LOGGER.info("Getting couponDetail used count");
        CouponDetail couponDetail = findById(id, languageType);
        if (couponDetail.getCoupon().getUsageLimit() != null && couponDetail.getCoupon().getUsageLimit().equals("3")) {
            if (couponDetail.getCustomerId() != null && couponDetail.getPhoneNumber() != null) {
                int usageCount = couponDetailRepository.countByCustomerIdAndPhoneNumberAndCoupon(couponDetail.getCustomerId(), couponDetail.getPhoneNumber(), couponDetail.getCoupon());
                if (usageCount >= 2) {
                    throw new IllegalArgumentException(LanguageType.ARB.equals(lang) ? COUPON_USAGE_LIMIT_EXCEEDS_ARABIC : COUPON_USAGE_LIMIT_EXCEEDS);
                }
            } else if (couponDetail.getCoupon().getUsageLimit() != null  && couponDetail.getPhoneNumber() == null) {
                int usageCount = couponDetailRepository.countByCustomerIdAndCoupon(couponDetail.getCustomerId(), couponDetail.getCoupon());
                if (usageCount >= 2) {
                    throw new IllegalArgumentException(LanguageType.ARB.equals(lang) ? COUPON_USAGE_LIMIT_EXCEEDS_ARABIC : COUPON_USAGE_LIMIT_EXCEEDS);
                }
            }
        }
        return true;
    }

    @Override
    public List<CouponDetail> findByWarehouseId(Long productWarehouseId) {
        LOGGER.info("Getting coupon details by WarehouseId");
        return couponDetailRepository.findByWarehouseId(productWarehouseId);
    }
    @Override
    public List<CouponDetail> findByOrderId(String orderId) {
        LOGGER.info("Getting coupon details by OrderId");
        return couponDetailRepository.findByOrderId(orderId);
    }

    @Override
    public void addCouponDetail(CouponDetailRequestDTO couponDetailRequestDTO, Customer customer, LanguageType languageType) {
        LOGGER.info("Adding coupon detail: {}", couponDetailRequestDTO);
        CouponDetail couponDetail;
        couponDetail = CouponDetail.fromDTO(couponDetailRequestDTO);
        couponDetail.setCustomerId(customer.getId());
        couponDetail.setCoupon(couponService.findById(couponDetailRequestDTO.getCouponId(), languageType));
        save(couponDetail);
    }

    @Override
    public Integer countByCustomerIdAndCoupon(Long customerId,  Coupon coupon) {
        LOGGER.info("Getting coupon usage count by Customer Id");
        return couponDetailRepository.countByCustomerIdAndCoupon(customerId, coupon);
    }

    @Override
    public Integer countByCustomerIdAndPhoneNumberAndCoupon(Long customerId, String phoneNumber, Coupon coupon) {
        LOGGER.info("Getting coupon usage count by phoneNumber & Customer Id");
        return couponDetailRepository.countByCustomerIdAndPhoneNumberAndCoupon(customerId, phoneNumber, coupon);
    }

    @Override
    public CouponDetail updateCouponDetail(CouponDetailRequestDTO couponDetailRequestDTO, LanguageType languageType) {
        LOGGER.info("Updating coupon detail: {}", couponDetailRequestDTO);
        CouponDetail existingCouponDetail = findById(couponDetailRequestDTO.getId(), languageType);

        existingCouponDetail.setPhoneNumber(couponDetailRequestDTO.getPhoneNumber() == null ? existingCouponDetail.getPhoneNumber() : couponDetailRequestDTO.getPhoneNumber());
        existingCouponDetail.setOrderId(couponDetailRequestDTO.getOrderId() == null ? existingCouponDetail.getOrderId() : couponDetailRequestDTO.getOrderId());
        existingCouponDetail.setWarehouseId(couponDetailRequestDTO.getWarehouseId() == null ? existingCouponDetail.getWarehouseId() : couponDetailRequestDTO.getWarehouseId());
        existingCouponDetail.setCoupon(couponDetailRequestDTO.getCouponId() == null ? existingCouponDetail.getCoupon() : couponService.findById(couponDetailRequestDTO.getCouponId(), languageType));

        return save(existingCouponDetail);
    }
    public CouponDetail save(CouponDetail couponDetail) {
        LOGGER.info("Saving coupon detail: {}", couponDetail);
        return couponDetailRepository.save(couponDetail);
    }
}
