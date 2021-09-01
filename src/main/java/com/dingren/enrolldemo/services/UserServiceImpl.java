package com.dingren.enrolldemo.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dingren.enrolldemo.JwtConfig.JwtTokenProvider;
import com.dingren.enrolldemo.domains.Course;
import com.dingren.enrolldemo.domains.User;
import com.dingren.enrolldemo.exceptions.AuthException;
import com.dingren.enrolldemo.exceptions.ConflictException;
import com.dingren.enrolldemo.exceptions.ResourceException;
import com.dingren.enrolldemo.exceptions.noAccessException;
import com.dingren.enrolldemo.repositories.UserRepository;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
@Service
@Transactional
public class UserServiceImpl implements UserService{
	
	@Autowired
	UserRepository userrepo;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	JwtTokenProvider jwtTokenProvider;
	
	@Override
	public User registerUser(String username, String password) throws ConflictException {

		if(userrepo.findByUsername(username) != null) {
			throw new ConflictException("user exists!");
		}
		User user = new User();
		user.setUsername(username);
		user.setPassword(getEncoder().encode(password));
		user.setRole("user");
		userrepo.save(user);
		return user;
		
	}
	@Override
	public  boolean deleteUser(String username) throws ResourceException {
		User toRemove = userrepo.findByUsername(username);
		if(toRemove == null) {
			throw new ResourceException("user doesn't exist!");
		}
		if(toRemove.getRole().equals("admin")) {
			throw new noAccessException("can't delete other admin user!");
		}
		if (userrepo.deleteByUsername(username) == 1) {
			return true;
		}
		return false;
	}
	
	private PasswordEncoder getEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Override
	public List<User> listUsers() {
		return userrepo.findAll();
	}

}
