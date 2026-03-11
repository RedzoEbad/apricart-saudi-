package com.apricart.consumer.repository.jpa;

import com.apricart.consumer.enity.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, Long> {

    Option findByKey(String key);
}
