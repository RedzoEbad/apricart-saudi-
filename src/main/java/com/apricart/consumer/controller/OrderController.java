package com.apricart.consumer.controller;

import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.enity.Orders;
import com.apricart.consumer.generic.GenericResponse;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.mapper.OrderMapper;
import com.apricart.consumer.security.dto.request.OrderRequestDTO;
import com.apricart.consumer.security.dto.response.OrderResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.security.enums.OrderType;
import com.apricart.consumer.security.enums.PaymentModeType;
import com.apricart.consumer.security.enums.PaymentStatusType;
import com.apricart.consumer.service.BaseService;
import com.apricart.consumer.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.ORDER_REMOVED_SUCCESSFULLY_ARABIC;
import static com.apricart.consumer.security.constants.ResponseMessage.ORDER_REMOVED_SUCCESSFULLY;


@RestController
@RequestMapping("/v1/orders")
@Api(value = "Order Controller", tags = {"Order"})
public class OrderController{

    @Autowired
    private OrderService orderService;

    @Autowired
    private BaseService baseService;

    @Autowired
    private OrderMapper orderMapper;

    @Transactional(readOnly = true)
    @ApiOperation(value = "Get Order by Id", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<OrderResponseDTO>> findOrderById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                           @RequestHeader("Language") LanguageType lang,
                                                                           @PathVariable String id) {
         OrderResponseDTO orders = Orders.toDTO(orderService.findById(id, lang), orderMapper, lang);
         return orders != null ? Response.success(orders) : Response.notFound();
    }

    @ApiOperation(value = "Add Order", authorizations = { @Authorization(value="jwtToken") })
    @PostMapping
    public ResponseEntity<GenericResponse<OrderResponseDTO>> addOrder(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                      @Valid @RequestBody OrderRequestDTO orderRequestDTO,
                                                                      HttpServletRequest request,
                                                                      @RequestHeader("Language") LanguageType lang) {
        Customer customer = baseService.resolveUser(request);
        OrderResponseDTO order = orderService.generateOrder(orderRequestDTO, customer, lang);
        return Response.created(order);
    }

