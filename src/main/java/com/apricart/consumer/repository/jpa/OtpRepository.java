package com.apricart.consumer.repository.jpa;

import com.apricart.consumer.enity.Otp;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface OtpRepository extends CrudRepository<Otp, Long> {

    Optional<Otp> findByPhoneNumber(String phoneNumber);

    @Query("SELECT o.otp FROM Otp o WHERE o.phoneNumber = ?1")
    String findOtpByPhoneNumber(String phoneNumber);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO otp( id, phoneNumber, otp) VALUES (?1, ?2, ?3) ON CONFLICT DO UPDATE SET otp=?3;", nativeQuery = true)
    Integer setOTP(int id, String phoneNumber, String otp);

    @Transactional
    @Modifying
    @Query("UPDATE Otp o SET o.otp = '' WHERE o.phoneNumber = ?1")
    Integer updateOTP(String email);


    @Query(value ="select id from otp order by id DESC limit 1", nativeQuery = true)
    int findLastEntry();

    Optional<Otp> findTopByOrderByIdDesc();



}
