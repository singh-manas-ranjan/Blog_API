package com.api_blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api_blog.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	Role findByRoleName(String roleName);
}
