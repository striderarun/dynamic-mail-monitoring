package com.arun.reader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("spring-integration-context.xml")
public class EmailConfig {

	public static void main(String[] args) {
		SpringApplication.run(EmailConfig.class, args);
	}
}
