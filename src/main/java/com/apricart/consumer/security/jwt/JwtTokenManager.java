package com.apricart.consumer.security.jwt;

import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.security.constants.SecurityConstants;
import com.apricart.consumer.security.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created on January, 2024
 *
 * @author Kashaf
 */
@Component
public class JwtTokenManager {

	public String generateToken(Customer user) {
		return generateToken(user.getUsername(), user.getUserRole());
	}

	public String generateToken(String username, UserRole userRole) {
		final Claims claims = Jwts.claims().setSubject(username);
		claims.put("role", userRole.name());

		final long currentTimeMillis = System.currentTimeMillis();

		return Jwts.builder()
				.setClaims(claims)
				.setIssuer(SecurityConstants.ISSUER)
				.setIssuedAt(new Date(currentTimeMillis))
				.setExpiration(new Date(currentTimeMillis + SecurityConstants.EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS256, SecurityConstants.SECRET_KEY)
				.compact();
	}

	public String getUsernameFromToken(String token) {

		final Claims claims = getAllClaimsFromToken(token);
		return claims.getSubject();
	}

	public boolean validateToken(String token, String authenticatedUsername) {

		final String usernameFromToken = getUsernameFromToken(token);

		final boolean equalsUsername = usernameFromToken.equals(authenticatedUsername);
		final boolean tokenExpired = isTokenExpired(token);

		return equalsUsername && !tokenExpired;
	}

	private boolean isTokenExpired(String token) {

		final Date expirationDateFromToken = getExpirationDateFromToken(token);
		return expirationDateFromToken.before(new Date());
	}

	private Date getExpirationDateFromToken(String token) {

		final Claims claims = getAllClaimsFromToken(token);
		return claims.getExpiration();
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(SecurityConstants.SECRET_KEY).parseClaimsJws(token).getBody();
	}

}
