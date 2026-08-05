package com.apricart.consumer.controller;

import com.apricart.consumer.generic.GenericResponse;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.mapper.ProductMapper;
import com.apricart.consumer.security.dto.dto.ProductDetailDTO;
import com.apricart.consumer.security.dto.response.ProductWarehouseResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.SearchService;
import com.apricart.consumer.utils.ProductControllerUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created on June, 2024
 *
 * @author Kashaf
 */
@Validated
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth/open/search")
@Api(value = "Search Controller", tags = {"Search"})
public class SearchController {

	@Autowired
	SearchService searchService;

	@Autowired
	ProductControllerUtil productControllerUtil;

	@Autowired
	ProductMapper productMapper;

	@GetMapping("/product")
	public ResponseEntity<GenericResponse<List<ProductDetailDTO>>> search(@RequestHeader("Language") LanguageType lang,
																		  @RequestParam(required = false) Long customerId,
																		  @RequestParam("wId") Long warehouseId,
																		  @RequestParam("q") String query) {

		List<ProductDetailDTO> products = searchService.searchProductDetails(query, warehouseId, customerId, lang);
		return !products.isEmpty() ? Response.success(products) : Response.notFound();
	}

}

