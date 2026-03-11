package com.apricart.consumer.controller;

import com.apricart.consumer.enity.DeliveryTime;
import com.apricart.consumer.generic.GenericResponse;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.security.dto.request.DeliveryTimeRequestDTO;
import com.apricart.consumer.security.dto.response.DeliveryTimeResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.DeliveryTimeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.DELIVERY_TIME_REMOVED_SUCCESSFULLY_ARABIC;
import static com.apricart.consumer.security.constants.ResponseMessage.DELIVERY_TIME_REMOVED_SUCCESSFULLY;

@RestController
@RequestMapping("/v1/auth/open/delivery/times")
@Api(value = "DeliveryTime Controller", tags = {"DeliveryTime"})
public class DeliveryTimeController {

    @Autowired
    private DeliveryTimeService deliveryTimeService;

    @ApiOperation(value = "Get all Delivery Times")
    @GetMapping
    public ResponseEntity<GenericResponse<List<DeliveryTimeResponseDTO>>> getAllDeliveryTime(@RequestHeader("Language") LanguageType lang) {
        List<DeliveryTimeResponseDTO> deliveryTimes = DeliveryTime.toDTOList(deliveryTimeService.getAllDeliveryTimes(lang));
        return !deliveryTimes.isEmpty() ? Response.success(deliveryTimes) : Response.notFound();
    }

    @ApiOperation(value = "Get Delivery Time by Id")
    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<DeliveryTimeResponseDTO>> findDeliveryTimeById(@PathVariable Long id, @RequestHeader("Language") LanguageType lang) {
        DeliveryTimeResponseDTO deliveryTime = DeliveryTime.toDTO(deliveryTimeService.findById(id, lang));
        return deliveryTime != null ? Response.success(deliveryTime) : Response.notFound();
    }

    @ApiOperation(value = "Get Delivery Time by settingId")
    @GetMapping("/setting/{id}")
    public ResponseEntity<GenericResponse<List<DeliveryTimeResponseDTO>>> findDeliveryTimeBySettingId(@PathVariable Long id, @RequestHeader("Language") LanguageType lang) {
        List<DeliveryTimeResponseDTO> deliveryTime = DeliveryTime.toDTOList(deliveryTimeService.findBySettingId(id));
        return !deliveryTime.isEmpty() ? Response.success(deliveryTime) : Response.notFound();
    }

    @ApiOperation(value = "Add Delivery Time")
    @PostMapping
    public ResponseEntity<GenericResponse<DeliveryTimeRequestDTO>> addDeliveryTime(@Valid @RequestBody DeliveryTimeRequestDTO deliveryTimeRequestDTO,
                                                                                   @RequestHeader("Language") LanguageType lang, @RequestParam("SettingId") Long settingId) {
        deliveryTimeService.addDeliveryTime(deliveryTimeRequestDTO, settingId, lang);
        return Response.created();
    }

    @ApiOperation(value = "Update Delivery Time")
    @PutMapping
    public ResponseEntity<GenericResponse<DeliveryTimeResponseDTO>> updateDeliveryTime(@Valid @RequestBody DeliveryTimeRequestDTO deliveryTimeRequestDTO, @RequestHeader("Language") LanguageType lang) {
        DeliveryTimeResponseDTO updatedDeliveryTime = DeliveryTime.toDTO(deliveryTimeService.updateDeliveryTime(deliveryTimeRequestDTO, lang));
        return updatedDeliveryTime != null ? Response.success(updatedDeliveryTime) : Response.notFound();
    }

    @ApiOperation(value = "Delete Delivery Time By Id")
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<String>> deleteDeliveryTime(@PathVariable Long id, @RequestHeader("Language") LanguageType lang) {
        deliveryTimeService.deleteDeliveryTime(id, lang);
        return lang.equals(LanguageType.ARB) ? Response.success(DELIVERY_TIME_REMOVED_SUCCESSFULLY_ARABIC) : Response.success(DELIVERY_TIME_REMOVED_SUCCESSFULLY);
    }
}
