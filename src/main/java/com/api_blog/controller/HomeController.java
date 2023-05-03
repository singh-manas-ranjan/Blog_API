package com.api_blog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api_blog.utils.ApiResponse;

@RestController
public class HomeController {
	
	@GetMapping("/")
	public ResponseEntity<?> homePage(){
		return new ResponseEntity<>(new ApiResponse("Welcom to Blog API use /swagger-ui.html", "200 OK"), HttpStatus.OK);
	}
	
}
