package com.apricart.consumer.controller;

import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.enity.WishList;
import com.apricart.consumer.generic.GenericResponse;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.security.dto.request.WishListRequestDTO;
import com.apricart.consumer.security.dto.response.WishListResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.BaseService;
import com.apricart.consumer.service.WishListService;
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

import static com.apricart.consumer.security.constants.ArabicResponseMessages.*;
import static com.apricart.consumer.security.constants.ResponseMessage.*;

@RestController
@RequestMapping("/v1/wish/list")
@Api(value = "WishList Controller", tags = {"WishList"})
public class WishListController {

    @Autowired
    private BaseService baseService;
    @Autowired
    private WishListService wishListService;


    @ApiOperation(value = "Get WishList by Id", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<WishListResponseDTO>> findWishListById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @PathVariable Long id, @RequestHeader("Language") LanguageType lang) {
        WishList wishList = wishListService.findById(id, lang);
        return wishList != null ? Response.success(WishList.toDTO(wishList)) : Response.notFound();
    }

    @ApiOperation(value = "Get WishList by Customer", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping("/customer")
    public ResponseEntity<GenericResponse<List<WishListResponseDTO>>> findWishListByCustomer(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                             @RequestParam Long warehouseId,
                                                                                             @RequestHeader("Language") LanguageType lang,
                                                                                             HttpServletRequest request) {
        Customer customer = baseService.resolveUser(request);
        List<WishListResponseDTO> wishLists = wishListService.findByCustomerId(customer, warehouseId, lang);
        return !wishLists.isEmpty() ? Response.success(wishLists) : Response.notFound(LanguageType.ARB.equals(lang) ? WISH_LIST_WAREHOUSE_NOT_FOUND_ARABIC : WISH_LIST_WAREHOUSE_NOT_FOUND);
    }

    @ApiOperation(value = "Add WishList", authorizations = {@Authorization(value = "jwtToken")})
    @PostMapping
    public ResponseEntity<GenericResponse<WishListResponseDTO>> addWishList(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                            @Valid @RequestBody WishListRequestDTO wishListRequestDTO, @RequestHeader("Language") LanguageType lang, HttpServletRequest request) {

        Customer customer = baseService.resolveUser(request);
        wishListService.addWishList(wishListRequestDTO, customer, lang);
        return Response.created();
    }

    @ApiOperation(value = "Clear WishList", authorizations = {@Authorization(value = "jwtToken")})
    @DeleteMapping("/clear")
    public ResponseEntity<GenericResponse<String>> clearWishList(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestHeader("Language") LanguageType lang, HttpServletRequest request) {
        Customer customer = baseService.resolveUser(request);
        wishListService.clearWishList(customer);
        return lang.equals(LanguageType.ARB) ? Response.success(WISHLIST_CLEARED_SUCCESSFULLY_ARABIC) : Response.success(WISHLIST_CLEARED_SUCCESSFULLY);
    }

    @ApiOperation(value = "Delete WishList By Id", authorizations = {@Authorization(value = "jwtToken")})
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<String>> deleteWishList(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @PathVariable Long id, @RequestHeader("Language") LanguageType lang) {
        wishListService.removeFromWishList(id, lang);
        return lang.equals(LanguageType.ARB) ? Response.success(WISHLIST_REMOVED_SUCCESSFULLY_ARABIC) : Response.success(WISHLIST_REMOVED_SUCCESSFULLY);
    }

    @ApiOperation(value = "Delete WishList By Product Id", authorizations = {@Authorization(value = "jwtToken")})
    @DeleteMapping("/product/{productId}")
    public ResponseEntity<GenericResponse<String>> deleteWishListByProductId(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                             @RequestHeader("Language") LanguageType lang,
                                                                             @PathVariable Long productId,
                                                                             HttpServletRequest request) {

        Customer customer = baseService.resolveUser(request);
        wishListService.removeFromWishListByCustomerAndProductId(customer, productId);
        return lang.equals(LanguageType.ARB) ? Response.success(WISHLIST_ITEM_REMOVED_SUCCESSFULLY_ARABIC) : Response.success(WISHLIST_ITEM_REMOVED_SUCCESSFULLY);
    }

    @ApiOperation(value = "Delete WishList By Customer", authorizations = {@Authorization(value = "jwtToken")})
    @DeleteMapping("/customer/{id}")
    public ResponseEntity<GenericResponse<String>> deleteWishListByCustomer(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @PathVariable Long id, @RequestHeader("Language") LanguageType lang, HttpServletRequest request) {
        Customer customer = baseService.resolveUser(request);
        wishListService.removeFromWishListByCustomerAndProductId(customer, id);
        return lang.equals(LanguageType.ARB) ? Response.success(WISHLIST_ITEM_REMOVED_SUCCESSFULLY_ARABIC) : Response.success(WISHLIST_ITEM_REMOVED_SUCCESSFULLY);
    }

}
