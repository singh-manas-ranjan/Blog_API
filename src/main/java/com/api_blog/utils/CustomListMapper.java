package com.api_blog.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.api_blog.entities.Comment;
import com.api_blog.entities.Post;
import com.api_blog.payload.comment.CommentDto;
import com.api_blog.payload.post.PostDto;

@Component
public class CustomListMapper {
	
	@Autowired
	private ModelMapper mapper;
	
	public List<CommentDto> mapCommentList(Page<Comment> page){
		List<CommentDto> dto = page.stream().map(comment -> mapper.map(comment, CommentDto.class))
				.collect(Collectors.toList());
		return dto;
	}
	
	
	public List<PostDto> mapPostList(Page<Post> page) {
		List<PostDto> dto = page.stream().map(post -> mapper.map(post, PostDto.class))
				.collect(Collectors.toList());
		return dto;
	}
	
	
}
