package com.gls.ppldv.configuration.security;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@PropertySource("classpath:prop/jwt.properties")
@Service
public class TokenProvider {

	@Value("${secret-key}")
	private String secretKey;
	
	@Value("${expiration-hours}")
	private Long expirationHours;
	
	@Value("${issuer}")
	private String issuer;
	
	public String createToken(String userSpecification) {
		SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
		
		LocalDateTime currentTime = LocalDateTime.now();
		Instant instant = currentTime.atZone(ZoneId.systemDefault()).toInstant();
		Date issuedAt = Date.from(instant);
		
		Date expiration = Date.from(instant.plus(expirationHours, ChronoUnit.HOURS));
		
		return Jwts.builder()
				.signWith(key)
				.claim("sub", userSpecification)
				.claim("iss", issuer)
				.setIssuedAt(issuedAt)
				.setExpiration(expiration)
				.compact();
		/*
		return Jwts.builder()
				.signWith(new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS512.getJcaName()))
				.setSubject(userSpecification) // jwt 토큰 제목
				.setIssuer(issuer) // jwt 토큰 발급자
				.setIssuedAt(Timestamp.valueOf(LocalDateTime.now())) // jwt 토큰 발급 시간
				.setExpiration(Date.from(Instant.now().plus(expirationHours, ChronoUnit.HOURS))) // JWT 토큰 만료 시간
				.compact();
		*/
	}
}
