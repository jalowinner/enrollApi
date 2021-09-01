package com.dingren.enrolldemo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dingren.enrolldemo.domains.Course;
import com.dingren.enrolldemo.domains.User;
import com.dingren.enrolldemo.exceptions.ConflictException;
import com.dingren.enrolldemo.exceptions.ResourceException;
import com.dingren.enrolldemo.repositories.CourseRepository;
import com.dingren.enrolldemo.repositories.UserRepository;

@Service
@Transactional
public class CourseServiceImpl implements CourseService{

	@Autowired
	CourseRepository courserepo;
	
	@Autowired
	UserRepository userrepo;
	
	@Override
	public List<Course> listAllCourses() {
		return courserepo.findAllSoft();
	}

	@Override
	public List<Course> listEnrolledCourses(String username) {
		// TODO Auto-generated method stub
		User user = userrepo.findByUsername(username);
		return new ArrayList<Course>(user.getCourses());
	}

	@Override
	public Course addCourse(String coursename, String instructor, Integer workload, String coursetitle) throws ConflictException {
		// TODO Auto-generated method stub
		if(courserepo.findByCoursenameSoft(coursename) != null) {
			throw new ConflictException("course exists!");
		}
		Course course = new Course();
		course.setCoursename(coursename);
		course.setInstructor(instructor);
		course.setWorkload(workload);
		course.setCourseTitle(coursetitle);
		courserepo.save(course);
		return course;
	}

	@Override
	public Course enrollCourse(String username, String coursename) throws ResourceException, ConflictException {
		User user = userrepo.findByUsername(username);
		Course course = courserepo.findByCoursenameSoft(coursename);
		if (course == null  || user == null) {
			throw new ResourceException("User/Course doesn't exists!");
		}
		else if(user.getCourses().contains(course)) {
			throw new ConflictException("You have already added this course!");
		}
		else {
			user.getCourses().add(course);
			return course;
		}
	}

	@Override
	public List<Course> listFinishedCourses(String curUser) {
		User user = userrepo.findByUsername(curUser);
		return new ArrayList<Course>(user.getFinishedCourses());
	}

	@Override
	public Course withdrawCourse(String username, String coursename) {
		User user = userrepo.findByUsername(username);
		Course course = courserepo.findByCoursenameSoft(coursename);
		if (course == null) {
			throw new ResourceException("Course doesn't exists!");
		}
		else if(!user.getCourses().contains(course)) {
			throw new ResourceException("You haven't enrolled in this course yet!");
		}
		else {
			user.getCourses().remove(course);
			return course;
		}
	}

	@Override
	public boolean deleteCourse(String coursename) throws ResourceException {
		Course toRemove = courserepo.findByCoursenameSoft(coursename);
		if(toRemove == null) {
			throw new ResourceException("course doesn't exist!");
		}
		Set<User> users = toRemove.getUsers();
		for(User user:users) {
			User withdrawUser = userrepo.findByUsername( user.getUsername());
			withdrawUser.getCourses().remove(toRemove);
		}
		toRemove.getUsers().clear();
		courserepo.deleteByCoursenameSoft(coursename);
		return true;
	}

	@Override
	public Course passCourse(String username, String coursename) {
		User user = userrepo.findByUsername(username);
		Course course = courserepo.findByCoursenameSoft(coursename);
		if (user == null || course == null) {
			throw new ResourceException("Course/User doesn't exists!");
		}
		else {
			user.getCourses().remove(course);
			user.getFinishedCourses().add(course);
			return course;
		}
	}

}
