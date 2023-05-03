package com.api_blog.exceptions;
public class ResourceNotFoundException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3553535689446999696L;

	public ResourceNotFoundException(String resourceField, String value) {
		super(String.format("Invalid %s : %s ",resourceField,value));
	}
	
	public ResourceNotFoundException(String resourceName, String resourceField, long fieldValue) {
		super(String.format("No %S found with %s: %s ",resourceName,resourceField,fieldValue));
	}
	
	public ResourceNotFoundException(String resourceName, String resourceField, String value) {
		super(String.format("No %S found with %s: %s ",resourceName,resourceField,value));
	}
	
}
