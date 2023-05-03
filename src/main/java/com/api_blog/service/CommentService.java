package com.api_blog.service;

import java.security.Principal;

import com.api_blog.payload.comment.CommentDto;
import com.api_blog.payload.comment.CommentDtoResponse;

public interface CommentService {
	
	CommentDto createComment(Principal principal, Integer postId , CommentDto commentDto);
	
	CommentDto updateComment(Principal principal, Integer commentId, CommentDto commentDto);
	
	void deleteComment(Principal principal, Integer commentId);
	
	CommentDtoResponse findAllCommentsByPost(Integer postId, Integer pageNumber, Integer pageSize, String sortBy, String orderBy);
	
	
}
