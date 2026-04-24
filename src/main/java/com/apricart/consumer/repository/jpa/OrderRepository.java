package com.apricart.consumer.repository.jpa;

import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.enity.CustomerAddress;
import com.apricart.consumer.enity.Orders;
import com.apricart.consumer.security.enums.OrderType;
import com.apricart.consumer.security.enums.PaymentModeType;
import com.apricart.consumer.security.enums.PaymentStatusType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, String> {

    List<Orders> findByCustomer(Customer customer);

    List<Orders> findByPaymentMode(PaymentModeType paymentModeType);

    List<Orders> findByPaymentStatus(PaymentStatusType paymentStatusType);

    List<Orders> findByOrderStatus(OrderType orderType);

    List<Orders> findByShippingCharge(String shippingCharge);

    Boolean existsByCustomerAddress(CustomerAddress customer);

}
