package com.example.banking_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Spring Boot banking Application",
				description = "Backend REST API for banking appkication",
				version = "v1.0",
				contact = @Contact(
						name = "Shiv Shankar",
						email = "shankarshiv470@gmail.com",
						url = "https://github.com/Shivshankar108/spring-banking-app"
				),
				license = @License(
						name = "Shiv Shankar",
						url = "https://github.com/Shivshankar108/spring-banking-app"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "Spring Boot Banking Application Description",
				url = "https://github.com/Shivshankar108/spring-banking-app"
		)
)
public class BankingAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingAppApplication.class, args);
	}

}
