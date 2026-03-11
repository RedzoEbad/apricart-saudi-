package com.apricart.consumer.service.Impl;

import com.apricart.consumer.enity.Coupon;
import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.exceptions.ResourceNotFoundException;
import com.apricart.consumer.repository.jpa.CouponRepository;
import com.apricart.consumer.security.dto.request.CouponRequestDTO;
import com.apricart.consumer.security.dto.response.CouponResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.security.validation.CouponValidation;
import com.apricart.consumer.service.CouponService;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.apricart.consumer.security.constants.DateConstants.DATE_FORMAT_MILLI_SECONDS_PATTERN;

@Service
public class CouponServiceImpl implements CouponService {
    protected static final Logger LOGGER = LoggerFactory.getLogger(CouponServiceImpl.class);
    private static final String COUPON_ENG = "Coupon";
    private static final String COUPON_ARB = "القسيمة";
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private CouponValidation couponValidation;

    @Override
    public List<Coupon> getAllCoupons() {
        LOGGER.info("Getting all coupons");
        List<Coupon> allCoupons = couponRepository.findAll();
        return allCoupons.stream()
                .filter(Coupon::getStatus)
                .sorted(Comparator.comparingLong(Coupon::getId))
                .collect(Collectors.toList());
    }

    @Override
    public Coupon findById(Long id, LanguageType languageType) {
        LOGGER.info("Finding coupon by id: {}", id);
        return couponRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Coupon with id {} not found", id);
                    return LanguageType.ARB.equals(languageType) ? new ResourceNotFoundException(COUPON_ARB, id, true) : new ResourceNotFoundException(COUPON_ENG, id, false);
                });
    }

    @Override
    public Coupon findByCode(String code, LanguageType languageType) {
        LOGGER.info("Getting coupon by code");
        return couponRepository.findByCode(code).orElseThrow(() -> {
            LOGGER.error("Coupon with code {} not found", code);
            return LanguageType.ARB.equals(languageType) ? new ResourceNotFoundException(COUPON_ARB, languageType) : new ResourceNotFoundException(COUPON_ENG, languageType);
        });
    }

    @Override
    public List<Coupon> findByStatus(Boolean status) {
        LOGGER.info("Getting coupon by status");
        return couponRepository.findByStatus(status);
    }

    @Override
    public List<Coupon> findByMinSubTotal(Double minSubTotal) {
        LOGGER.info("Getting coupon by min sub total");
        return couponRepository.findByMinSubTotal(minSubTotal);
    }

    @Override
    public List<Coupon> findByUsageLimit(String usageLimit) {
        LOGGER.info("Getting coupon by usage limit");
        return couponRepository.findByUsageLimit(usageLimit);
    }

    @Override
    public List<Coupon> findByExpiry(String expiry) {
        LOGGER.info("Formatting received expiry date by expiry: {}", expiry);
        DateTimeFormatter formatter = DateTimeFormat.forPattern(DATE_FORMAT_MILLI_SECONDS_PATTERN);
        DateTime expiryDateTime = formatter.parseDateTime(expiry);
        LOGGER.info("Formatting received expiry date by expiry: {}", expiryDateTime.toDate());
        LOGGER.info("Getting coupon by expiry");
        return couponRepository.findByExpiry(expiryDateTime.toDate());
    }

    @Override
    public void addCoupon(CouponRequestDTO couponRequestDTO) {
        LOGGER.info("Adding coupon: {}", couponRequestDTO);
        Coupon coupon;
        coupon = Coupon.fromDTO(couponRequestDTO);
        save(coupon);
    }

    @Override
    public void deleteCoupon(Long id, LanguageType languageType) {
        LOGGER.info("Removing coupon for id: {}", id);
        if (!couponRepository.existsById(id)) {
            LOGGER.error("Coupon with id {} not found for removing", id);
            throw LanguageType.ARB.equals(languageType) ? new ResourceNotFoundException(COUPON_ARB, id, true) : new ResourceNotFoundException(COUPON_ENG, id, false);
        }
        couponRepository.deleteById(id);
    }

    @Override
    public Coupon save(Coupon coupon) {
        LOGGER.info("Saving coupon: {}", coupon);
        return couponRepository.save(coupon);
    }

    @Override
    public Coupon updateCoupon(CouponRequestDTO couponRequestDTO, LanguageType languageType) {
        LOGGER.info("Updating coupon: {}", couponRequestDTO);
        Coupon existingCoupon = findById(couponRequestDTO.getId(), languageType);

        existingCoupon.setCode(couponRequestDTO.getCode() == null ? existingCoupon.getCode() : couponRequestDTO.getCode());
        existingCoupon.setDescription(couponRequestDTO.getDescription() == null ? existingCoupon.getDescription() : couponRequestDTO.getDescription());
        existingCoupon.setExpiry(couponRequestDTO.getExpiry() == null ? existingCoupon.getExpiry() : couponRequestDTO.getExpiry());
        existingCoupon.setMinSubTotal(couponRequestDTO.getMinSubTotal() == null ? existingCoupon.getMinSubTotal() : couponRequestDTO.getMinSubTotal());
        existingCoupon.setUsageLimit(couponRequestDTO.getUsageLimit() == null ? existingCoupon.getUsageLimit() : couponRequestDTO.getUsageLimit());

        return save(existingCoupon);
    }


    @Override
    public void validateCoupon(CouponResponseDTO coupon, Customer customer, Double subtotal, LanguageType lang) {
        couponValidation.validateCoupon(coupon, customer, subtotal, lang);
    }

}
