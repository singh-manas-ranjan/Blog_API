package com.api_blog.service;

import java.io.UnsupportedEncodingException;

import com.api_blog.entities.User;
import com.api_blog.utils.ApiResponse;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

public interface ResetPasswordService {
	
	ApiResponse updateResetPasswordTokenAndSendMail(String email, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException;
	
	User findUserByResetPasswordToken(String resetPasswordToken);
	
	Boolean validateOtp(String otp, String username);
	
	void updatePassword(String username, String newPassword);
}
