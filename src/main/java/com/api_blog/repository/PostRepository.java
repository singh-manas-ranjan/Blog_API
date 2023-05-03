package com.api_blog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.api_blog.entities.Category;
import com.api_blog.entities.Post;
import com.api_blog.entities.User;

public interface PostRepository extends JpaRepository<Post, Integer> {

	Boolean existsByTitle(String title);
	
	Page<Post> findAllByUser(User user,Pageable pageable);
	
	Page<Post> findAllByCategory(Category category, Pageable pageable);
	
	Page<Post> findByTitleContaining(String keyword, Pageable pageable);
}
