package com.apricart.consumer.controller;

import com.apricart.consumer.enity.Coupon;
import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.exceptions.CouponValidationException;
import com.apricart.consumer.generic.GenericResponse;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.security.dto.request.CouponRequestDTO;
import com.apricart.consumer.security.dto.response.CouponResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.BaseService;
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

import static com.apricart.consumer.security.constants.ArabicResponseMessages.COUPON_REMOVED_SUCCESSFULLY_ARABIC;
import static com.apricart.consumer.security.constants.ArabicResponseMessages.VALID_COUPON_ARABIC;
import static com.apricart.consumer.security.constants.ResponseMessage.COUPON_REMOVED_SUCCESSFULLY;
import static com.apricart.consumer.security.constants.ResponseMessage.VALID_COUPON;

@RestController
@RequestMapping("/v1/coupons")
@Api(value = "Coupon Controller", tags = {"Coupon"})
public class CouponController {

    @Autowired
    private CouponService couponService;
    @Autowired
    private BaseService baseService;

    @ApiOperation(value = "Get all Coupons")
    @GetMapping
    public ResponseEntity<GenericResponse<List<CouponResponseDTO>>> getAllCoupons(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestHeader("Language") LanguageType lang) {
        List<CouponResponseDTO> coupons = Coupon.toDTOList(couponService.getAllCoupons());
        return !coupons.isEmpty() ? Response.success(coupons) : Response.notFound();
    }

    @ApiOperation(value = "Get Coupon by Id")
    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<CouponResponseDTO>> findCouponById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                             @PathVariable Long id, @RequestHeader("Language") LanguageType lang) {
        Coupon coupon = couponService.findById(id, lang);
        return coupon != null ? Response.success(Coupon.toDTO(coupon)) : Response.notFound();
    }

    @ApiOperation(value = "Get Coupon by Coupon Code")
    @GetMapping("/code/{code}")
    public ResponseEntity<GenericResponse<CouponResponseDTO>> findCouponByCode(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                               @PathVariable String code, @RequestHeader("Language") LanguageType lang) {
        Coupon coupon = couponService.findByCode(code, lang);
        return coupon != null ? Response.success(Coupon.toDTO(coupon)) : Response.notFound();
    }

    @ApiOperation(value = "Get Coupon by Min SubTotal")
    @GetMapping("/subtotal/{subtotal}")
    public ResponseEntity<GenericResponse<List<CouponResponseDTO>>> findCouponByMinSubTotal(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                            @PathVariable Double subtotal, @RequestHeader("Language") LanguageType lang) {
        List<CouponResponseDTO> coupons = Coupon.toDTOList(couponService.findByMinSubTotal(subtotal));
        return !coupons.isEmpty() ? Response.success(coupons) : Response.notFound();
    }

    @ApiOperation(value = "Get Coupon by Expiry")
    @GetMapping("/expiry/{expiry}")
    public ResponseEntity<GenericResponse<List<CouponResponseDTO>>> findCouponByExpiry(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                       @PathVariable String expiry, @RequestHeader("Language") LanguageType lang) {
        List<CouponResponseDTO> coupons = Coupon.toDTOList(couponService.findByExpiry(expiry));
        return !coupons.isEmpty() ? Response.success(coupons) : Response.notFound();
    }

    @ApiOperation(value = "Get Coupon by Status")
    @GetMapping("/status/{status}")
    public ResponseEntity<GenericResponse<List<CouponResponseDTO>>> findCouponByStatus(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                       @PathVariable Boolean status, @RequestHeader("Language") LanguageType lang) {
        List<CouponResponseDTO> coupons = Coupon.toDTOList(couponService.findByStatus(status));
        return !coupons.isEmpty() ? Response.success(coupons) : Response.notFound();
    }

    @ApiOperation(value = "Get Coupon by Usage Limit")
    @GetMapping("/usage/{usage}")
    public ResponseEntity<GenericResponse<List<CouponResponseDTO>>> findCouponByUsageLimit(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                           @PathVariable String usage, @RequestHeader("Language") LanguageType lang) {
        List<CouponResponseDTO> coupons = Coupon.toDTOList(couponService.findByUsageLimit(usage));
        return !coupons.isEmpty() ? Response.success(coupons) : Response.notFound();
    }

    @ApiOperation(value = "Add Coupon")
    @PostMapping
    public ResponseEntity<GenericResponse<CouponResponseDTO>> addCoupon(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                        @Valid @RequestBody CouponRequestDTO couponRequestDTO, @RequestHeader("Language") LanguageType lang) {
        couponService.addCoupon(couponRequestDTO);
        return Response.created();
    }

    @ApiOperation(value = "Update Coupon")
    @PutMapping
    public ResponseEntity<GenericResponse<CouponResponseDTO>> updateCoupon(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                           @Valid @RequestBody CouponRequestDTO couponRequestDTO, @RequestHeader("Language") LanguageType lang) {
        Coupon updateCoupon = couponService.updateCoupon(couponRequestDTO, lang);
        return updateCoupon != null ? Response.success(Coupon.toDTO(updateCoupon)) : Response.notFound();
    }


    @ApiOperation(value = "Delete Coupon By Id")
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<String>> deleteCoupon(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                @PathVariable Long id, @RequestHeader("Language") LanguageType lang) {
        couponService.deleteCoupon(id, lang);
        return lang.equals(LanguageType.ARB) ? Response.success(COUPON_REMOVED_SUCCESSFULLY_ARABIC) : Response.success(COUPON_REMOVED_SUCCESSFULLY);
    }

    @GetMapping("/validate")
    public ResponseEntity<GenericResponse<CouponResponseDTO>> validateCoupon(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                  @RequestParam(required = true) String couponCode,
                                                                  @RequestParam(required = true) Double subtotal,
                                                                  @RequestHeader("Language") LanguageType lang, HttpServletRequest request) {

        CouponResponseDTO coupon = Coupon.toDTO(couponService.findByCode(couponCode, lang));
        try {
            Customer customer = baseService.resolveUser(request);
            couponService.validateCoupon(coupon, customer, subtotal, lang);
            
            return lang.equals(LanguageType.ARB) ? Response.success(VALID_COUPON_ARABIC, coupon) : Response.success(VALID_COUPON, coupon);
        } catch(CouponValidationException e){
                return Response.error(e.getMessage(), coupon);
            }
        }

}

