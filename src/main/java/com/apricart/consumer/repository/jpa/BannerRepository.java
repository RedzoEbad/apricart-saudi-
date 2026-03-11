package com.apricart.consumer.repository.jpa;

import com.apricart.consumer.enity.Banner;
import com.apricart.consumer.security.enums.LevelType;
import com.apricart.consumer.security.enums.PositionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long> {
    List<Banner> findByPosition(PositionType positionType);
    List<Banner> findByLevel(LevelType levelType);
}
