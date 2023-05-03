package com.api_blog.entities;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "roles")
public class Role implements GrantedAuthority{

	@Id
	private Integer id;
	
	private String roleName;
	
	@ManyToMany(mappedBy = "roles")
	private Set<User> user = new HashSet<>();

	@Override
	public String getAuthority() {
		return roleName;
	}


}
