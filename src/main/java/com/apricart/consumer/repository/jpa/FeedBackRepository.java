package com.apricart.consumer.repository.jpa;

import com.apricart.consumer.enity.FeedBack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedBackRepository extends JpaRepository<FeedBack, Long> {
    List<FeedBack> findByPhoneNumber(String phoneNumber);
}
