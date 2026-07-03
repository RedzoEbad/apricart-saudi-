package com.apricart.consumer.repository.jpa;

import com.apricart.consumer.enity.UserPortal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created on July, 2026
 *
 * @author Antigravity
 */
@Repository
public interface UserPortalRepository extends JpaRepository<UserPortal, Long> {

	UserPortal findByUsername(String username);

	UserPortal findByEmail(String email);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);
}
