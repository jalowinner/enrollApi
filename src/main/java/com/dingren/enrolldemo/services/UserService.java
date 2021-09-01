package com.dingren.enrolldemo.services;

import java.util.List;

import com.dingren.enrolldemo.domains.User;
import com.dingren.enrolldemo.exceptions.ConflictException;
import com.dingren.enrolldemo.exceptions.ResourceException;

public interface UserService {
	User registerUser(String username, String password) throws ConflictException;
	List<User> listUsers();
	boolean deleteUser(String username) throws ResourceException;
}
