package com.apricart.consumer.enity;

import com.apricart.consumer.security.dto.request.CouponRequestDTO;
import com.apricart.consumer.security.dto.response.CategoryResponseDTO;
import com.apricart.consumer.security.dto.response.CouponResponseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.apricart.consumer.security.constants.DateConstants.DATE_FORMAT_MILLI_SECONDS_PATTERN;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "COUPON")
public class Coupon extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String code;

    @Column
    private Double couponDiscount;

    @Column
    private String description;

    @Column
    private String arabicDescription;

    @Column
    private Double minSubTotal;

    @Column
    @JsonFormat(pattern = DATE_FORMAT_MILLI_SECONDS_PATTERN)
    private Date expiry;

    @Column
    private String usageLimit;

    @Column
    private Boolean status;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "coupon_detail_id")
//    private CouponDetail couponDetail;

    public static CouponResponseDTO toDTO(Coupon coupon) {
        return CouponResponseDTO.builder()
                .id(coupon.getId())
                .code(coupon.getCode())
                .description(coupon.getDescription())
                .couponDiscount(coupon.getCouponDiscount())
                .arabicDescription(coupon.getArabicDescription())
                .expiry(coupon.getExpiry())
                .minSubTotal(coupon.getMinSubTotal())
                .usageLimit(coupon.getUsageLimit())
                .status(coupon.getStatus())
//                .couponDetailId(coupon.getCouponDetail().getId())
                .build();
    }

    public static List<CouponResponseDTO> toDTOList(List<Coupon> coupons) {
        return coupons.stream()
                .map(Coupon::toDTO)
                .filter(dto -> dto.getExpiry() != null)
                .sorted(Comparator.comparing(CouponResponseDTO::getExpiry).reversed())
                .collect(Collectors.toList());
    }

    public static Coupon fromDTO(CouponRequestDTO dto) {
        return Coupon.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .couponDiscount(dto.getCouponDiscount())
                .description(dto.getDescription())
                .arabicDescription(dto.getArabicDescription())
                .expiry(dto.getExpiry())
                .minSubTotal(dto.getMinSubTotal())
                .usageLimit(dto.getUsageLimit())
                .status(dto.getStatus())
                .build();
    }
    public static Coupon fromDTO(CouponResponseDTO dto) {
        return Coupon.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .couponDiscount(dto.getCouponDiscount())
                .description(dto.getDescription())
                .arabicDescription(dto.getArabicDescription())
                .expiry(dto.getExpiry())
                .minSubTotal(dto.getMinSubTotal())
                .usageLimit(dto.getUsageLimit())
                .status(dto.getStatus())
                .build();
    }

}
