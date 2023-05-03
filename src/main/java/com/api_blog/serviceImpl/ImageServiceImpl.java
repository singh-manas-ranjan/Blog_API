package com.api_blog.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.api_blog.service.ImageService;
@Service
public class ImageServiceImpl implements ImageService {

	@Override
	public String uploadImage(String location, MultipartFile file) throws IOException {
		
		String originalFileName = file.getOriginalFilename();
		
		String randomId = UUID.randomUUID().toString();
		
		String fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf(".")));
		
		File filePath = new ClassPathResource(location).getFile();
		
		Path uploadPath = Paths.get(filePath.getAbsolutePath()+File.separator+fileName);
		
		Files.copy(file.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);
				
		return fileName;
	}

}
