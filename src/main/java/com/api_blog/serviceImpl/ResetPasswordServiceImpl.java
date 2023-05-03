package com.api_blog.serviceImpl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api_blog.entities.User;
import com.api_blog.exceptions.ResourceNotFoundException;
import com.api_blog.repository.UserRepository;
import com.api_blog.service.ResetPasswordService;
import com.api_blog.utils.ApiResponse;
import com.api_blog.utils.SiteUrl;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class ResetPasswordServiceImpl  implements ResetPasswordService{
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private EmailSenderService emailSender;
	
	@Override
	public ApiResponse updateResetPasswordTokenAndSendMail(String email, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
		
		User user = userRepo.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("User", "Email", email));
		String firstName = user.getFirstname();
		String resetToken = UUID.randomUUID().toString();
		String otp = String.valueOf( new Random().nextInt(9999999));
		Date at = new Date(System.currentTimeMillis()+1800000);
		
		user.setResetPasswordToken(resetToken);
		user.setOtp(otp);
		user.setOtpExpiration(at);
		userRepo.save(user);
		
		String resetUrl = SiteUrl.getSiteUrl(request)+File.separator+resetToken;
		
		return emailSender.sendResetPasswordRequest(firstName, email, at, otp, resetUrl);
	}
	
	@Override
	public User findUserByResetPasswordToken(String resetPasswordToken) {
		return userRepo.findByResetPasswordToken(resetPasswordToken).orElse(null);
	}
	
	@Override
	public Boolean validateOtp(String otp, String username) {
		User user = userRepo.findByUsername(username);
		return (Integer.parseInt(otp) == Integer.parseInt(user.getOtp()) && user.getOtpExpiration().after(new Date()));
	}
	
	@Override
	public void updatePassword(String username, String newPassword) {
		User user = userRepo.findByUsername(username);
		user.setPassword(passwordEncoder.encode(newPassword));
		user.setResetPasswordToken(null);
		user.setOtp(null);
		user.setOtpExpiration(null);
		userRepo.save(user);
	}

}
