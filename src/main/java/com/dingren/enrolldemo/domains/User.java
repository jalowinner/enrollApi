package com.dingren.enrolldemo.domains;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "user_login")

public class User {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String username;
	private String password;
	private String role;
	private boolean isActive;
	
	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@ManyToMany(fetch=FetchType.LAZY, cascade=CascadeType.PERSIST)
	@JoinTable(name="user_courses",
		joinColumns = { @JoinColumn(name="uid")},
		inverseJoinColumns = {@JoinColumn(name="cid")})
	@JsonIgnoreProperties("users")
	private Set<Course> courses = new HashSet<Course>();
	
	@ManyToMany(fetch=FetchType.LAZY, cascade=CascadeType.PERSIST)
	@JoinTable(name="user_finished_courses",
		joinColumns = { @JoinColumn(name="uid")},
		inverseJoinColumns = {@JoinColumn(name="cid")})
	@JsonIgnoreProperties("pastUsers")
	private Set<Course> finishedCourses = new HashSet<Course>();
	@JsonIgnore
	public Set<Course> getFinishedCourses() {
		return finishedCourses;
	}

	public void setFinishedCourses(Set<Course> finishedCourses) {
		this.finishedCourses = finishedCourses;
	}

	public Long getid(){
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@JsonIgnore
	public Set<Course> getCourses() {
		return courses;
	}

	public void setCourses(Set<Course> courses) {
		this.courses = courses;
	}

	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getRole() {
		return role;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setRole(String role) {
		this.role = role;
	}

}
