package com.assignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.integration.config.EnableIntegration;

import com.assignment.config.SitaTestTaskConfiguration;

@SpringBootApplication
@EnableIntegration
@Import(SitaTestTaskConfiguration.class)
public class SitaTestTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(SitaTestTaskApplication.class, args);
	}

}
