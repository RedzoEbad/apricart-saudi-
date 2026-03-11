package com.apricart.consumer.utils;

import com.apricart.consumer.security.constants.ProjectConstants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Objects;

/**
 * Created on August, 2021
 *
 * @author Farrukh Ellahi
 */
@Service
public class ExceptionMessageAccessor {

	private final MessageSource messageSource;

	ExceptionMessageAccessor(@Qualifier("exceptionMessageSource") MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public String getMessage(Locale locale, String key, Object... parameter) {

		if (Objects.isNull(locale)) {
			return messageSource.getMessage(key, parameter, ProjectConstants.ENGLISH_LOCALE);
		}

		return messageSource.getMessage(key, parameter, locale);
	}

}
