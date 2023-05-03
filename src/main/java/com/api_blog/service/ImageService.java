package com.api_blog.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
	
	 String uploadImage(String location, MultipartFile file) throws IOException;
}
