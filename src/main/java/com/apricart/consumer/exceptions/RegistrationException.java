package com.apricart.consumer.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created on August, 2021
 *
 * @author Farrukh Ellahi
 */
@Getter
@RequiredArgsConstructor
public class RegistrationException extends RuntimeException {

	private final String errorMessage;

}
