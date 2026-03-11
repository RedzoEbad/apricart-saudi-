package com.apricart.consumer.controller;


import com.apricart.consumer.enity.OrderItem;
import com.apricart.consumer.enity.Orders;
import com.apricart.consumer.enity.ProductWarehouse;
import com.apricart.consumer.generic.GenericResponse;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.security.dto.request.OrderItemRequestDTO;
import com.apricart.consumer.security.dto.response.OrderItemResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.OrderItemService;
import com.apricart.consumer.service.OrderService;
import com.apricart.consumer.service.ProductWarehouseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.ORDER_ITEM_REMOVED_SUCCESSFULLY_ARABIC;
import static com.apricart.consumer.security.constants.ResponseMessage.ORDER_ITEM_REMOVED_SUCCESSFULLY;

@RestController
@RequestMapping("/v1/orderItems")
@Api(value = "Order Item Controller", tags = {"Order Item"})
public class OrderItemController{

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductWarehouseService productWarehouseService;

    @ApiOperation(value = "Get all order items", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping
    public ResponseEntity<GenericResponse<List<OrderItemResponseDTO>>> getAllOrderItems(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                        @RequestHeader("Language") LanguageType lang) {
        List<OrderItemResponseDTO> orderItems = OrderItem.toDTOList(orderItemService.getAllOrderItems());
        return !orderItems.isEmpty() ? Response.success(orderItems) : Response.notFound();
    }

    @ApiOperation(value = "Get order items by order id", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping("/order/{id}")
    public ResponseEntity<GenericResponse<List<OrderItemResponseDTO>>> getOrdersByOrderId(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                          @RequestHeader("Language") LanguageType lang,
                                                                                          @PathVariable String id) {
        Orders orders = orderService.findById(id, lang);
        List<OrderItemResponseDTO> orderItems = OrderItem.toDTOList(orderItemService.findByOrder(orders));
        return !orderItems.isEmpty() ? Response.success(orderItems) : Response.notFound();
    }

    @ApiOperation(value = "Get Total amount by Order Id", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping("/total/{id}")
    public ResponseEntity<GenericResponse<Double>> calculateTotalAmountById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                            @RequestHeader("Language") LanguageType lang,
                                                                            @PathVariable Long id) {
        return Response.success(orderItemService.calculateTotalAmount(id, lang));
    }

    @ApiOperation(value = "Get Tax amount by Order Id", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping("/tax/{id}")
    public ResponseEntity<GenericResponse<Double>> calculateTaxAmountById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                          @RequestHeader("Language") LanguageType lang,
                                                                          @PathVariable Long id) {
        return Response.success(orderItemService.calculateTaxAmount(id, lang));
    }

    @ApiOperation(value = "Get Order Item by Id", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<OrderItemResponseDTO>> findOrderItemById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                   @RequestHeader("Language") LanguageType lang,
                                                                                   @PathVariable Long id) {
        OrderItem orderItem = orderItemService.findById(id, lang);
        return orderItem != null ? Response.success(OrderItem.toDTO(orderItem)) : Response.notFound();
    }

    @ApiOperation(value = "Get Order Items by Product Warehouse Id", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping("/productWarehouse/{id}")
    public ResponseEntity<GenericResponse<List<OrderItemResponseDTO>>> findOrderItemByProductWarehouseId(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                                         @RequestHeader("Language") LanguageType lang,
                                                                                                         @PathVariable Long id) {
        ProductWarehouse productWarehouse = productWarehouseService.findById(id, lang);
        List<OrderItem> orderItems = orderItemService.findByProductWarehouse(productWarehouse);
        return !orderItems.isEmpty() ? Response.success(OrderItem.toDTOList(orderItems)) : Response.notFound();
    }

    @ApiOperation(value = "Add Order Item", authorizations = { @Authorization(value="jwtToken") })
    @PostMapping
    public ResponseEntity<GenericResponse<OrderItemResponseDTO>> addOrderItem(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                              @RequestHeader("Language") LanguageType lang,
                                                                              @Valid @RequestBody OrderItemRequestDTO orderItemRequestDTO) {
        orderItemService.saveOrderItem(orderItemRequestDTO, lang);
        return Response.created();
    }

    @ApiOperation(value = "Update Order Item", authorizations = { @Authorization(value="jwtToken") })
    @PutMapping
    public ResponseEntity<GenericResponse<OrderItemResponseDTO>> updateOrderItem(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                 @RequestHeader("Language") LanguageType lang,
                                                                                 @Valid @RequestBody OrderItemRequestDTO orderRequestDTO) {
        OrderItem updateOrderItem = orderItemService.updateOrderItem(orderRequestDTO, lang);
        return updateOrderItem != null ? Response.success(OrderItem.toDTO(updateOrderItem)) : Response.notFound();
    }

    @ApiOperation(value = "Delete Order Item By Id", authorizations = { @Authorization(value="jwtToken") })
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<String>> deleteOrderItem(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                   @PathVariable Long id, @RequestHeader("Language") LanguageType lang) {
        orderItemService.deleteOrderItem(id, lang);
        return lang.equals(LanguageType.ARB) ? Response.success(ORDER_ITEM_REMOVED_SUCCESSFULLY_ARABIC) : Response.success(ORDER_ITEM_REMOVED_SUCCESSFULLY);
    }

}
