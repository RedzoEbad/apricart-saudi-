package com.apricart.consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.unit.DataSize;

import javax.annotation.PostConstruct;
import javax.servlet.MultipartConfigElement;
import java.util.TimeZone;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableAsync
@EnableCaching
@EnableJpaRepositories(basePackages = "com.apricart.consumer.repository.jpa")
public class ApricartApplication {

	@Value("${application.timezone}")
	private String applicationTimeZone;

	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone(applicationTimeZone));
	}

	public static void main(String[] args) {
		SpringApplication.run(ApricartApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		long max_file_size = (10L * 1048576L);
		factory.setMaxFileSize(DataSize.ofBytes(max_file_size));
		factory.setMaxRequestSize(DataSize.ofBytes(max_file_size));
		return factory.createMultipartConfig();
	}

}

@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "scheduling.enable", matchIfMissing = true)
class SchedulingConfiguration{

}

