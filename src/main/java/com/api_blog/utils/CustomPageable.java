package com.api_blog.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
@Component
public class CustomPageable {

	public  Pageable createPageable(Integer pageNumber, Integer pageSize, String sortBy, String orderBy) {
		
		Integer pN = Integer.parseInt(AppConstants.PAGE_NUMBER); // value = 1
		
		pageNumber = pageNumber >= pN ? pageNumber - pN : AppConstants.DEFAULT_PAGE_NUMBER; //value=0

		Sort sort = orderBy.equalsIgnoreCase(AppConstants.DECREASE) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		return pageable;
	}
}
