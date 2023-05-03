package com.api_blog.serviceImpl;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api_blog.entities.Role;
import com.api_blog.entities.User;
import com.api_blog.exceptions.ResourceNotFoundException;
import com.api_blog.payload.changePassword.ChangePasswordDto;
import com.api_blog.payload.user.UserDto;
import com.api_blog.payload.user.UserDtoClient;
import com.api_blog.repository.RoleRepository;
import com.api_blog.repository.UserRepository;
import com.api_blog.service.UserService;
import com.api_blog.utils.ApiResponse;
import com.api_blog.utils.AppConstants;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private EmailSenderService emailSender;

	@Override
	public Boolean isUserExistsById(Integer id) {
		return userRepo.existsById(id);
	}

	@Override
	public Boolean isUserExistsByEmail(String email) {
		return userRepo.isUserExistsByEmail(email);
	}

	@Override
	public Boolean isUserExistsByUsername(String username) {
		return userRepo.existsByUsername(username);
	}

	@Override
	public UserDto findUserById(Integer id) {
		User user = userRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("User", "Id", id));
		return mapper.map( user, UserDto.class);
	}

	@Override
	public List<UserDto> findAllUsers() {
		List<User> uli = userRepo.findAll();
		List<UserDto> udtoli = uli.stream()
				.map(user -> mapper.map(user, UserDto.class))
				.collect(Collectors.toList());
		return udtoli;
	}
	
	@Override
	public UserDto createUser(UserDtoClient userDtoClient) {
		
		User user = mapper.map(userDtoClient, User.class);
		Role role = roleRepo.findById(AppConstants.ROLE_USER).get();
		user.getRoles().add(role);
		user.setPassword(encoder.encode(userDtoClient.getPassword()));
		return mapper.map(userRepo.save(user), UserDto.class);
	}

	@Override
	public UserDto updateUser(Principal principal, UserDto userDto) {
		
		User oldUser = userRepo.findByUsername(principal.getName());
		
		oldUser.setFirstname(userDto.getFirstname());
		
		oldUser.setLastname(userDto.getLastname());
		
		oldUser.setEmail(userDto.getEmail());
		
		oldUser.setAbout(userDto.getAbout());
		
		return mapper.map(userRepo.save(oldUser),UserDto.class);
	}

	@Override
	public void deleteUseById(Integer id) {
		User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "Id", id));
		userRepo.delete(user);
	}

	@Override
	public UserDto getUserByUsename(String username) {
		User user = userRepo.findByUsername(username);
		return mapper.map(user, UserDto.class);
	}

	@Override
	public Boolean changePassword(Principal principal, ChangePasswordDto changePasswordDto) {
		
		User user = userRepo.findByUsername(principal.getName());

		Boolean isMatch = changePasswordDto.getConfirmNewPassword().equals(user.getPassword());
		if(! isMatch) {
			return false;
		}
		else {
			user.setPassword(encoder.encode(changePasswordDto.getConfirmNewPassword()));
			userRepo.save(user);
			return true;
		}
	}

	@Override
	public ApiResponse updateEmailRequest(String newEmail, Principal principal, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
		
		User user = userRepo.findByUsername(principal.getName());
		Date at = new Date(System.currentTimeMillis()+1800000);
		String otp = String.valueOf(new Random().nextInt(9999999));
		
		user.setOtp(otp);
		user.setOtpExpiration(at);
		
		userRepo.save(user);

		return emailSender.sendUpdateEmailRequest(user.getFirstname(), newEmail, at, otp);
	}

	@Override
	public Boolean validateOtp(Principal principal, String otp) {
		User user = userRepo.findByUsername(principal.getName());
		return (Integer.parseInt(otp) == Integer.parseInt(user.getOtp()) && user.getOtpExpiration().after(new Date()));
	}

	@Override
	public void updateEmail(String username, String newEmail) {
		User user = userRepo.findByUsername(username);
		user.setEmail(newEmail);
		user.setOtp(null);
		user.setOtpExpiration(null);
		
		userRepo.save(user);
		
	}

	@Override
	public void dissableUserAccount(Integer userId) {
		User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "UserId", userId));
		user.setEnabled(false);
		userRepo.save(user);
	}

	@Override
	public void enableUserAccount(Integer userId) {
		User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "UserId", userId));
		user.setEnabled(true);
		userRepo.save(user);
	}


}
