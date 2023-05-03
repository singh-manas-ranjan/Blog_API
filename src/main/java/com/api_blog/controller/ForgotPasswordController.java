package com.api_blog.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.api_blog.entities.User;
import com.api_blog.payload.forgotPassword.RecoveryMailId;
import com.api_blog.payload.forgotPassword.ResetPasswordDetails;
import com.api_blog.serviceImpl.ResetPasswordServiceImpl;
import com.api_blog.utils.ApiResponse;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@SecurityRequirement(name = "bearerAuth")
public class ForgotPasswordController {
	
	@Autowired
	private ResetPasswordServiceImpl resetPassword;
	
//	:::::::::::::::::::::::::::::::::::::::::::::::::::::: FORGOT-PASSWORD LINK :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	@PostMapping("/forgot-password")
	public ResponseEntity<?> forgotPassword(@Valid @RequestBody RecoveryMailId mailId, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException{
		ApiResponse response = resetPassword.updateResetPasswordTokenAndSendMail(mailId.getEmailId(), request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

//	:::::::::::::::::::::::::::::::::::::::::::::::::::::: RESET PASSWORD LINK :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	@PostMapping("/reset-password/{resetToken}")
	public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordDetails resetPasswordDetails, @PathVariable String resetToken){
		
		if(resetPasswordDetails.getConfirmPassword().equals(resetPasswordDetails.getPassword()))
		{
			User user = resetPassword.findUserByResetPasswordToken(resetToken);
			
			if(user==null) {
				return new ResponseEntity<>(new ApiResponse("Invalid or Expired Reset Password Link", "400 BAD_REQUEST"), HttpStatus.BAD_REQUEST);
			}
			
			Boolean isOtpValid = resetPassword.validateOtp(resetPasswordDetails.getOtp(), user.getUsername());
			
			if(isOtpValid) {
				resetPassword.updatePassword(user.getUsername(), resetPasswordDetails.getConfirmPassword());
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			else {
				return new ResponseEntity<>(new ApiResponse("Invalid or Expired OTP", "FAILED"), HttpStatus.OK);
			}
		}
		else {
			return new ResponseEntity<>(new ApiResponse("Passwords do NOT Match", "FAILED"), HttpStatus.OK);
		}
	}
	
	
}
