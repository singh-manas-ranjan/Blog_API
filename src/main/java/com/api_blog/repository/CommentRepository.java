package com.api_blog.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.api_blog.entities.Comment;
import com.api_blog.entities.Post;
import com.api_blog.entities.User;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
	
	Page<Comment> findAllByPost(Post post, Pageable pageable);
	
	List<Comment> findByUser(User user);
}
