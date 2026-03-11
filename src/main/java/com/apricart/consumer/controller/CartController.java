package com.apricart.consumer.controller;

import com.apricart.consumer.enity.Cart;
import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.generic.GenericResponse;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.security.dto.request.CartRequestDTO;
import com.apricart.consumer.security.dto.response.CartResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.BaseService;
import com.apricart.consumer.service.CartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.CART_CLEARED_SUCCESSFULLY_ARABIC;
import static com.apricart.consumer.security.constants.ArabicResponseMessages.CART_ITEM_REMOVED_SUCCESSFULLY_ARABIC;
import static com.apricart.consumer.security.constants.ResponseMessage.CART_CLEARED_SUCCESSFULLY;
import static com.apricart.consumer.security.constants.ResponseMessage.CART_ITEM_REMOVED_SUCCESSFULLY;

@RestController
@RequestMapping("/v1/carts")
@Api(value = "Cart Controller", tags = {"Cart"})
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    BaseService baseService;

    @ApiOperation(value = "Add Cart Item", authorizations = {@Authorization(value = "jwtToken")})
    @PostMapping
    public ResponseEntity<GenericResponse<CartResponseDTO>> addCartItem(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @Valid @RequestBody CartRequestDTO cartRequestDTO,
            HttpServletRequest request,
            @RequestHeader("Language") LanguageType lang) {
        Customer customer = baseService.resolveUser(request);
        cartService.addToCart(cartRequestDTO, customer, lang);
        return Response.created();
    }

    @ApiOperation(value = "Update Cart Item", authorizations = {@Authorization(value = "jwtToken")})
    @PutMapping
    public ResponseEntity<GenericResponse<CartResponseDTO>> updateCartItem(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @Valid @RequestBody CartRequestDTO cartRequestDTO,
            HttpServletRequest request,
            @RequestHeader("Language") LanguageType lang) {
        Customer customer = baseService.resolveUser(request);
        Cart updatedCart = cartService.updateCart(customer, cartRequestDTO.getProductId(), cartRequestDTO.getProductWarehouseId(), Integer.valueOf(cartRequestDTO.getQuantity()));
        return updatedCart != null ? Response.success(Cart.toDTO(updatedCart)) : Response.notFound();
    }

    @ApiOperation(value = "Delete Cart By Product Id", authorizations = {@Authorization(value = "jwtToken")})
    @DeleteMapping("/{productId}")
    public ResponseEntity<GenericResponse<String>> removeCartItem(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            HttpServletRequest request,
            @PathVariable Long productId,
            @RequestHeader("Language") LanguageType lang) {
        Customer customer = baseService.resolveUser(request);
        cartService.removeCartItem(customer, productId);
        return lang.equals(LanguageType.ARB) ? Response.success(CART_ITEM_REMOVED_SUCCESSFULLY_ARABIC) :Response.success(CART_ITEM_REMOVED_SUCCESSFULLY);
    }

    @ApiOperation(value = "Get Cart by Customer Id", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping
    public ResponseEntity<GenericResponse<List<CartResponseDTO>>> findCartByCustomerId(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            HttpServletRequest request,
            @RequestHeader("Language") LanguageType lang) {
        Customer customer = baseService.resolveUser(request);
        List<Cart> cart = cartService.findByCustomerId(customer);
        return cart != null ? Response.success(Cart.toDTOList(cart)) : Response.notFound();
    }

    @ApiOperation(value = "Get Total Price by Customer Id", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping("/total")
    public ResponseEntity<GenericResponse<String>> getTotalPrice(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            HttpServletRequest request,
            @RequestHeader("Language") LanguageType lang) {
        Customer customer = baseService.resolveUser(request);
        String cartTotal = cartService.calculateTotal(customer);
        return Response.success(cartTotal);
    }

    @ApiOperation(value = "Clear cart by Customer Id", authorizations = {@Authorization(value = "jwtToken")})
    @DeleteMapping("/clear")
    public ResponseEntity<GenericResponse<String>> clearCart(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            HttpServletRequest request,
            @RequestHeader("Language") LanguageType lang) {
        Customer customer = baseService.resolveUser(request);
        cartService.clearCart(customer);
        return lang.equals(LanguageType.ARB) ? Response.success(CART_CLEARED_SUCCESSFULLY_ARABIC) : Response.success(CART_CLEARED_SUCCESSFULLY);
    }
}
