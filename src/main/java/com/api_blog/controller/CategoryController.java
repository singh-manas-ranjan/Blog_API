package com.api_blog.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import com.api_blog.payload.category.CategoryDto;
import com.api_blog.serviceImpl.CategoryServiceImpl;
import com.api_blog.utils.ApiResponse;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/categories")
@SecurityRequirement(name = "bearerAuth")
public class CategoryController {
	
	@Autowired
	private CategoryServiceImpl categoryRepo;
	
//	:::::::::::::::::::::::::::::::::::::::::::::::::::::: CREATE CATEGORY :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/")
	public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDto categoryDto){
		boolean isExists = categoryRepo.isCategoryExistsByTile(categoryDto.getTitle());
		if( isExists) {
			return new ResponseEntity<>(new ApiResponse("Category Title Already Exists","FORBIDDEN"), HttpStatus.FORBIDDEN);
		}
		CategoryDto dto = categoryRepo.createCategory(categoryDto);
		return new ResponseEntity<>(dto,HttpStatus.CREATED);
	}
	
//	:::::::::::::::::::::::::::::::::::::::::::::::::::::: GET ALL CATEGORIES :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	@GetMapping("/")
	public ResponseEntity<?> findAllCategories(){
		List<CategoryDto> li = categoryRepo.findAllCategories();
		if(li.isEmpty()) {
			return new ResponseEntity<>(new ApiResponse("No Categories Are Present At The Moment", "OK with No Records"), HttpStatus.OK);
		}
		return new ResponseEntity<>(li,HttpStatus.OK);
	}
	
//	:::::::::::::::::::::::::::::::::::::::::::::::::::::: UPDATE CATEGORY :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<?> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable Integer id){
		categoryRepo.updateCategory(id, categoryDto);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

//	:::::::::::::::::::::::::::::::::::::::::::::::::::::: DELETE CATEGORY :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteCategory(@PathVariable Integer id){
		categoryRepo.deleteCategoryById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
