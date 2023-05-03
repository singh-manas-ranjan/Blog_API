package com.api_blog.payload.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class CategoryDto {

	private int id;
	
	@Size(min = 3,max = 20)
	@NotBlank(message = "required field")
	private String title;
	
	@NotBlank(message = "required field")
	@Size(min = 10,max = 500)
	private String description;
}
