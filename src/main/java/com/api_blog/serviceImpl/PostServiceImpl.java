package com.api_blog.serviceImpl;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.api_blog.entities.Category;
import com.api_blog.entities.Post;
import com.api_blog.entities.User;
import com.api_blog.exceptions.ResourceNotFoundException;
import com.api_blog.payload.post.PostDto;
import com.api_blog.payload.post.PostDtoResponse;
import com.api_blog.repository.CategoryRepository;
import com.api_blog.repository.PostRepository;
import com.api_blog.repository.UserRepository;
import com.api_blog.service.PostService;
import com.api_blog.utils.AppConstants;
import com.api_blog.utils.CustomListMapper;
import com.api_blog.utils.CustomPageable;

@Service
public class PostServiceImpl implements PostService {
	
	@Autowired
	private PostRepository postRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private CategoryRepository  categoryRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private ImageServiceImpl imageService;
	
	@Autowired
	private CustomPageable customPageable;
	
	@Autowired
	private CustomListMapper customMapper;
	
	@Override
	public Boolean isPostExistsById(Integer id) {
		return postRepo.existsById(id);
	}
	
	@Override
	public Boolean isPostExistsByTile(String title) {
		return postRepo.existsByTitle(title);
	}
	
	@Override
	public PostDto createPost(Principal principal, PostDto postDto, Integer categoryId) {
		User user = userRepo.findByUsername(principal.getName());
		Category category = categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category", "CategoryId", categoryId));
		Post post = mapper.map(postDto, Post.class);
		post.setCategory(category);
		post.setUser(user);
		post.setImage(AppConstants.DEFAULT_IMAGE);
		post.setCreatedOn(LocalDateTime.now());
		Post savedPost = postRepo.save(post);
		return mapper.map(savedPost, PostDto.class);
	}

	@Override
	public PostDto updatePost(Integer id, PostDto postDto, Principal principal) {
		
		Post post = postRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post", "Id", id));
		
		if(!post.getUser().getUsername().equals(principal.getName())) {
			throw new ResourceNotFoundException("Post", "Post Id", id);
		}
		
		post.setTitle(postDto.getTitle());
		post.setDescription(postDto.getDescription());
		post.setContent(postDto.getContent());
		
		return mapper.map(postRepo.save(post), PostDto.class);
	}
	
	@Override
	public PostDto updatePostImage(Integer id, MultipartFile file, Principal principal) throws IOException {
		
		Post post = postRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post", "Post Id", id));
		
		if(!post.getUser().getUsername().equals(principal.getName())) {
			throw new ResourceNotFoundException("Post", "Post Id", id);
		}
	
		post.setImage(imageService.uploadImage(AppConstants.POSTS_IMAGE_LOCATION, file));

		return mapper.map(postRepo.save(post), PostDto.class);
	}
	
	@Override
	public void deletePostById(Integer id, Principal principal) {
		
		Post post = postRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post", "Post Id", id));
		
		Boolean isRoleAdmin = userRepo.findByUsername(principal.getName())
				.getRoles()
				.stream()
				.anyMatch(role -> role.getRoleName()
						.equals("ROLE_ADMIN"));
		
		if(post.getUser().getUsername().equals(principal.getName()) || isRoleAdmin) {
			postRepo.deleteById(id);
		}
		throw new ResourceNotFoundException("Post", "Post Id", id);
	}
	
	@Override
	public PostDto findPostById(Integer id) {
		Post post = postRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post", "Post Id", id));
		return mapper.map(post, PostDto.class);
	}
	
//	================================================== CREATE PostDtoResponse ========================================================================
	
	private PostDtoResponse createPostDtoResponse(List<PostDto> dto, Page<Post> page) {
		
		PostDtoResponse postDtoResponse = new PostDtoResponse();
		
		postDtoResponse.setContent(dto);
		postDtoResponse.setPageNumber(page.getNumber()+Integer.parseInt(AppConstants.PAGE_NUMBER));
		postDtoResponse.setPageSize(page.getSize());
		postDtoResponse.setTotalElements(page.getTotalElements());
		postDtoResponse.setTotalPages(page.getTotalPages());
		postDtoResponse.setFirstPage(page.isFirst());
		postDtoResponse.setLastPage(page.isLast());
		
		return postDtoResponse;
	}
	
	@Override
	public PostDtoResponse findAllPosts(Integer pageNumber, Integer pageSize, String sortBy, String orderBy) {
		
		Pageable pageable = customPageable.createPageable(pageNumber, pageSize, sortBy, orderBy);
		
		Page<Post> page = postRepo.findAll(pageable);
		
		List<PostDto> dto = customMapper.mapPostList(page);
		
		return createPostDtoResponse(dto, page);
	}
	
	@Override
	public PostDtoResponse findAllPostsByUser(Integer userId, Integer pageNumber, Integer pageSize, String sortBy, String orderBy) {
		
		User user = userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("Post", "User Id", userId));
		
		Pageable pageable = customPageable.createPageable(pageNumber, pageSize, sortBy, orderBy);
		
		Page<Post> page = postRepo.findAllByUser(user,pageable);
		
		List<PostDto> dto = customMapper.mapPostList(page);
		
		return createPostDtoResponse(dto,page);
	}
	
	@Override
	public PostDtoResponse findAllPostsByCategory(Integer categoryId, Integer pageNumber, Integer pageSize, String sortBy, String orderBy) {
		
		Category category = categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Post", "Category Id", categoryId));
		
		Pageable pageable = customPageable.createPageable(pageNumber, pageSize, sortBy, orderBy);
		
		Page<Post> page = postRepo.findAllByCategory(category,pageable);
		
		List<PostDto> dto = customMapper.mapPostList(page);
		
		return createPostDtoResponse(dto,page);
	}
	
	@Override
	public PostDtoResponse  searchPost(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String orderBy) {
		
		Pageable pageable = customPageable.createPageable(pageNumber, pageSize, sortBy, orderBy);
		
		Page<Post> page = postRepo.findByTitleContaining(keyword,pageable);
		
		List<PostDto> dto = customMapper.mapPostList(page);
		
		return createPostDtoResponse(dto,page);
	}



}
