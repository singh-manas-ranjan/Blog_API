package com.api_blog.payload.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class UserDtoClient {
	
	@NotBlank(message = "required field")
	@Size(min = 2, max = 20, message = "minimum 2 & maxium 20 character")
	private String firstname;
	
	@NotBlank(message = "required field")
	@Size(min = 2, max = 20, message = "minimum 2 & maxium 20 character")
	private String lastname;
	
	@NotBlank(message = "required field")
	@Size(min = 8, max = 20, message = "minimum 8 & maxium 10 character")
	private String username;
	
	@NotBlank(message = "required field")
	@Email(regexp = "^[a-z0-9+_.-]+@[gmail|yahoo|zoho]+.com", message = "only gmail,yahoo,zoho is accepted and no uppercase is allowed")
	private String email;
	
	@NotBlank(message = "required field")
	@Size(min = 8, max = 20)
	private String password;
	
	@NotBlank(message = "required field")
	@Size(min = 10, max = 250)
	private String about;
}
