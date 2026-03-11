package com.apricart.consumer.repository.jpa;

import com.apricart.consumer.enity.Cart;
import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.enity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findCartByCustomer(Customer customer);
    Cart findCartByCustomerAndProductIdAndProductWarehouseId(Customer customer, Long productId, Long productWarehouseId);
    void deleteByCustomer(Customer customer);
    void deleteByCustomerAndProductId(Customer customer, Long productId);
}
