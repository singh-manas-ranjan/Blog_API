package com.api_blog.payload.forgotPassword;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RecoveryMailId {

	@NotBlank(message = "*required")
	@Email(regexp = "^[a-z0-9+_.-]+@[gmail|yahoo]+.com", message = "only gmail & yahoo is accepted and no uppercase is allowed")
	private String emailId;
}
