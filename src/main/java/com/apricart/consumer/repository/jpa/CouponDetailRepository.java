package com.apricart.consumer.repository.jpa;

import com.apricart.consumer.enity.Coupon;
import com.apricart.consumer.enity.CouponDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponDetailRepository extends JpaRepository<CouponDetail, Long> {

    CouponDetail findByCoupon(Coupon coupon);
    List<CouponDetail> findByWarehouseId(Long warehouseId);
    List<CouponDetail> findByOrderId(String orderId);
    List<CouponDetail> findByCustomerId(Long customerId);
    Integer countByCustomerIdAndCoupon(Long customerId, Coupon coupon);
    Integer countByCustomerIdAndPhoneNumberAndCoupon(Long customerId, String phoneNumber, Coupon coupon);

}

