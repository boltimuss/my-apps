package com.gamenight.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gamenight.dao.models.User;
import com.gamenight.mongo.repositories.UserRepository;

@Component
public class AuthenticationDao {

	@Autowired
	private UserRepository userRepository;
	
	public User readUser(String userName)
	{
		return userRepository.findById(userName).orElse(null);
	}
	
	public User createUser(User user)
	{
		return userRepository.insert(user);
	}
}
