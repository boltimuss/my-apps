package com.gamenight.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("gamenight")
@Slf4j
public class ApiController {

	@GetMapping("/test")
	public boolean test() 
	{
		log.info("test");
	    return true;
	}
	
}
