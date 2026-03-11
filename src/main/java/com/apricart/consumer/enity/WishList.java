package com.apricart.consumer.enity;

import com.apricart.consumer.security.dto.request.WishListRequestDTO;
import com.apricart.consumer.security.dto.response.WishListResponseDTO;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Table(name = "WISH_LIST")
public class WishList extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public static WishListResponseDTO toDTO(WishList wishList) {
        return WishListResponseDTO.builder()
                .id(wishList.getId())
                .customerId(wishList.getCustomer().getId())
                .productId(wishList.getProduct().getId())
                .build();
    }

    public static List<WishListResponseDTO> toDTOList(List<WishList> wishLists) {
        return wishLists.stream()
                .map(WishList::toDTO)
                .collect(Collectors.toList());
    }

    public static WishList fromDTO(WishListRequestDTO dto) {
        return WishList.builder()
                .id(dto.getId())
                .build();
    }
}
