package com.api_blog.serviceImpl;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.api_blog.utils.ApiResponse;
import com.api_blog.utils.AppConstants;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailSenderService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	public ApiResponse sendResetPasswordRequest(String firstName, String to, Date at,String otp, String resetPasswordUrl) throws UnsupportedEncodingException, MessagingException {
		
		final String content = "<p>Hi "+firstName+",</p>"
				+"<p>We recieved a requested for reset password link at :<b>"+at+"</b></p>"
				+"<p>Your OTP is : "+otp+"</p>"
				+"<a href=\""+resetPasswordUrl+"\">Reset Password Link</a>"
				+"<p><b>OTP is valid for 10 minutes</b></p>"
				+"<p>If you did not request this link, ignore this mail.</p>"
				+"<p><b>Do not forward or give this link to anyone.</b></p>"
				+"<p>Sincerely yours,</p>"
				+"<p>The Blog_APi Support Team</p>";
		
				sendMail(to, content);
		
		return new ApiResponse("Check Email For Reset Password Request", "200 OK");
	}
	
	public ApiResponse sendUpdateEmailRequest(String firstName, String to, Date at,String otp) throws MessagingException, UnsupportedEncodingException {
		
		final String content = "<p>Hi "+firstName+",</p>"
				+"<p>We recieved a requested for update Email at :<b>"+at+"</b></p>"
				+"<p>Your OTP is : "+otp+"</p>"
				+"<p><b>OTP is valid for 10 minutes</b></p>"
				+"<p>If you did not request this link, ignore this mail.</p>"
				+"<p><b>Do not forward or give this link to anyone.</b></p>"
				+"<p>Sincerely yours,</p>"
				+"<p>The Blog_APi Support Team</p>";
		
		sendMail(to, content);
		
		return new ApiResponse("Check Email For Update Email Request", "200 OK");
	}
	
	private void sendMail(String to, String content) throws MessagingException, UnsupportedEncodingException {
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setFrom(AppConstants.EMAIL_ADDRESS, AppConstants.EMAIL_NAME);
		helper.setTo(to);
		helper.setSubject(AppConstants.EMAIL_SUBJECT);
		helper.setText(content, true);
		
		mailSender.send(message);
	}
}
