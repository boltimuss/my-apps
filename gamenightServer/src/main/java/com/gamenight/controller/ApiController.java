package com.gamenight.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gamenight.dao.AuthenticationDao;
import com.gamenight.dao.models.User;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("gamenight")
@Slf4j
public class ApiController {

	@Autowired
	private AuthenticationDao authenticationDao;
	
	@PostMapping("/enterUser")
	public User enterUser(@RequestBody User newUser)
	{
		return authenticationDao.createUser(newUser);
	}
	
	@GetMapping("/authenticateUser/{userName}/{password}")
	public boolean authenticateUser(@PathVariable String userName, @PathVariable String password) 
	{
		User user = authenticationDao.readUser(userName);
		
		if (user == null || !user.getPassword().equals(password)) return false;
		else return true;
	}
	
}
