package com.api_blog;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.api_blog.entities.Role;
import com.api_blog.repository.RoleRepository;

@SpringBootApplication
public class BlogApiApplication implements CommandLineRunner{
	
	@Autowired
	private RoleRepository roleRepo;

	public static void main(String[] args) {
		SpringApplication.run(BlogApiApplication.class, args);
	}
	
	@Bean
	ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Override
	public void run(String... args) throws Exception {
		
		Role admin = new Role();
		admin.setId(101);
		admin.setRoleName("ROLE_ADMIN");
		
		Role user = new Role();
		user.setId(102);
		user.setRoleName("ROLE_USER");
		
		List<Role> roles = List.of(admin,user);
		roleRepo.saveAll(roles);

		
	}
	

}
