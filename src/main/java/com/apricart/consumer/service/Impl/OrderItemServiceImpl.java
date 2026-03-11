package com.apricart.consumer.service.Impl;

import com.apricart.consumer.enity.OrderItem;
import com.apricart.consumer.enity.Orders;
import com.apricart.consumer.enity.ProductWarehouse;
import com.apricart.consumer.exceptions.ResourceNotFoundException;
import com.apricart.consumer.repository.jpa.OrderItemRepository;
import com.apricart.consumer.security.dto.request.OrderItemRequestDTO;
import com.apricart.consumer.security.dto.request.ProductWarehouseRequestDTO;
import com.apricart.consumer.security.dto.response.ProductWarehouseResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.OrderItemService;
import com.apricart.consumer.service.OrderService;
import com.apricart.consumer.service.ProductWarehouseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    protected static final Logger LOGGER = LoggerFactory.getLogger(OrderItemServiceImpl.class);

    private static final String ORDER_ITEM_ENG = "Order Item";
    private static final String ORDER_ITEM_ARB = "عنصر الطلب";

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductWarehouseService productWarehouseService;

    @Override
    public List<OrderItem> getAllOrderItems() {
        LOGGER.info("Getting all order items");
        return orderItemRepository.findAll();
    }

    @Override
    public OrderItem findById(Long id, LanguageType languageType) {
        LOGGER.info("Finding order item by id: {}", id);
        return orderItemRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Order item with id {} not found", id);
                    return LanguageType.ARB.equals(languageType) ? new ResourceNotFoundException(ORDER_ITEM_ARB, id, true) : new ResourceNotFoundException(ORDER_ITEM_ENG, id, false);
                });
    }

    @Override
    public List<OrderItem> findByOrder(Orders orders) {
        LOGGER.info("Getting order items by order Id: {}", orders.getId());
        return orderItemRepository.findByOrders(orders);
    }

    @Override
    public List<OrderItem> findByProductWarehouse(ProductWarehouse productWarehouse) {
        LOGGER.info("Getting order items by product warehouse Id: {}", productWarehouse.getId());
        return orderItemRepository.findByProductWarehouse(productWarehouse);
    }

    @Override
    public Double calculateTaxAmount(Long id, LanguageType languageType) {
        LOGGER.info("Calculating Tax amount for order item id: {}", id);
        OrderItem existingOrderItem = findById(id, languageType);
        return existingOrderItem.getTaxAmount();
    }

    @Override
    public Double calculateTotalAmount(Long id, LanguageType languageType) {
        LOGGER.info("Calculating Total amount for order item id: {}", id);
        OrderItem existingOrderItem = findById(id, languageType);
        return existingOrderItem.getTotalAmount();
    }

    @Override
    public void saveOrderItem(OrderItemRequestDTO orderItemRequestDTO, LanguageType languageType) {
        LOGGER.info("Adding order item: {}", orderItemRequestDTO);
        OrderItem orderItem;
        orderItem = OrderItem.fromDTO(orderItemRequestDTO);
        orderItem.setProductWarehouse(productWarehouseService.findById(orderItemRequestDTO.getProductWarehouseId(), languageType));
        orderItem.setOrders(orderService.findById(orderItemRequestDTO.getOrderId(), languageType));
        save(orderItem);
    }

    @Override
    public void saveOrderItem(OrderItem orderItem, Orders orders) {
        LOGGER.info("Adding order item: {}", orderItem);

        orderItem.setProductWarehouse(orderItem.getProductWarehouse());
        orderItem.setOrders(orders);
        save(orderItem);
    }

    @Override
    public void updateOrderItemQuantity(OrderItem orderItem, LanguageType languageType, boolean isOrderCancelled) {
        try {
            ProductWarehouseResponseDTO productWarehouse = productWarehouseService.findByProductId(
                    orderItem.getProductWarehouse().getProduct().getId(), languageType);

            int orderQuantity = Integer.parseInt(orderItem.getQuantity());

            int newQuantityInStock;
            if (isOrderCancelled) {
                newQuantityInStock = productWarehouse.getQuantityInStock() + orderQuantity;
            } else {
                newQuantityInStock = productWarehouse.getQuantityInStock() - orderQuantity;
                newQuantityInStock = Math.max(newQuantityInStock, 0);
            }

            boolean inStock = newQuantityInStock > 0;

            productWarehouse.setQuantityInStock(newQuantityInStock);
            productWarehouse.setInStock(inStock);

            ProductWarehouseRequestDTO updateRequest = ProductWarehouseRequestDTO.builder()
                    .id(productWarehouse.getId())
                    .currentRate(productWarehouse.getCurrentRate())
                    .inStock(productWarehouse.getInStock())
                    .productId(productWarehouse.getProduct().getId())
                    .rate(productWarehouse.getRate())
                    .taxId(productWarehouse.getTaxId())
                    .quantityInStock(productWarehouse.getQuantityInStock())
                    .warehouseId(productWarehouse.getWarehouseId())
                    .specialRate(productWarehouse.getSpecialRate())
                    .priceListId(productWarehouse.getPriceListId())
                    .categoryId(productWarehouse.getCategoryId())
                    .subCategoryId(productWarehouse.getSubCategoryId())
                    .isActive(productWarehouse.getIsActive())
                    .build();

            productWarehouseService.updateProductWarehouse(updateRequest, languageType);

        } catch (NumberFormatException e) {
            LOGGER.error("Invalid quantity format: " + orderItem.getQuantity());
        } catch (Exception e) {
           LOGGER.error(e.getMessage());
        }
    }

    @Override
    public boolean isInStockItem(OrderItemRequestDTO orderItem, LanguageType languageType) {
        ProductWarehouse productWarehouse = productWarehouseService.findById(orderItem.getProductWarehouseId(), languageType);
        return productWarehouse.getInStock() && productWarehouse.getQuantityInStock() > 0;
    }


    @Override
    public OrderItem updateOrderItem(OrderItemRequestDTO orderItemRequestDTO, LanguageType languageType) {
        LOGGER.info("Updating order Item: {}", orderItemRequestDTO);
        OrderItem existingOrderItem = findById(orderItemRequestDTO.getId(), languageType);

        existingOrderItem.setTitle(orderItemRequestDTO.getTitle() == null ? existingOrderItem.getTitle() : orderItemRequestDTO.getTitle());
        existingOrderItem.setQuantity(orderItemRequestDTO.getQuantity() == null ? existingOrderItem.getQuantity() : orderItemRequestDTO.getQuantity());
        existingOrderItem.setTaxAmount(orderItemRequestDTO.getTaxAmount() == null ? existingOrderItem.getTaxAmount() : orderItemRequestDTO.getTaxAmount());
        existingOrderItem.setTotalAmount(orderItemRequestDTO.getTotalAmount() == null ? existingOrderItem.getTotalAmount() : orderItemRequestDTO.getTotalAmount());
        existingOrderItem.setProductWarehouse(orderItemRequestDTO.getProductWarehouseId() == null ? existingOrderItem.getProductWarehouse() : productWarehouseService.findById(orderItemRequestDTO.getProductWarehouseId(), languageType));
        existingOrderItem.setOrders(orderItemRequestDTO.getOrderId() == null ? existingOrderItem.getOrders() : orderService.findById(orderItemRequestDTO.getOrderId(), languageType));

        return save(existingOrderItem);
    }


    @Override
    public void deleteOrderItem(Long id, LanguageType languageType) {
        LOGGER.info("Removing order item status for id: {}", id);
        if (!orderItemRepository.existsById(id)) {
            throw  LanguageType.ARB.equals(languageType) ? new ResourceNotFoundException(ORDER_ITEM_ARB, id, true) : new ResourceNotFoundException(ORDER_ITEM_ENG, id, false);
        }
        orderItemRepository.deleteById(id);

    }

    public OrderItem save(OrderItem orderItem) {
        LOGGER.info("Saving order item: {}", orderItem);
        return orderItemRepository.save(orderItem);
    }
}
