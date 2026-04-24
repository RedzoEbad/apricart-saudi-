package com.apricart.consumer.service.Impl;

import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.enity.CustomerAddress;
import com.apricart.consumer.enity.Orders;
import com.apricart.consumer.exceptions.OrderException;
import com.apricart.consumer.exceptions.ResourceNotFoundException;
import com.apricart.consumer.mapper.OrderMapper;
import com.apricart.consumer.repository.jpa.OrderRepository;
import com.apricart.consumer.security.constants.ArabicResponseMessages;
import com.apricart.consumer.security.constants.ResponseMessage;
import com.apricart.consumer.security.dto.request.OrderItemRequestDTO;
import com.apricart.consumer.security.dto.request.OrderRequestDTO;
import com.apricart.consumer.security.dto.request.PendingOrderRequestDTO;
import com.apricart.consumer.security.dto.response.OrderResponseDTO;
import com.apricart.consumer.security.enums.*;
import com.apricart.consumer.service.OrderItemService;
import com.apricart.consumer.service.OrderService;
import com.apricart.consumer.service.PendingOrderService;
import com.apricart.consumer.utils.EmailUtils;
import com.apricart.consumer.utils.OrderUtils;
import com.google.protobuf.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.ORDER_CANCEL_TIME_EXPIRED_ARABIC;
import static com.apricart.consumer.security.constants.ArabicResponseMessages.ORDER_STATUS_NULL_ARABIC;
import static com.apricart.consumer.security.constants.Constants.*;
import static com.apricart.consumer.security.constants.ResponseMessage.ORDER_CANCEL_TIME_EXPIRED;
import static com.apricart.consumer.security.constants.ResponseMessage.ORDER_STATUS_NULL;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    protected static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderUtils orderUtils;

    @Autowired
    private PendingOrderService pendingOrderService;

    @Autowired
    EmailUtils emailUtils;
    private static final String ORDER_ENG = "Order";
    private static final String ORDER_ARB = "الطلب";

    @Override
    public List<Orders> getAllOrders() {
        LOGGER.info("Getting all orders");
        return orderRepository.findAll();
    }

    @Override
    public Orders findById(String id, LanguageType languageType) {
        LOGGER.info("Finding order by id: {}", id);
        return orderRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Order with id {} not found", id);
                    return LanguageType.ARB.equals(languageType) ? new ResourceNotFoundException(ORDER_ARB, languageType) : new ResourceNotFoundException(ORDER_ENG, languageType);
                });
    }

    @Override
    public List<Orders> findByCustomer(Customer customer) {
        LOGGER.info("Getting orders by customer: {}", customer);
        return orderRepository.findByCustomer(customer);
    }

    @Override
    public List<Orders> findByOrderStatus(OrderType orderType) {
        LOGGER.info("Getting orders by order status: {}", orderType);
        return orderRepository.findByOrderStatus(orderType);
    }

    @Override
    public List<Orders> findByPaymentStatus(PaymentStatusType paymentStatusType) {
        LOGGER.info("Getting orders by payment status: {}", paymentStatusType);
        return orderRepository.findByPaymentStatus(paymentStatusType);
    }

    @Override
    public List<Orders> findByPaymentMode(PaymentModeType paymentModeType) {
        LOGGER.info("Finding orders by payment mode: {}", paymentModeType);
        return orderRepository.findByPaymentMode(paymentModeType);
    }

    @Override
    public List<Orders> findByShippingCharge(String shippingCharge) {
        LOGGER.info("Finding orders by shipping charges: {}", shippingCharge);
        return orderRepository.findByShippingCharge(shippingCharge);
    }


    @Override
    public Boolean existsByCustomerAddress(CustomerAddress customer) {
        LOGGER.info("Checking existence of orders for customer address: {}", customer);
        return orderRepository.existsByCustomerAddress(customer);
    }

    @Override
    public Orders save(Orders orders) {

        LOGGER.info("Saving order: {}", orders);
        return orderRepository.save(orders);
    }

    @Override
    public OrderResponseDTO generateOrder(OrderRequestDTO orderRequestDTO, Customer customer, LanguageType lang) {
        try {
            isInStockProduct(orderRequestDTO.getOrderItems(), lang);
            Orders orders = createOrder(orderRequestDTO, customer, lang);
            saveOrder(orders);
            addPendingOrder(orders);
            saveOrderItems(orders, lang);
            updateOrderItemsQuantity(orders, lang);
            sendOrderEmail(orders, lang);

            return Orders.toDTO(orders, orderMapper, lang);

        } catch (Exception e) {
            LOGGER.error("CRITICAL ORDER ERROR: {}", e.getMessage(), e);
            throw new OrderException("Order failed because: " + e.getMessage());
        }
    }

    private Orders createOrder(OrderRequestDTO orderRequestDTO, Customer customer, LanguageType lang) {
        Orders orders = orderUtils.generate(orderRequestDTO, customer, lang);
        if (orderRequestDTO.getOrderStatus() != null) {
            String translation = OrderTypeArabic.getTranslationForStatus(orderRequestDTO.getOrderStatus());
            orders.setOrderTypeArabic(translation);
        } else {
            throw new ResourceNotFoundException(LanguageType.ARB.equals(lang) ? ORDER_STATUS_NULL_ARABIC : ORDER_STATUS_NULL, true);
        }
        return orders;
    }

    private void saveOrder(Orders orders) {
        save(orders);
    }

    private void addPendingOrder(Orders orders) throws ServiceException {
        pendingOrderService.addPendingOrder(
                PendingOrderRequestDTO
                        .builder()
                        .orderId(orders.getId())
                        .status(Boolean.TRUE)
                        .build()
        );
    }

    private void saveOrderItems(Orders orders, LanguageType lang) {
        LOGGER.info("Saving order items: {}", orders);
        orders.getOrderItems().forEach(orderItem -> orderItemService.saveOrderItem(orderItem, orders));
    }

    private void updateOrderItemsQuantity(Orders orders, LanguageType lang) {
        LOGGER.info("Updating Order Items: {}", orders);
        orders.getOrderItems().forEach(orderItem -> orderItemService.updateOrderItemQuantity(orderItem, lang, Boolean.FALSE));
    }
    private void isInStockProduct(List<OrderItemRequestDTO> orderItems, LanguageType lang) {
        for (OrderItemRequestDTO orderItem : orderItems) {
            if (!orderItemService.isInStockItem(orderItem, lang)) {
                String errorMessage;

                if (LanguageType.ARB.equals(lang)) {
                    errorMessage = String.format(ArabicResponseMessages.PRODUCT_OUT_OF_STOCK_ARABIC, orderItem.getArabicTitle());
                } else {
                    errorMessage = String.format(ResponseMessage.PRODUCT_OUT_OF_STOCK, orderItem.getTitle());
                }
                throw new OrderException(errorMessage);
            }
        }
    }

    @Override
    public void sendOrderEmail(Orders orders, LanguageType lang) {
        boolean emailSent = false;
        try {
            EmailUtils.SUBJECT = String.format(EMAIL_SUBJECT_CONFIRM_ORDER, orders.getId());
            emailUtils.sendAddOrderEmail(orders);
            emailSent = true;
        } catch (Exception emailException) {
            LOGGER.error("Error sending email for order : {}", emailException.getMessage());
        }

        if (emailSent) {
            LOGGER.info("Updating Pending Order: {}", orders);
            pendingOrderService.updatePendingOrder(
                    PendingOrderRequestDTO
                            .builder()
                            .orderId(orders.getId())
                            .status(Boolean.FALSE)
                            .build(), lang
            );
        }
    }


    @Override
    public Orders updateOrder(OrderRequestDTO orderRequestDTO, LanguageType languageType) throws OrderException {
        try {
            LOGGER.info("Updating order: {}", orderRequestDTO);

            Orders existingOrders = findById(orderRequestDTO.getId(), languageType);
            existingOrders = orderMapper.updateOrderFromDTO(existingOrders, orderRequestDTO, languageType);
            return save(existingOrders);
        } catch (Exception e) {
            LOGGER.error("Error updating order: {}", e.getMessage());
            throw new OrderException(e.getMessage());
        }
    }

    @Override
    public Orders updateOrderStatus(String id, OrderType orderType, LanguageType languageType) throws OrderException {
        try {
            LOGGER.info("Updating order status for order id: {}", id);
            Orders existingOrders = findById(id, languageType);
            existingOrders.setOrderStatus(orderType == null ? existingOrders.getOrderStatus() : orderType);
            String translation = OrderTypeArabic.getTranslationForStatus(existingOrders.getOrderStatus());
            existingOrders.setOrderTypeArabic(translation);
            save(existingOrders);

            return existingOrders;
        } catch (Exception e) {
            LOGGER.error("Error updating order status: {}", e.getMessage());
            throw new OrderException(e.getMessage());
        }
    }

    @Override
    public Orders updatePaymentStatus(String id, PaymentStatusType paymentStatusType, LanguageType languageType) throws OrderException {
        try {
            LOGGER.info("Updating payment status for order id: {}", id);
            Orders existingOrders = findById(id, languageType);
            existingOrders.setPaymentStatus(paymentStatusType == null ? existingOrders.getPaymentStatus() : paymentStatusType);
            save(existingOrders);
            return existingOrders;
        } catch (Exception e) {
            LOGGER.error("Error updating payment status: {}", e.getMessage());
            throw new OrderException(e.getMessage());

        }
    }

    @Override
    public void cancelOrder(String id, LanguageType lang) throws OrderException {
        try {
            Orders existingOrders = findById(id, lang);

            Duration duration = Duration.between(existingOrders.getCreateDateTime(), ZonedDateTime.now());

            if (duration.getSeconds() < CANCEL_ORDER_TIME_IN_SEC) {
                LOGGER.info("Cancelling order status for id: {}", id);
                existingOrders.setStatus(false);
                existingOrders.setPaymentStatus(PaymentStatusType.UNPAID);
                existingOrders.setOrderStatus(OrderType.CANCELLED);
                String translation = OrderTypeArabic.getTranslationForStatus(existingOrders.getOrderStatus());
                existingOrders.setOrderTypeArabic(translation);
                existingOrders.getOrderItems().forEach(orderItem -> orderItemService.updateOrderItemQuantity(orderItem, lang, Boolean.TRUE));

                save(existingOrders);
                EmailUtils.SUBJECT = String.format(EMAIL_SUBJECT_CANCEL_ORDER, existingOrders.getId());
                emailUtils.sendOrderCancelEmail(existingOrders);
            } else {
                throw new OrderException(LanguageType.ARB.equals(lang) ? ORDER_CANCEL_TIME_EXPIRED_ARABIC : ORDER_CANCEL_TIME_EXPIRED);
            }
        } catch (Exception e) {
            LOGGER.error("Error cancelling order: {}", e.getMessage());
            throw new OrderException(e.getMessage());
        }
    }


}