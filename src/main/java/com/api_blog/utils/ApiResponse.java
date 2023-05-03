package com.api_blog.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@Getter
@Setter
public class ApiResponse {
	private String Message;
	private String Status;
	public ApiResponse(String message, String status) {
		Message = message;
		Status = status;
	}
	
}
