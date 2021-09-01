package com.dingren.enrolldemo.services;

import java.util.List;

import com.dingren.enrolldemo.domains.Course;
import com.dingren.enrolldemo.exceptions.ConflictException;
import com.dingren.enrolldemo.exceptions.ResourceException;

public interface CourseService {
	public List<Course> listAllCourses();
	public List<Course> listEnrolledCourses(String username);
	public Course addCourse(String coursename, String instructor, Integer workload, String coursetitle) throws ConflictException;
	public Course enrollCourse(String username, String coursename) throws ResourceException, ConflictException;
	public List<Course> listFinishedCourses(String curUser);
	public Course withdrawCourse(String username, String coursename);
	public boolean deleteCourse(String coursename) throws ResourceException;
	public Course passCourse(String username, String coursename);
}
