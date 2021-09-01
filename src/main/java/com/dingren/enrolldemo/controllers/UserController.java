package com.dingren.enrolldemo.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dingren.enrolldemo.JwtConfig.JwtTokenProvider;
import com.dingren.enrolldemo.domains.User;
import com.dingren.enrolldemo.exceptions.noAccessException;
import com.dingren.enrolldemo.repositories.UserRepository;
import com.dingren.enrolldemo.services.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@CrossOrigin(origins="http://localhost:3000")
@RestController
public class UserController {
	
	@Autowired
	UserService userservice;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	
	@PostMapping("/login")
	public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, Object> user){
		String username = (String) user.get("username");
		String password = (String) user.get("password");
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		Map<String, String> map= new HashMap<String, String>();
		if (authentication.isAuthenticated()) {
			map.put("username", authentication.getName());
			map.put("role", authentication.getAuthorities().iterator().next().toString());
			map.put("token", jwtTokenProvider.createToken(username, userRepository.findByUsername(username).getRole()));
		}
		return new ResponseEntity<>(map, HttpStatus.OK);
		
	}
	
	@PostMapping("/signup")
	public ResponseEntity<Map<String, String>> register(@RequestBody Map<String, Object> user){
		String username = (String) user.get("username");
		String password = (String) user.get("password");
		//String role = (String) user.get("role");
		User newuser = userservice.registerUser(username, password);
		Map<String, String> map= new HashMap<String, String>();
		map.put("message", "registration success");
		return new ResponseEntity<>(map, HttpStatus.CREATED);
		
	}
	
	@CrossOrigin(origins="http://localhost:3000")
	@DeleteMapping("/removeuser")
	public ResponseEntity<Map<String, String>> remove(@RequestParam String username){
		String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().toString();
		Map<String, String> map= new HashMap<String, String>();
		if(role.equals("admin")) {
			if(userservice.deleteUser(username)) {
				map.put("username", username);
				return new ResponseEntity<>(map, HttpStatus.OK);
			}else {
				map.put("error", "oops, something went wrong");
				return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}else {
			map.put("error", "No access to remove user");
			return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
		}
		
	}
	
	@GetMapping("/listusers")
	public ResponseEntity<List<User>> listUsers() throws noAccessException{
		String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().toString();
		if(role.equals("admin")) {
			return new ResponseEntity<> (userservice.listUsers(), HttpStatus.OK);
		}else {
			throw new noAccessException("no access to user list");
		}
	}
	
}
