package com.apricart.consumer.service;


import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.enity.CustomerAddress;
import com.apricart.consumer.enity.Orders;
import com.apricart.consumer.exceptions.OrderException;
import com.apricart.consumer.security.dto.request.OrderRequestDTO;
import com.apricart.consumer.security.dto.response.OrderResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.security.enums.OrderType;
import com.apricart.consumer.security.enums.PaymentModeType;
import com.apricart.consumer.security.enums.PaymentStatusType;

import java.util.List;

public interface OrderService {
    List<Orders> getAllOrders();

    Orders findById(String id, LanguageType languageType);
    List<Orders> findByCustomer(Customer customer);
    List<Orders> findByOrderStatus(OrderType orderType);
    List<Orders> findByPaymentStatus(PaymentStatusType paymentStatusType);
    List<Orders> findByPaymentMode(PaymentModeType paymentModeType);
    List<Orders> findByShippingCharge(String shippingCharge);

    Boolean existsByCustomerAddress(CustomerAddress customer);

    Orders save(Orders orders);

    OrderResponseDTO generateOrder(OrderRequestDTO orderRequestDTO, Customer customer, LanguageType lang);
    Orders updateOrder(OrderRequestDTO orderRequestDTO, LanguageType languageType);

    Orders updateOrderStatus(String id, OrderType orderType, LanguageType languageType);
    Orders updatePaymentStatus(String id, PaymentStatusType paymentStatusType, LanguageType languageType);

    void cancelOrder(String id, LanguageType languageType) throws OrderException;
    void sendOrderEmail(Orders orders, LanguageType lang);
}
