package com.api_blog.serviceImpl;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api_blog.entities.Comment;
import com.api_blog.entities.Post;
import com.api_blog.entities.User;
import com.api_blog.exceptions.ResourceNotFoundException;
import com.api_blog.payload.comment.CommentDto;
import com.api_blog.payload.comment.CommentDtoResponse;
import com.api_blog.repository.CommentRepository;
import com.api_blog.repository.PostRepository;
import com.api_blog.repository.UserRepository;
import com.api_blog.service.CommentService;
import com.api_blog.utils.AppConstants;
import com.api_blog.utils.CustomListMapper;
import com.api_blog.utils.CustomPageable;

@Service
public class CommentServiceImpl implements CommentService {
	
	@Autowired
	private CommentRepository commentRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PostRepository postRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private CustomListMapper listMapper;
	
	@Autowired
	private CustomPageable customPageable;

	@Override
	public CommentDto createComment(Principal principal, Integer postId, CommentDto commentDto) {
		
		User user = userRepo.findByUsername(principal.getName());
		
		Post post = postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post", "Id", postId));
		
		Comment comment = mapper.map(commentDto, Comment.class);
		comment.setPost(post);
		comment.setUser(user);
		comment.setCommented_On(LocalDateTime.now());
		return mapper.map(commentRepo.save(comment), CommentDto.class);
	}
	
	private Comment getComment(Integer commentId) {
		return commentRepo.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment", "Comment Id", commentId));
	}

	@Override
	public CommentDto updateComment(Principal principal, Integer commentId, CommentDto commentDto) {
		
		Comment comment = getComment(commentId);
		
		if(!comment.getUser().getUsername().equals(principal.getName())) {
			throw new ResourceNotFoundException("Comment", "Comment Id", commentId);
		}
		comment.setContent(commentDto.getContent());

		return mapper.map(commentRepo.save(comment), CommentDto.class);
	}

	@Override
	public void deleteComment(Principal principal, Integer commentId) {
		
		Comment comment = getComment(commentId);
		
		Boolean isRoleAdmin = userRepo.findByUsername(principal.getName())
				.getAuthorities()
				.stream()
				.anyMatch(role -> role.getAuthority()
						.equals("ROLE_ADMIN"));
		
		if(comment.getUser().getUsername().equals(principal.getName()) || isRoleAdmin) {
			commentRepo.delete(comment);
		}
		throw new ResourceNotFoundException("Comment", "Comment Id", commentId);
	}
	
//	================================================== CREATE CommentDtoResponse ========================================================================
	
	private CommentDtoResponse createCommentDtoResponse(List<CommentDto> dto, Page<Comment> page) {
		
		CommentDtoResponse commentDtoResponse = new CommentDtoResponse();
		
		commentDtoResponse.setContent(dto);
		commentDtoResponse.setPageNumber(page.getNumber()+Integer.parseInt(AppConstants.PAGE_NUMBER));
		commentDtoResponse.setPageSize(page.getSize());
		commentDtoResponse.setTotalElements(page.getTotalElements());
		commentDtoResponse.setTotalPages(page.getTotalPages());
		commentDtoResponse.setFirstPage(page.isFirst());
		commentDtoResponse.setLastPage(page.isLast());
		
		return commentDtoResponse;
	}	

	@Override
	public CommentDtoResponse findAllCommentsByPost(Integer postId, Integer pageNumber, Integer pageSize, String sortBy, String orderBy) {
		Post post = postRepo.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post", "Id", postId));
		
		Pageable pageable = customPageable.createPageable(pageNumber, pageSize, sortBy, orderBy);
		
		Page<Comment> page = commentRepo.findAllByPost(post, pageable);
		
		List<CommentDto> dto = listMapper.mapCommentList(page);
		
		return createCommentDtoResponse(dto, page);
	}

}
