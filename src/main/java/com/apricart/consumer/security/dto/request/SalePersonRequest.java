package com.apricart.consumer.security.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalePersonRequest {

	private Long id;
	@NotNull
	private String name;

	private String description;

	private Boolean isActive;

	private Long cityId;

}
