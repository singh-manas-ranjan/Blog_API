package com.api_blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api_blog.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	Boolean existsByUsername(String username);
	
	Optional<User> findByEmail(String email);
	
	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM User u WHERE email=:email")
	Boolean isUserExistsByEmail(@Param("email") String email);
	
//	Optional<User> findByUsername(String username);
	
	User findByUsername(String username);
	
	Optional<User> findByResetPasswordToken(String resetPasswordToken);
	
	
}
