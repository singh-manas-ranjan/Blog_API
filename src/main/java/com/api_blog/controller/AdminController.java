package com.api_blog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api_blog.payload.user.UserDto;
import com.api_blog.serviceImpl.UserServiceImpl;
import com.api_blog.utils.ApiResponse;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/admin")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {
	
	@Autowired
	private UserServiceImpl userRepo;
	
//	:::::::::::::::::::::::::::::::::::::::::::::::::::::: GET ALL USERS:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/users")
	public ResponseEntity<?> findAllUsers()
	{
		List<UserDto> userDtoList = userRepo.findAllUsers();
		if(userDtoList != null) {
			return ResponseEntity.ok(userDtoList);
		}
		else {
			return new ResponseEntity<>(new ApiResponse("No Users Are Present At The Moment","OK with No Records"), HttpStatus.OK);
		}
	}
	
// :::::::::::::::::::::::::::::::::::::::::::::::::::::: GET USER BY ID :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/users/{userId}")
	public ResponseEntity<?> findUserById(@PathVariable  int userId)
	{
		UserDto userDto = userRepo.findUserById(userId);
		return ResponseEntity.ok(userDto);
	}
	
//	:::::::::::::::::::::::::::::::::::::::::::::::::::::: DELETE USER BY ID :::::::::::::::::::::::::::::::::::::::::::::::::::::::
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/users/{userId}")
	public ResponseEntity<?> deleteUser(@PathVariable int userId) {
		userRepo.deleteUseById(userId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
		
// :::::::::::::::::::::::::::::::::::::::::::::::::::::: DISSABLE USER-ACCOUNT :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/users/dissable")
	public ResponseEntity<?> dissableUserAccount(@RequestParam Integer userId){
		userRepo.dissableUserAccount(userId);
		return new ResponseEntity<>(new ApiResponse("Account With UserId: "+userId+" Has Been Dissabled", "200 OK"), HttpStatus.OK);
	}
	
// :::::::::::::::::::::::::::::::::::::::::::::::::::::: ENABLE USER-ACCOUNT :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/users/enable")
	public ResponseEntity<?> enableUserAccount(@RequestParam Integer userId){
		userRepo.enableUserAccount(userId);
		return new ResponseEntity<>(new ApiResponse("Account With UserId: "+userId+" Has Been Enabled", "200 OK"), HttpStatus.OK);
	}

}
