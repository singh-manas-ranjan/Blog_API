package com.api_blog.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api_blog.entities.Category;
import com.api_blog.exceptions.ResourceNotFoundException;
import com.api_blog.payload.category.CategoryDto;
import com.api_blog.repository.CategoryRepository;
import com.api_blog.service.CategoryService;
@Service
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	private CategoryRepository categoryRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@Override
	public Boolean isCategoryExistsById(Integer id) {
		return categoryRepo.existsById(id);
	}

	@Override
	public Boolean isCategoryExistsByTile(String title) {
		return categoryRepo.existsByTitle(title);
	}

	@Override
	public CategoryDto findCategoryById(Integer id) {
		Category category = categoryRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Category", "ID", id));
		return mapper.map(category, CategoryDto.class);
	}

	@Override
	public List<CategoryDto> findAllCategories() {
		List<Category> li = categoryRepo.findAll();
		List<CategoryDto> dto = li.stream()
				.map(category -> mapper.map(category, CategoryDto.class))
				.collect(Collectors.toList());
		return dto;
	}

	@Override
	public CategoryDto createCategory(CategoryDto categoryDto) {
		return mapper.map(
				categoryRepo.save(
						mapper.map(categoryDto, Category.class)),
				CategoryDto.class);
	}

	@Override
	public CategoryDto updateCategory(Integer id, CategoryDto categoryDto) {
		Category category = categoryRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Category", "Id", id));
		categoryDto.setId(category.getId());
		return mapper.map(
				categoryRepo.save(mapper.map(categoryDto, Category.class)),
			CategoryDto.class);
	}

	@Override
	public void deleteCategoryById(Integer id) {
		Boolean isExists = categoryRepo.existsById(id);
		if(!isExists) {
			throw new ResourceNotFoundException("Category", "id", id);
		}
		categoryRepo.deleteById(id);
	}

}
