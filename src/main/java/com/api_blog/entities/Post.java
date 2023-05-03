package com.api_blog.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "posts")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Post {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(nullable = false, length = 100)
	private String title;
	
	@Column(nullable = false, length = 1000)
	private String description;
	
	@Column(nullable = false, length = 10000)
	private String content;
	
	@Column(nullable = false)
	private String image;
	
	@ManyToOne
	@JoinColumn(name = "category_id", referencedColumnName = "id",nullable = false)
	private Category category;
	
	@ManyToOne
	@JoinColumn(name = "user_id",referencedColumnName = "id",nullable = false)
	private User user;
	
	private LocalDateTime createdOn;
	
	@OneToMany(mappedBy = "post",fetch = FetchType.EAGER)
	private Set<Comment> comments = new HashSet<>();
}
