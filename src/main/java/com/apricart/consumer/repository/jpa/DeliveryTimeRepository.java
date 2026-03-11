package com.apricart.consumer.repository.jpa;

import com.apricart.consumer.enity.DeliveryTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryTimeRepository extends JpaRepository<DeliveryTime, Long> {
    List<DeliveryTime> findBySettingId(Long settingId);
}
