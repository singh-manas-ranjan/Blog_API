package com.api_blog.exceptions;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExpiredTokenException  extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	private String token;
	
	public ExpiredTokenException(String token) {
		super(String.format("Expired Token: %s",token));
//		this.token = token;
	}
}
