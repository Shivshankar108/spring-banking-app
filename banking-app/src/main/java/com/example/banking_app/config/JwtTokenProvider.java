package com.example.banking_app.config;


import java.security.Key;
import java.util.Date;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

	@Value("${app.jwt-secret}")
	private String jwtSecret;
	
	@Value("${app.jwt-expiration}")
	private long jwtExpirationDate;
	
	public String generateToken(Authentication authentication) {
		String username = authentication.getName();
		Date currentDate = new Date();
		Date expiryDate = new Date(currentDate.getTime() + jwtExpirationDate);
		
		String token =  Jwts.builder()
				.setSubject(username)
				.setIssuedAt(currentDate)
				.setExpiration(expiryDate)
				.signWith(key())
				.compact();

		System.out.println("Generated JWT Token: " + token);
		return token;
	}
	
	
	private Key key() {
		byte[] bytes = Decoders.BASE64.decode(jwtSecret);
		return Keys.hmacShaKeyFor(bytes);
	}
	
	public String getUserName(String token) {
		Claims claims = Jwts.parserBuilder()
				.setSigningKey(key())
				.build()
				.parseClaimsJws(token)
				.getBody();
		
		return claims.getSubject();
	}
	
	
	
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(key())
				.build()
				.parse(token);
			return true;
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
}
