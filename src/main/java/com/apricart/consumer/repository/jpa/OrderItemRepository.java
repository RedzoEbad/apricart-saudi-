package com.apricart.consumer.repository.jpa;

import com.apricart.consumer.enity.Orders;
import com.apricart.consumer.enity.OrderItem;
import com.apricart.consumer.enity.ProductWarehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrders(Orders orders);
    List<OrderItem> findByProductWarehouse(ProductWarehouse productWarehouse);
}
