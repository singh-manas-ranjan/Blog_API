package com.api_blog.service;

import java.util.List;

import com.api_blog.payload.category.CategoryDto;

public interface CategoryService {
	
	Boolean isCategoryExistsById(Integer id);
	
	Boolean isCategoryExistsByTile(String title);
	
	CategoryDto findCategoryById(Integer id);
	
	List<CategoryDto> findAllCategories();
	
	CategoryDto createCategory(CategoryDto categoryDto);
	
	CategoryDto updateCategory(Integer id, CategoryDto categoryDto);
	
	void deleteCategoryById(Integer id);
}
