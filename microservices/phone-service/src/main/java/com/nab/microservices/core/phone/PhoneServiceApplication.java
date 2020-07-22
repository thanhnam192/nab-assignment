package com.nab.microservices.core.phone;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PhoneServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhoneServiceApplication.class, args);
	}

	@Bean
	public ObjectMapper mapper(){
		return new ObjectMapper();
	}
}
