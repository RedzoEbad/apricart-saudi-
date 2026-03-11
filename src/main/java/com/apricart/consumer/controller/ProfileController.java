package com.apricart.consumer.controller;

import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.generic.GenericResponse;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.security.dto.dto.UserDto;
import com.apricart.consumer.security.dto.request.UpdateProfileRequest;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.BaseService;
import com.apricart.consumer.service.ProfileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created on January, 2024
 *
 * @author Kashaf
 */
@Validated
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/profile")
@Api(value = "Profile Controller", tags = {"Profile"})
public class ProfileController {

	@Autowired
	BaseService baseService;
	@Autowired
	ProfileService profileService;

	@ApiOperation(value = "Update Profile Request")
	@PostMapping()
	public ResponseEntity<?> updateProfile(@Valid @RequestBody UpdateProfileRequest updateProfileRequest, HttpServletRequest request, @RequestHeader("Language") LanguageType lang) {
		Customer user = baseService.resolveUser(request);
		return profileService.updateProfile(updateProfileRequest, user, lang);
	}
	@ApiOperation(value = "Get Customer Profile")
	@GetMapping()
	public ResponseEntity<GenericResponse<UserDto>> getCustomerProfile(HttpServletRequest request, @RequestHeader("Language") LanguageType lang) {
		Customer user = baseService.resolveUser(request);
		UserDto userProfile = Customer.toDTO(profileService.findCustomerProfile(user, lang));
		return userProfile != null ? Response.success(userProfile) : Response.notFound();
	}
}
