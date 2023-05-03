package com.api_blog.controller;

import java.io.UnsupportedEncodingException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api_blog.payload.changePassword.ChangePasswordDto;
import com.api_blog.payload.updateEmail.NewEmailDto;
import com.api_blog.payload.updateEmail.UpdateEmailDto;
import com.api_blog.payload.user.UserDto;
import com.api_blog.payload.user.UserDtoClient;
import com.api_blog.serviceImpl.UserServiceImpl;
import com.api_blog.utils.ApiResponse;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
	
	@Autowired
	private UserServiceImpl userRepo;
	
	// ::::::::::::::::::::::::::::::::::::::::::::::::::::: CREATE USER :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
			@PostMapping("/register")
			public ResponseEntity<?> createUser(@Valid @RequestBody UserDtoClient userDtoClient){
				boolean uName = userRepo.isUserExistsByUsername(userDtoClient.getUsername());
				boolean email = userRepo.isUserExistsByEmail(userDtoClient.getEmail());
				if(uName) {
					return new ResponseEntity<>(new ApiResponse("User Already Exists With username","403 FORBIDDEN"), HttpStatus.FORBIDDEN);
				}
				else if(email) {
					return new ResponseEntity<>(new ApiResponse("User Already Exists With email","403 FORBIDDEN"), HttpStatus.FORBIDDEN);
				}
				UserDto userDto = userRepo.createUser(userDtoClient);
				return new ResponseEntity<>(userDto,HttpStatus.CREATED);
			}

//	:::::::::::::::::::::::::::::::::::::::::::::::::::::: UPDATE USER BY ID (Basic Info only)  ::::::::::::::::::::::::::::::::::::::::::::::
	@PreAuthorize("hasRole('ROLE_USER')")
	@PutMapping("/update-profile")
	public ResponseEntity<?> updateUser(@Valid @RequestBody UserDto userDto,Principal principal ){
		userRepo.updateUser(principal, userDto);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
//	:::::::::::::::::::::::::::::::::::::::::::::::::::::: UPDATE USER EMAIL ::::::::::::::::::::::::::::::::::::::::::::::
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	@PostMapping("/account-settings/update-email")
	public ResponseEntity<?>updateEmailRequest(@Valid @RequestBody NewEmailDto newEmailDto, Principal principal, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException{
		ApiResponse response = userRepo.updateEmailRequest(newEmailDto.getNewEmail(), principal,request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	@PutMapping("/account-settings/update-email")
	public ResponseEntity<?> updateEmail(@Valid @RequestBody UpdateEmailDto updateEmailDto, Principal principal){
		Boolean isValid = userRepo.validateOtp(principal, updateEmailDto.getOtp());
		if(! isValid) {
			return new ResponseEntity<>(new ApiResponse("Invalid or Expired OTP", "FAILED"), HttpStatus.OK);
		}
		userRepo.updateEmail(principal.getName(), updateEmailDto.getNewEmail());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
//	:::::::::::::::::::::::::::::::::::::::::::::::::::::: CHANGE USER PASSWORD ::::::::::::::::::::::::::::::::::::::::
	@PreAuthorize("hasRole('ROLE_USER')")
	@PutMapping("/account-settings/change-password")
	public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto, Principal principal){
		
		if(changePasswordDto.getConfirmNewPassword().equals(changePasswordDto.getNewPassword())) {
			Boolean isSuccess = userRepo.changePassword(principal,changePasswordDto);
			if(isSuccess) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			else {
				return new ResponseEntity<>(new ApiResponse("Invalid Old Password", "FAILED"), HttpStatus.OK);
			}
		}
		else {
			return new ResponseEntity<>(new ApiResponse("Passwords do NOT Match", "FAILED"), HttpStatus.OK);
		}
	}


	
}
