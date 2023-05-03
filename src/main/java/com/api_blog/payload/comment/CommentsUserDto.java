package com.api_blog.payload.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class CommentsUserDto {
	
	private int id;
	
	private String firstname;
	
	private String lastname;
	
	private String email;

}
