package com.apricart.consumer.repository.jpa;

import com.apricart.consumer.enity.OnBoard;
import com.apricart.consumer.enity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OnBoardRepository extends JpaRepository<OnBoard, Long> {
}
