package com.api_blog.service;

import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
	
	public String generateToken(String username);
	
	public String extractUsername(String token);
	
	public Date extractExpiration(String token);
	
	public Boolean isTokenExpired(String token);
	
	public Boolean validateToken(String token, UserDetails userDetails);

}