    @Transactional(readOnly = true)
    @ApiOperation(value = "Get orders History by customer", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping("/history")
    public ResponseEntity<GenericResponse<List<OrderResponseDTO>>> getOrdersByCustomer(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                       HttpServletRequest request,
                                                                                       @RequestHeader("Language") LanguageType lang) {
        Customer customer = baseService.resolveUser(request);
        List<OrderResponseDTO> orders = orderMapper.toOrderList(orderService.findByCustomer(customer), lang);
        return !orders.isEmpty() ? Response.success(orders) : Response.notFound();
    }

    @ApiOperation(value = "Update Order", authorizations = { @Authorization(value="jwtToken") })
    @PutMapping
    public ResponseEntity<GenericResponse<OrderResponseDTO>> updateOrder(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                         @RequestHeader("Language") LanguageType lang,
                                                                         @Valid @RequestBody OrderRequestDTO orderRequestDTO) {
        Orders updateOrders = orderService.updateOrder(orderRequestDTO, lang);
        return updateOrders != null ? Response.success(Orders.toDTO(updateOrders, orderMapper, lang)) : Response.notFound();
    }

    @ApiOperation(value = "Cancel order By Id", authorizations = { @Authorization(value="jwtToken") })
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<String>> deleteOrder(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                               @RequestHeader("Language") LanguageType lang,
                                                               @PathVariable String id) {
        orderService.cancelOrder(id, lang);
        return lang.equals(LanguageType.ARB) ? Response.success(ORDER_REMOVED_SUCCESSFULLY_ARABIC) : Response.success(ORDER_REMOVED_SUCCESSFULLY);
    }

    @ApiOperation(value = "Update Order Status By Id", authorizations = { @Authorization(value="jwtToken") })
    @PutMapping("/{id}")
    public ResponseEntity<GenericResponse<OrderResponseDTO>> updateOrderStatusById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                   @PathVariable String id,
                                                                                   @RequestParam OrderType status,
                                                                                   @RequestHeader("Language") LanguageType lang) {
        Orders updatedOrders = orderService.updateOrderStatus(id, status, lang);
        return updatedOrders != null ? Response.success(Orders.toDTO(updatedOrders, orderMapper, lang)) : Response.notFound();
    }
    @Transactional(readOnly = true)
    @ApiOperation(value = "Get all orders", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping
    public ResponseEntity<GenericResponse<List<OrderResponseDTO>>> getAllOrder(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                               @RequestHeader("Language") LanguageType lang) {
        List<OrderResponseDTO> orders = Orders.toDTOList(orderService.getAllOrders(), orderMapper, lang);
        return !orders.isEmpty() ? Response.success(orders) : Response.notFound();
    }

    @Transactional(readOnly = true)
    @ApiOperation(value = "Get orders by order type", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping("/type/{orderType}")
    public ResponseEntity<GenericResponse<List<OrderResponseDTO>>> getOrdersByOrderType(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                        @PathVariable OrderType orderType,
                                                                                        @RequestHeader("Language") LanguageType lang) {
        List<OrderResponseDTO> orders = Orders.toDTOList(orderService.findByOrderStatus(orderType), orderMapper, lang);
        return !orders.isEmpty() ? Response.success(orders) : Response.notFound();
    }

    @Transactional(readOnly = true)
    @ApiOperation(value = "Get orders by payment status", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping("/status/{paymentType}")
    public ResponseEntity<GenericResponse<List<OrderResponseDTO>>> getOrdersByPaymentType(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                          @RequestHeader("Language") LanguageType lang,
                                                                                          @PathVariable PaymentStatusType paymentType) {
        List<OrderResponseDTO> orders = Orders.toDTOList(orderService.findByPaymentStatus(paymentType), orderMapper, lang);
        return !orders.isEmpty() ? Response.success(orders) : Response.notFound();
    }

    @Transactional(readOnly = true)
    @ApiOperation(value = "Get orders by payment mode", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping("/payment/{paymentMode}")
    public ResponseEntity<GenericResponse<List<OrderResponseDTO>>> getOrdersByPaymentMode(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                          @RequestHeader("Language") LanguageType lang,
                                                                                          @PathVariable PaymentModeType paymentMode) {
        List<OrderResponseDTO> orders = Orders.toDTOList(orderService.findByPaymentMode(paymentMode), orderMapper, lang);
        return !orders.isEmpty() ? Response.success(orders) : Response.notFound();
    }

    @Transactional(readOnly = true)
    @ApiOperation(value = "Get orders by shipping charges", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping("/shipping/{shippingCharges}")
    public ResponseEntity<GenericResponse<List<OrderResponseDTO>>> getOrdersByPaymentMode(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                          @RequestHeader("Language") LanguageType lang,
                                                                                          @PathVariable String shippingCharges) {
        List<OrderResponseDTO> orders = Orders.toDTOList(orderService.findByShippingCharge(shippingCharges), orderMapper, lang);
        return !orders.isEmpty() ? Response.success(orders) : Response.notFound();
    }


    @ApiOperation(value = "Update Payment Status By Id", authorizations = { @Authorization(value="jwtToken") })
    @PutMapping("/payment/{id}/{status}")
    public ResponseEntity<GenericResponse<OrderResponseDTO>> updatePaymentStatusById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                     @RequestHeader("Language") LanguageType lang,
                                                                                     @PathVariable String id,
                                                                                     @PathVariable PaymentStatusType status) {
        Orders updatedOrders = orderService.updatePaymentStatus(id, status, lang);
        return updatedOrders != null ? Response.success(Orders.toDTO(updatedOrders, orderMapper, lang)) : Response.notFound();
    }


}
