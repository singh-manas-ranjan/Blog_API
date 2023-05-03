package com.api_blog.payload.authRequest;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AuthRequest {
	
	@NotBlank
	private String username;
	
	@NotBlank
	private String password;
}
