package com.api_blog.service;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;

import com.api_blog.payload.changePassword.ChangePasswordDto;
import com.api_blog.payload.user.UserDto;
import com.api_blog.payload.user.UserDtoClient;
import com.api_blog.utils.ApiResponse;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {
	
	UserDto getUserByUsename(String username);
	
	Boolean isUserExistsById(Integer id);
	
	Boolean isUserExistsByEmail(String email);
	
	Boolean isUserExistsByUsername(String username);
	
	UserDto findUserById(Integer id);
	
	List<UserDto> findAllUsers();
	
	UserDto createUser(UserDtoClient userDtoClient);
	
	UserDto updateUser(Principal principal, UserDto userDto);
	
	Boolean changePassword(Principal currentUser, ChangePasswordDto changePasswordDto);
	
	ApiResponse updateEmailRequest(String newEmail, Principal principal, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException;
	
	Boolean validateOtp(Principal principal, String otp);
	
	void updateEmail(String username, String newEmail);
	
	void deleteUseById(Integer id);
	
	void dissableUserAccount(Integer userId);

	void enableUserAccount(Integer userId);

}
