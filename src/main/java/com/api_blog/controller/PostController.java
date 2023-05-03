package com.api_blog.controller;

import java.io.IOException;
import java.security.Principal;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.api_blog.payload.post.PostDto;
import com.api_blog.payload.post.PostDtoResponse;
import com.api_blog.serviceImpl.PostServiceImpl;
import com.api_blog.utils.ApiResponse;
import com.api_blog.utils.AppConstants;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/posts")
@SecurityRequirement(name = "bearerAuth")
public class PostController {
	
	@Autowired
	private PostServiceImpl postRepo;
	
//	:::::::::::::::::::::::::::::::::::::::::::::::::::::: GET ALL POSTS :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	@GetMapping("/")
	public ResponseEntity<?> findAllPosts(
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "orderBy", defaultValue = AppConstants.ORDER_BY, required = false) String orderBy
			){
		PostDtoResponse postDtoResponse = postRepo.findAllPosts(pageNumber, pageSize, sortBy, orderBy);
		if(postDtoResponse.getContent().isEmpty()) {
			return new ResponseEntity<>(new ApiResponse("No Posts Are Present At The Moment", "OK with No Records"),HttpStatus.OK);
		}
		return new ResponseEntity<>(postDtoResponse, HttpStatus.OK);
	}

//	:::::::::::::::::::::::::::::::::::::::::::::::::::::: GET ALL POSTS BY CATEGORY :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	@GetMapping("/category/{categoryId}")
	public ResponseEntity<?> findAllPostsByCategory(@PathVariable Integer categoryId,
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "orderBy", defaultValue = AppConstants.ORDER_BY, required = false) String orderBy
			){
		PostDtoResponse postDtoResponse = postRepo.findAllPostsByCategory(categoryId,pageNumber, pageSize, sortBy, orderBy);
		if(postDtoResponse.getContent().isEmpty()) {
			return new ResponseEntity<>(new ApiResponse("No Posts Are Present At The Moment", "OK with No Records"),HttpStatus.OK);
		}
		return new ResponseEntity<>(postDtoResponse, HttpStatus.OK);
	}
	
//	:::::::::::::::::::::::::::::::::::::::::::::::::::::: GET ALL POSTS BY USER :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	@GetMapping("/user/{userId}")
	public ResponseEntity<?> findAllPostsByUser(@PathVariable Integer userId,
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "orderBy", defaultValue = AppConstants.ORDER_BY, required = false) String orderBy
			){
		PostDtoResponse postDtoResponse = postRepo.findAllPostsByUser(userId,pageNumber, pageSize, sortBy, orderBy);
		if(postDtoResponse.getContent().isEmpty()) {
			return new ResponseEntity<>(new ApiResponse("No Posts Are Present At The Moment", "OK with No Records"),HttpStatus.OK);
		}
		return new ResponseEntity<>(postDtoResponse, HttpStatus.OK);
	}
	
//	:::::::::::::::::::::::::::::::::::::::::::::::::::::: SEARCH POST BY KEYWORD :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	@GetMapping("/title/search-by")
	public ResponseEntity<?> searchPostsByKeyword(@RequestParam(value = "keyword",required = true) String keyword,
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "orderBy", defaultValue = AppConstants.ORDER_BY, required = false) String orderBy
			){
			PostDtoResponse postDtoResponse = postRepo.searchPost(keyword, pageNumber, pageSize, sortBy, orderBy);
			
			if(postDtoResponse.getContent().isEmpty()) {
				return new ResponseEntity<>(new ApiResponse("No Result Found", "OK with No Records"), HttpStatus.OK);
			}
		return new ResponseEntity<>(postDtoResponse, HttpStatus.OK);
	}
	
//	:::::::::::::::::::::::::::::::::::::::::::::::::::::: GET POSTS BY ID :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	@GetMapping("/{id}")
	public ResponseEntity<?> findPostById(@PathVariable Integer id){
		return new ResponseEntity<>(postRepo.findPostById(id), HttpStatus.OK);
	}
	
//	:::::::::::::::::::::::::::::::::::::::::::::::::::::: CREATE POSTS :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
@PostMapping("/category/{categoryId}")
public ResponseEntity<?> createPost(@Valid @RequestBody PostDto postDto,@PathVariable Integer categoryId, Principal principal){
	PostDto post = postRepo.createPost(principal,postDto, categoryId);
	if(post != null) {
		return new ResponseEntity<>(new ApiResponse("Post Created Successfully","SUCCESS"),HttpStatus.CREATED);
	}
	return new ResponseEntity<>(new ApiResponse("Somrthing went wrong!","FAILED"), HttpStatus.INTERNAL_SERVER_ERROR);
}

//	:::::::::::::::::::::::::::::::::::::::::::::::::::::: UPDATE POSTS BY ID :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	@PreAuthorize("hasRole('ROLE_USER')")
	@PutMapping("/{id}")
	public ResponseEntity<?> updatePost(@Valid @RequestBody PostDto postDto, @PathVariable Integer id, Principal principal){
		postRepo.updatePost(id, postDto, principal);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

//	:::::::::::::::::::::::::::::::::::::::::::::::::::::: UPDATE POSTS IMAGE :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping("/{id}")
	public ResponseEntity<?> updatePost(@RequestParam MultipartFile imageFile, @PathVariable Integer id, Principal principal) throws IOException{

		Tika tika = new Tika();
		String mimeType = tika.detect(imageFile.getBytes());
		
		if(mimeType.equalsIgnoreCase(AppConstants.IMAGE_PNG)) {
			
			postRepo.updatePostImage(id, imageFile, principal);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		else {
			return new ResponseEntity<>(new ApiResponse("Only .png File Is Accepted For Post Image","FORBIDDEN" ),HttpStatus.FORBIDDEN);
		}
		
	}

//	:::::::::::::::::::::::::::::::::::::::::::::::::::::: DELETEL POSTS BY ID :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletePost(@PathVariable Integer id, Principal principal){
		postRepo.deletePostById(id, principal);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		
	}
	
	
}
