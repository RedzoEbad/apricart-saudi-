package com.apricart.consumer.repository.jpa;

import com.apricart.consumer.enity.Coupon;
import com.apricart.consumer.enity.ProductWarehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByCode(String code);
    List<Coupon> findByStatus(Boolean status);
    List<Coupon> findByMinSubTotal(Double minSubTotal);
    List<Coupon> findByUsageLimit(String usageLimit);
    List<Coupon> findByExpiry(Date expiry);
//    List<Coupon> findByProductWarehouse(ProductWarehouse productWarehouse);
}
