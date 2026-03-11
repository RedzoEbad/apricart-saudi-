package com.apricart.consumer.controller;

import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.generic.GenericResponse;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.mapper.OrderMapper;
import com.apricart.consumer.security.dto.response.OrderResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.CustomerService;
import com.apricart.consumer.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/v1/auth/open/orders")
@Api(value = "Order Open Controller", tags = {"Order"})
public class OrderOpenController{

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private CustomerService customerService;

    @ApiOperation(value = "Get orders History by customer")
    @GetMapping("/customer/history")
    public ResponseEntity<GenericResponse<List<OrderResponseDTO>>> getOrdersByCustomerId(@RequestParam("customerId") Long id, @RequestHeader("Language") LanguageType lang) {

        Customer customer = customerService.findActiveCustomerById(id, lang);
        List<OrderResponseDTO> orders = orderMapper.toOrderList(orderService.findByCustomer(customer), lang);
        return !orders.isEmpty() ? Response.success(orders) : Response.notFound();
    }
}

