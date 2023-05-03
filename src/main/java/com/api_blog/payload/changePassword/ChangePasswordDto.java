package com.api_blog.payload.changePassword;

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
public class ChangePasswordDto {
	
	@NotBlank(message = "required field")
	private String oldPassword;
	
	@NotBlank(message = "required field")
	@Size(min = 8, max = 20)
	private String newPassword;
	
	@NotBlank(message = "required field")
	@Size(min = 8, max = 20)
	private String confirmNewPassword;
}
