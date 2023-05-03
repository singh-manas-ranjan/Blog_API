package com.api_blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.api_blog.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
	
//	@Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END FROM Category c WHERE title=:title")
//	Boolean isCategoryExistsByTitle(@Param("title") String title);
	
	Boolean existsByTitle(String title);
}
