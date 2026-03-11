package com.apricart.consumer.generic;

import com.apricart.consumer.security.dto.response.CouponResponseDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericResponse<T> {

    private int status;

    private String message;

    private T data;

}