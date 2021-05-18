package com.example.restservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableJpaRepositories
public class RestServiceApplication {
	List<User> userList = new ArrayList<>();

	public static void main(String[] args) {
		SpringApplication.run(RestServiceApplication.class, args);
	}


}
