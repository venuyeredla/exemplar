package com.exemplar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
		/* exclude = {SecurityAutoConfiguration.class} */
	)
public class ExemplarApplication {
	public static void main(String[] args) {
		SpringApplication.run(ExemplarApplication.class, args);
	}
}