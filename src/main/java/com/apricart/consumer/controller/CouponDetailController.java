package com.apricart.consumer.controller;

import com.apricart.consumer.enity.Coupon;
import com.apricart.consumer.enity.CouponDetail;
import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.generic.GenericResponse;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.security.dto.request.CouponDetailRequestDTO;
import com.apricart.consumer.security.dto.response.CouponDetailResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.BaseService;
import com.apricart.consumer.service.CouponDetailService;
import com.apricart.consumer.service.CouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/coupons/detail")
@Api(value = "Coupon Detail Controller", tags = {"Coupon Detail"})
public class CouponDetailController{

    @Autowired
    private CouponDetailService couponDetailService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private BaseService baseService;

    @ApiOperation(value = "Get Coupon Detail by Id")
    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<CouponDetailResponseDTO>> findCouponDetailById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                         @PathVariable Long id, @RequestHeader("Language") LanguageType lang) {
        CouponDetail couponDetail = couponDetailService.findById(id, lang);
        return couponDetail != null ? Response.success(CouponDetail.toDTO(couponDetail)) : Response.notFound();
    }

    @ApiOperation(value = "Get Coupon Detail by Coupon Id")
    @GetMapping("/id/{coupon}")
    public ResponseEntity<GenericResponse<CouponDetailResponseDTO>> findCouponDetailByCoupon(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                             @PathVariable Long coupon, @RequestHeader("Language") LanguageType lang) {
        Coupon coupons = couponService.findById(coupon, lang);
        CouponDetail couponDetail = couponDetailService.findByCoupon(coupons);
        return couponDetail != null ? Response.success(CouponDetail.toDTO(couponDetail)) : Response.notFound();
    }

    @ApiOperation(value = "Get Coupon Details by Warehouse Id")
    @GetMapping("/warehouse/{id}")
    public ResponseEntity<GenericResponse<List<CouponDetailResponseDTO>>> findCouponDetailByWarehouseId(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                                               @PathVariable Long id, @RequestHeader("Language") LanguageType lang) {
        List<CouponDetail> couponDetails = couponDetailService.findByWarehouseId(id);
        return !couponDetails.isEmpty() ? Response.success(CouponDetail.toDTOList(couponDetails)) : Response.notFound();
    }
    @ApiOperation(value = "Get Coupon Details by Order Id")
    @GetMapping("/order/{id}")
    public ResponseEntity<GenericResponse<List<CouponDetailResponseDTO>>> findCouponDetailByOrderId(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                                               @PathVariable String id, @RequestHeader("Language") LanguageType lang) {
        List<CouponDetail> couponDetails = couponDetailService.findByOrderId(id);
        return !couponDetails.isEmpty() ? Response.success(CouponDetail.toDTOList(couponDetails)) : Response.notFound();
    }

    @ApiOperation(value = "Add Coupon Detail")
    @PostMapping
    public ResponseEntity<GenericResponse<CouponDetailResponseDTO>> addCouponDetail(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                    @Valid @RequestBody CouponDetailRequestDTO couponDetailRequestDTO,
                                                                                    HttpServletRequest request, @RequestHeader("Language") LanguageType lang) {
        Customer customer = baseService.resolveUser(request);
        couponDetailService.addCouponDetail(couponDetailRequestDTO, customer, lang);
        return Response.created();
    }

    @ApiOperation(value = "Update Coupon Detail")
    @PutMapping
    public ResponseEntity<GenericResponse<CouponDetailResponseDTO>> updateCouponDetail(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                       @Valid @RequestBody CouponDetailRequestDTO couponRequestDTO, @RequestHeader("Language") LanguageType lang) {
        CouponDetail updateCouponDetail = couponDetailService.updateCouponDetail(couponRequestDTO, lang);
        return updateCouponDetail != null ? Response.success(CouponDetail.toDTO(updateCouponDetail)) : Response.notFound();
    }
}
