package com.api_blog.entities;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.api_blog.utils.AppConstants;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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
@Setter
@Getter
@ToString
@Table(name = "users")
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false, length = 20)
	private String firstname;
	
	@Column(nullable = false, length = 20)
	private String lastname;
	
	@Column(nullable = false, length = 20, unique = true)
	private String username;
	
	@Column(nullable = false,unique = true)
	private String email;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false, length = 250)
	private String about;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private Set<Post> posts = new HashSet<>();
	
	@OneToMany( mappedBy = "user")
	private Set<Comment> comments = new HashSet<>();
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_roles", 
	joinColumns = @JoinColumn(name = "user_id"), 
	inverseJoinColumns = @JoinColumn(name = "role_id") )
	@Column(nullable = false)
	private Set<Role> roles = new HashSet<>();
	
	@Column(nullable=false, columnDefinition="boolean default true")
	private boolean accountNonExpired = AppConstants.TRUE;
	
	@Column(nullable=false, columnDefinition="boolean default true")
	private boolean accountNonLocked = AppConstants.TRUE;
	
	@Column(nullable=false, columnDefinition="boolean default true")
	private boolean credentialsNonExpired = AppConstants.TRUE;
	
	@Column(nullable=false, columnDefinition="boolean default true")
	private boolean enabled = AppConstants.TRUE;
	
	private String resetPasswordToken;
	
	private String otp;
	
	private Date otpExpiration;
	
	
// =====================================================	UserDetails Override Methods =================================================

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles;
	}

//	@Override
//	public boolean isAccountNonExpired() {
//		return accountNonExpired;
//	}
//
//	@Override
//	public boolean isAccountNonLocked() {
//		return accountNonLocked;
//	}
//
//	@Override
//	public boolean isCredentialsNonExpired() {
//		return credentialsNonExpired;
//	}
//
//	@Override
//	public boolean isEnabled() {
//		return enabled;
//	}


}
