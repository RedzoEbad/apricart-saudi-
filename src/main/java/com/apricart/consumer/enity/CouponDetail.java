package com.apricart.consumer.enity;

import com.apricart.consumer.security.dto.request.CouponDetailRequestDTO;
import com.apricart.consumer.security.dto.response.CouponDetailResponseDTO;
import com.apricart.consumer.security.dto.response.CouponResponseDTO;
import lombok.*;

import javax.persistence.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "COUPON_DETAIL")
public class CouponDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long customerId;

    @Column
    private String phoneNumber;

    @Column
    private Long warehouseId;

    @Column
    private String orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;


    public static CouponDetailResponseDTO toDTO(CouponDetail couponDetail) {
        return CouponDetailResponseDTO.builder()
                .id(couponDetail.getId())
                .customerId(couponDetail.getCustomerId())
                .phoneNumber(couponDetail.getPhoneNumber())
                .warehouseId(couponDetail.getWarehouseId())
                .orderId(couponDetail.getOrderId())
                .couponId(couponDetail.getCoupon().getId())
                .build();
    }

    public static List<CouponDetailResponseDTO> toDTOList(List<CouponDetail> couponDetails) {
        return couponDetails.stream()
                .map(CouponDetail::toDTO)
                .filter(dto -> dto.getId() != null)
                .sorted(Comparator.comparing(CouponDetailResponseDTO::getId).reversed())
                .collect(Collectors.toList());
    }

    public static CouponDetail fromDTO(CouponDetailRequestDTO dto) {
        return CouponDetail.builder()
                .id(dto.getId())
                .customerId(dto.getCustomerId())
                .phoneNumber(dto.getPhoneNumber())
                .warehouseId(dto.getWarehouseId())
                .orderId(dto.getOrderId())
                .build();
    }

}

