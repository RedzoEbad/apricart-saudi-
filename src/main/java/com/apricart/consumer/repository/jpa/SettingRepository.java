package com.apricart.consumer.repository.jpa;

import com.apricart.consumer.enity.Setting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SettingRepository  extends JpaRepository<Setting, Long> {
    List<Setting> findByWarehouseId(Long warehouseId);
}
