package com.dingren.enrolldemo.domains;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "course")
public class Course {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String coursename;
	private String courseTitle;
	private String instructor;
	private Integer workload;
	private boolean isActive = true;
	
	
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	@ManyToMany(fetch=FetchType.LAZY, cascade= {CascadeType.PERSIST},mappedBy = "courses")
	@JsonIgnoreProperties("courses")
	private Set<User> users = new HashSet<>();
	
	@ManyToMany(fetch=FetchType.LAZY, cascade={CascadeType.PERSIST}, mappedBy = "finishedCourses")
	@JsonIgnoreProperties("finishedCourses")
	private Set<User> pastUsers = new HashSet<>();
	
	public String getCourseTitle() {
		return courseTitle;
	}
	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCoursename() {
		return coursename;
	}
	public void setCoursename(String coursename) {
		this.coursename = coursename;
	}
	public String getInstructor() {
		return instructor;
	}
	public void setInstructor(String instructor) {
		this.instructor = instructor;
	}
	public Integer getWorkload() {
		return workload;
	}
	public void setWorkload(Integer workload) {
		this.workload = workload;
	}
	@JsonIgnore
	public Set<User> getUsers() {
		return users;
	}
	public void setUsers(Set<User> users) {
		this.users = users;
	}
	@JsonIgnore
	public Set<User> getPastUsers() {
		return pastUsers;
	}
	public void setPastUsers(Set<User> pastUsers) {
		this.pastUsers = pastUsers;
	}
	@Override
	public boolean equals(Object o) {
		if (o instanceof Course) {
			Course course = (Course) o;
			return this.getId().equals(course.getId());
		}
		return false;
	}
	
}
