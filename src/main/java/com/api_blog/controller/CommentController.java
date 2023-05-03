package com.api_blog.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.api_blog.payload.comment.CommentDto;
import com.api_blog.serviceImpl.CommentServiceImpl;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@SecurityRequirement(name = "bearerAuth")
public class CommentController {

	@Autowired
	private CommentServiceImpl commentRepo;
	
	
//	:::::::::::::::::::::::::::::::::::::::::::::::::::::: CREATE COMMENT :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	@PostMapping("/posts/{postId}/comments")
	public ResponseEntity<?> createComment(@Valid @RequestBody CommentDto commentDto, @PathVariable Integer postId, Principal principal){
		CommentDto dto = commentRepo.createComment(principal, postId, commentDto);
		return new ResponseEntity<>(dto,HttpStatus.CREATED);
	}
	
//	:::::::::::::::::::::::::::::::::::::::::::::::::::::: UPDATE COMMENT :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	@PreAuthorize("hasAnyRole('ROLE_USER')")
	@PutMapping("/comments/{commentId}")
	public ResponseEntity<?> updateComment(@Valid @RequestBody CommentDto commentDto, @PathVariable Integer commentId, Principal principal){
		commentRepo.updateComment(principal,commentId, commentDto);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
//	:::::::::::::::::::::::::::::::::::::::::::::::::::::: DELETE COMMENT :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	@DeleteMapping("/comment/{commentId}")
	public ResponseEntity<?> deleteComment(@PathVariable Integer commentId, Principal principal){
		commentRepo.deleteComment(principal,commentId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	
}
