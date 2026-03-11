package com.apricart.consumer.enity;

import com.apricart.consumer.security.dto.request.CartRequestDTO;
import com.apricart.consumer.security.dto.response.CartResponseDTO;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart", uniqueConstraints = @UniqueConstraint(columnNames = {"customer_id", "product_id"}))
public class Cart extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_warehouse_id")
    private ProductWarehouse productWarehouse;

    public static CartResponseDTO toDTO(Cart cart) {
        return CartResponseDTO.builder()
                .id(cart.getId())
                .quantity(cart.getQuantity())
                .productId(cart.getProduct().getId())
                .product(Product.toDTO(cart.getProduct()))
                .customerId(cart.getCustomer().getId())
                .productWarehouseId(cart.getProductWarehouse().getId())
                .build();
    }
    public static List<CartResponseDTO> toDTOList(List<Cart> carts) {
        return carts.stream()
                .map(Cart::toDTO)
                .collect(Collectors.toList());
    }
    public static Cart fromDTO(CartRequestDTO dto) {
        return Cart.builder()
                .id(dto.getId())
                .quantity(dto.getQuantity())
                .build();
    }

}
