package com.apricart.consumer.configuration;

import com.apricart.consumer.enity.Roles;
import com.apricart.consumer.enity.UserPortal;
import com.apricart.consumer.repository.jpa.RoleRepository;
import com.apricart.consumer.repository.jpa.UserPortalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Created on July, 2026
 *
 * @author Antigravity
 */
@Component
public class AdminDataInitializer implements CommandLineRunner {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserPortalRepository userPortalRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
		// 1. Initialize SUPER_ADMIN role (ID 1) if not exists
		Roles adminRole = roleRepository.findById(1L).orElse(null);
		if (adminRole == null) {
			adminRole = new Roles();
			adminRole.setId(1L);
			adminRole.setName("SUPER_ADMIN");
			adminRole.setArabicName("مدير خارق");
			adminRole.setActive("Y");
			roleRepository.save(adminRole);
		}

		// 2. Initialize default admin user if not exists
		if (!userPortalRepository.existsByUsername("admin")) {
			UserPortal admin = UserPortal.builder()
					.name("Admin User")
					.username("admin")
					.email("admin@apricart.com")
					.password(passwordEncoder.encode("admin123"))
					.phoneNumber("966500000000")
					.isActive(true)
					.roles(adminRole)
					.build();
			userPortalRepository.save(admin);
		}
	}
}
