package com.apricart.consumer.repository.jpa;

import com.apricart.consumer.enity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Brand findBrandByName(String name);
    Brand findBrandByArabicName(String arabicName);
}
