package com.api_blog.payload.forgotPassword;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ResetPasswordDetails {
	
	@NotBlank(message = "required field")
	@Size(min = 8, max = 20)
	private String password;
	
	@NotBlank(message = "required field")
	@Size(min = 8, max = 20)
	private String confirmPassword;
	
	@NotBlank(message = "required field")
	private String otp;
}
