package com.api_blog.payload.post;

import java.time.LocalDateTime;
import java.util.Set;

import com.api_blog.payload.category.CategoryDto;
import com.api_blog.payload.comment.CommentDto;
import com.api_blog.payload.user.UserDto;
import com.fasterxml.jackson.annotation.JsonFormat;

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
public class PostDto {
	
	private int id;
	
	@NotBlank(message = "required field")
	@Size(min = 10,max = 100)
	private String title;
	
	@NotBlank(message = "required field")
	@Size(min = 100,max = 1000)
	private String description;
	
	@NotBlank(message = "required field")
	@Size(min = 500,max = 10000)
	private String content;
	
	private String image;
	
	private CategoryDto category;
	
	private UserDto user;
	
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private LocalDateTime createdOn;
	
	private  Set<CommentDto> comments;
}
