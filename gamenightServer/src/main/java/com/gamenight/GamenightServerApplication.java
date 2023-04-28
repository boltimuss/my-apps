package com.gamenight;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class GamenightServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GamenightServerApplication.class, args);
	}

}
