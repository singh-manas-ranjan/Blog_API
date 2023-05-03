package com.api_blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.api_blog.payload.authRequest.AuthRequest;
import com.api_blog.serviceImpl.JwtServiceImpl;
import com.api_blog.utils.ApiResponse;

import jakarta.validation.Valid;

@RestController
public class AuthenticateController {
	
	@Autowired
	private JwtServiceImpl jwt;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@PostMapping("/authenticate")
	public ResponseEntity<?> authenticate(@Valid @RequestBody AuthRequest authRequest){
		
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		
		if(authentication.isAuthenticated()) {
			String token = jwt.generateToken(authRequest.getUsername());
			return new ResponseEntity<>(token,HttpStatus.OK);
		}
		return new ResponseEntity<>(new ApiResponse("Invalid Credentials", "UNAUTHORIZED"), HttpStatus.UNAUTHORIZED);
	}
}
