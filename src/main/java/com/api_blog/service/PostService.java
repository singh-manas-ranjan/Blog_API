package com.api_blog.service;

import java.io.IOException;
import java.security.Principal;

import org.springframework.web.multipart.MultipartFile;

import com.api_blog.payload.post.PostDto;
import com.api_blog.payload.post.PostDtoResponse;

public interface PostService {
	
	Boolean isPostExistsById(Integer id);
	
	Boolean isPostExistsByTile(String title);
	
	//CREATE
	PostDto createPost(Principal principal, PostDto postDto, Integer categoryId);
	
	//UPDATE
	PostDto updatePost(Integer id,PostDto postDto, Principal principal);
	
	//UPDATE POST IMAGE
	PostDto updatePostImage(Integer id,MultipartFile file, Principal principal) throws IOException;
	
	//DELETE
	void deletePostById(Integer id, Principal principal);
	
	//FIND POST BY ID
	PostDto findPostById(Integer id);
	
	//FIND ALL POSTS
	PostDtoResponse findAllPosts(Integer pageNumber, Integer pageSize, String sortBy, String orderBy);
	
	//FIND ALL POSTS BY CATEGORY ID
	PostDtoResponse findAllPostsByCategory(Integer categoryId,Integer pageNumber, Integer pageSize, String sortBy, String orderBy);
	
	//FIND ALL POSTS BY USER ID
	PostDtoResponse findAllPostsByUser(Integer userId,Integer pageNumber, Integer pageSize, String sortBy, String orderBy);
	
	//SEARCH POST BY KEYWORD
	PostDtoResponse  searchPost(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String orderBy);
	
}
