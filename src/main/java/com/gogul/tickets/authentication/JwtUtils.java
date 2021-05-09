package com.gogul.tickets.authentication;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtils {
	@Value("${jwt.jwtSecret:tickets}")
	private String jwtSecret;

	@Value("${jwt.jwtExpirationMs:240000}")
	private int jwtExpirationMs;
	
	private Set<String> skipURIs = new HashSet<String>(
			Arrays.asList(
					"/api/login",
					"/api/signup/users",
					"/api/movies",
					"/api/movies/schedule"
			));
	
	public Set<String> byPassingURIS(){
		return skipURIs; 
	}

	public String generateJwtToken(String username) {
		return Jwts.builder()
				.setSubject(username)
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public Map<String,String> generateJwtResponse(long id, String username) {
		Map<String,String> responseMap = new HashMap<String,String>();
		responseMap.put("token", generateJwtToken(username));
		responseMap.put("userid", id+"");
		responseMap.put("username", username);
		return responseMap;
	}

}
