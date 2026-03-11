package com.apricart.consumer.controller;

import com.apricart.consumer.generic.GenericResponse;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.security.dto.response.HomeDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.HomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth/open/home")
@Api(value = "Home Controller", tags = {"Home"})
public class HomeController {

    @Autowired
    private HomeService homeService;

    @ApiOperation(value = "Get Home Details")
    @GetMapping("/{warehouseId}")
    public ResponseEntity<GenericResponse<HomeDTO>> getHomeDetails (
            @RequestHeader("Language") LanguageType lang, @PathVariable Long warehouseId,
            @RequestParam Long customerId,
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize) {
        HomeDTO home = homeService.getHomeDetailsByWarehouseId(warehouseId, lang, customerId, pageNo, pageSize, lang);
        return home != null ? Response.success(home) : Response.notFound();
    }
}
