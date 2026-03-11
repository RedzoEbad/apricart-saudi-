package com.apricart.consumer.enity;

import lombok.*;

import javax.persistence.*;

/**
 * Created on Feb, 2024
 *
 * @author Kashaf Arshad
 */

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "OTP")
public class Otp extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String phoneNumber;

    @Column
    private String otp;
}