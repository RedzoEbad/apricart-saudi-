package com.apricart.consumer.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.swagger.web.SecurityConfiguration;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.boot.actuate.trace.http.Include.AUTHORIZATION_HEADER;

/**
 * Created on January, 2024
 *
 * @author Kashaf
 */
@Configuration
@EnableSwagger2
@PropertySource(value = "classpath:swagger-information.properties")
public class SwaggerConfiguration {

	@Value("${swagger.app-info.name}")
	private String appName;

	@Value("${swagger.app-info.description}")
	private String appDescription;

	@Value("${swagger.app-info.version}")
	private String appVersion;

	@Value("${swagger.app-info.license}")
	private String licence;

	@Value("${swagger.app-info.license-url}")
	private String licenceUrl;

	@Value("${swagger.contact.name}")
	private String contactName;

	@Value("${swagger.contact.url}")
	private String contactUrl;

	@Value("${swagger.contact.email}")
	private String contactEmail;

	final String PROJECT_BASE_PACKAGE = "com.apricart.consumer.controller";

	@Bean
	public Docket api() {

		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage(PROJECT_BASE_PACKAGE))
				.paths(PathSelectors.regex("/.*"))
				.build()
				.securitySchemes(Collections.singletonList(apiKey()))
				.apiInfo(getAPIInfo());
	}

	private ApiKey apiKey() {

		return new ApiKey("JWT", AUTHORIZATION_HEADER.name(), "header");
	}

	private ApiInfo getAPIInfo() {

		return new ApiInfoBuilder()
				.title(appName)
				.version(appVersion)
				.description(appDescription)
				.license(licence).licenseUrl(licenceUrl)
				.contact(new Contact(contactName, "", contactEmail))
				.build();
	}

}
