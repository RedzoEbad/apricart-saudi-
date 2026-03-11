package com.apricart.consumer.repository.jpa;

import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.enity.Product;
import com.apricart.consumer.enity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishListRepository extends JpaRepository<WishList, Long> {
    List<WishList> findWishListByCustomer(Customer customer);
    Optional<WishList> findWishListByCustomerAndProduct(Customer customer, Product product);
    void deleteByCustomer(Customer customer);
    void deleteByCustomerAndProductId(Customer customer, Long productId);
    void deleteByProductId(Long productId);
    boolean existsByCustomerIdAndProductId(Long customerId, Long productId);
}
