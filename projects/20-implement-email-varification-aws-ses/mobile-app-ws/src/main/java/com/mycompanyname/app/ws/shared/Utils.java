package com.mycompanyname.app.ws.shared;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

import org.springframework.stereotype.*;

import com.mycompanyname.app.ws.security.SecurityConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class Utils {
	
	private final Random RANDOM = new SecureRandom();
	private final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefjhigklmnopqrastuvwxyz";
	
	public String generateUserId(int length) {
		return generateRandomString(length);
	}
	
	public String generateAddressId(int length) {
		return generateRandomString(length);
	}

	public static boolean hasTokenExpired(String token) {
		boolean returnValue = false;
		
		Claims claims = Jwts.parser()
				.setSigningKey(SecurityConstants.getTokenSecrett())
				.parseClaimsJws(token)
				.getBody();
		
		Date tokenExpirationDate = claims.getExpiration();
		Date todayDate = new Date();
		
		returnValue = tokenExpirationDate.before(todayDate);
		
		return returnValue;
	}
	
	public String generateEmailVerificationToken(String userId) {
		String token = Jwts.builder()
				.setSubject(userId)
				.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecrett())
				.compact();
		
		return token;
	}
	
	private String generateRandomString(int length) {
		
		StringBuilder returnValue = new StringBuilder(length);
		
		for(int i = 0; i < length; i++) {
			returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
		}
		
		return returnValue.toString();
	}

}
