package com.dingren.enrolldemo.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.dingren.enrolldemo.domains.Course;
import com.dingren.enrolldemo.exceptions.noAccessException;
import com.dingren.enrolldemo.services.CourseService;

@CrossOrigin(origins="http://localhost:3000")
@RestController
public class CourseController {
	@Autowired
	CourseService courseservice;
	@GetMapping("/search")
	public ResponseEntity<List<Course>> allCourses(){
		List<Course> courses = courseservice.listAllCourses();
		return new ResponseEntity<List<Course>>(courses, HttpStatus.OK);
		
	}
	
	@GetMapping("/enrolled")
	public ResponseEntity<List<Course>> enrolledCourses() throws noAccessException{
		String curUser = SecurityContextHolder.getContext().getAuthentication().getName();
		List<Course> courses = courseservice.listEnrolledCourses(curUser);
		return new ResponseEntity<List<Course>>(courses, HttpStatus.OK);
		
	}
	
	@GetMapping("/finished")
	public ResponseEntity<List<Course>> finishedCourses() throws noAccessException{
		String curUser = SecurityContextHolder.getContext().getAuthentication().getName();
		List<Course> courses = courseservice.listFinishedCourses(curUser);
		return new ResponseEntity<List<Course>>(courses, HttpStatus.OK);
		
	}
	
	@PostMapping("/enroll")
	public ResponseEntity<Course> enrollCourse(@RequestBody Map<String, Object> body){
		String coursename = (String)body.get("coursename");		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Course course = courseservice.enrollCourse(username, coursename);
		return new ResponseEntity<>(course, HttpStatus.CREATED);
		
	}
	
	@CrossOrigin(origins="http://localhost:3000")
	@DeleteMapping("/withdraw")
	public ResponseEntity<Course> withdrawCourse(@RequestParam String coursename){	
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Course course = courseservice.withdrawCourse(username, coursename);
		return new ResponseEntity<>(course, HttpStatus.OK);
		
	}
	
	
	@PostMapping("/addCourse")
	public ResponseEntity<Map<String, String>> addCourse(@RequestBody Map<String, Object> course){
		String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().toString();
		Map<String, String> map= new HashMap<String, String>();
		if(role.equals("admin")) {
			String coursename = (String)course.get("coursename");
			String coursetitle = (String)course.get("coursetitle");
			String instructor = (String)course.get("instructor");
			Integer workload = Integer.valueOf((String) course.get("workload"));
			Course newCourse = courseservice.addCourse(coursename, instructor, workload,coursetitle);
			map.put("coursename", newCourse.getCoursename());
			return new ResponseEntity<>(map, HttpStatus.CREATED);
		}else {
			map.put("error", "you don't have right to add course");
			return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
		}
	}
	@CrossOrigin(origins="http://localhost:3000")
	@DeleteMapping("/removecourse")
	public ResponseEntity<Map<String, String>> removeCourse(@RequestParam String coursename){
		String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().toString();
		Map<String, String> map= new HashMap<String, String>();
		if(role.equals("admin")) {
			if(courseservice.deleteCourse(coursename)) {
				map.put("coursename", coursename);
				return new ResponseEntity<>(map, HttpStatus.OK);
			}else {
				map.put("error", "oops, something went wrong");
				return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}else {
			map.put("error", "you don't have right to delete course");
			return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
		}
	}
	@GetMapping("/getUserCourse")
	public ResponseEntity<List<Course>> getUserCourse(@RequestParam String username)throws noAccessException{
		String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().toString();
		if(role.equals("admin")) {
			List<Course> courses = courseservice.listEnrolledCourses(username);
			return new ResponseEntity<List<Course>>(courses, HttpStatus.OK);
		}
		else {
			throw new noAccessException("forbidden");
		}
	}
	
	@PostMapping("/passAction")
	public ResponseEntity<Course> passAction(@RequestBody Map<String, Object> body)throws noAccessException{
		String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().toString();
		if(role.equals("admin")) {
			
			Course course = courseservice.passCourse((String)body.get("username"), (String)body.get("coursename"));
			return new ResponseEntity<Course>(course, HttpStatus.OK);
		}
		else {
			throw new noAccessException("forbidden");
		}
	}
	
	@CrossOrigin(origins="http://localhost:3000")
	@DeleteMapping("/adminWithdraw")
	public ResponseEntity<Course> adminWithdrawCourse(@RequestParam String username, @RequestParam String coursename) throws noAccessException{
		String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().toString();
		if(role.equals("admin")) {
			Course course = courseservice.withdrawCourse(username, coursename);
			return new ResponseEntity<>(course, HttpStatus.OK);
		}else {
			throw new noAccessException("forbidden");
		}
		
	}
	
}
