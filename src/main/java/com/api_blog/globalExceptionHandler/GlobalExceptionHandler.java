package com.api_blog.globalExceptionHandler;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.api_blog.exceptions.ExpiredTokenException;
import com.api_blog.exceptions.ResourceNotFoundException;
import com.api_blog.utils.ApiResponse;

import jakarta.mail.MessagingException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex){
		String message = ex.getMessage();
		return new ResponseEntity<>(new ApiResponse(message,"BAD_REQUEST"), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> methodArgumentNotValidException(MethodArgumentNotValidException ex){
		
		Map<String,String> errorMap = new HashMap<>();
		
		ex.getBindingResult().getAllErrors().forEach((error)->{
			String errorField = ((FieldError)error).getField();
			String errorMsg = error.getDefaultMessage();
			errorMap.put(errorField, errorMsg);
		});
		return new ResponseEntity<>(errorMap, HttpStatus.FORBIDDEN);
	}

	
	@ExceptionHandler({MessagingException.class,UnsupportedEncodingException.class})
	public ResponseEntity<?> emailServiceException(Exception ex){
		return new ResponseEntity<>(new ApiResponse("OOPS! Something Went Wrong Please Try Again Later","500 INTERNAL_SERVER_ERROR"), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<?> accessDeniedException(AccessDeniedException ex){
		return new ResponseEntity<>(new ApiResponse("Page Not Found","404 NOT_FOUND"), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<?> badCredentialsException(BadCredentialsException ex){
		return new ResponseEntity<>(new ApiResponse("Invalid Credentials","403 FORBIDDEN"), HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(ExpiredTokenException.class)
	public ResponseEntity<?> expiredTokenException(ExpiredTokenException ex){
		return new ResponseEntity<>(new ApiResponse(ex.getMessage(),"403 FORBIDDEN"), HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(DisabledException.class)
	public ResponseEntity<?> disabledException(DisabledException ex){
		return new ResponseEntity<>(new ApiResponse("Account is Dissabled Due to Violation of Community Guidelines","403 FORBIDDEN"), HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(LockedException.class)
	public ResponseEntity<?> lockedException(LockedException ex){
		return new ResponseEntity<>(new ApiResponse("Account Is Locked For Exceding Incorrect Login Attempts (Try after 30 minutes)","403 FORBIDDEN"), HttpStatus.FORBIDDEN);
	}
	


}
