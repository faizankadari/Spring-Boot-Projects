package com.example.grocerystore.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    @Autowired
    private BlackList blackList;

	private final ConcurrentHashMap<String, String> blacklistedTokens = new ConcurrentHashMap<>();

	private String SECRET_KEY = "TaK+HaV^uvCHEFsEVfypW#7g9^k*Z8$V";

	SecretKey getSigningKey() {
		return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
	}

	public String extractUsername(String token) {
		Claims claims = extractAllClaims(token);
		return claims.getSubject();
	}

	public Date extractExpiration(String token) {
		return extractAllClaims(token).getExpiration();
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public String generateToken(String username) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, username);
	}

	private String createToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 )) // 1 minutes expiration time
				.signWith(getSigningKey()).compact();
	}

	public Boolean validateToken(String token) {
		return (!isTokenExpired(token)&& !blackList.isBlackListed(token));
	}

//	private String createToken(Map<String, Object> claims, String subject) {
//	    String token = Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
//	            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60)) // 1 minute expiration time
//	            .signWith(getSigningKey()).compact();
//	    
//	    // Store the token in memory for demonstration purposes
//	    blacklistedTokens.putIfAbsent(token, null); // Initially not blacklisted
//	    
//	    return token;
//	}
//	public boolean validateToken(String token) {
//	    if (blacklistedTokens.containsKey(token)) {
//	        return false; // Token is blacklisted
//	    }
//	    
//	    try {
//	        Jwts.parser().setSigningKey(getSigningKey()).parseClaimsJws(token);
//	        return true; // Token is valid and not blacklisted
//	    } catch (JwtException e) {
//	        return false; // Token is invalid
//	    }
//	}

}
