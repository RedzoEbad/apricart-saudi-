package com.apricart.consumer.enity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
public class BaseEntity {

    @Column(name = "create_date_time")
    @CreationTimestamp
    public LocalDateTime createDateTime;

    @Column(name = "update_date_time")
    @UpdateTimestamp
    public LocalDateTime updateDateTime;

}
