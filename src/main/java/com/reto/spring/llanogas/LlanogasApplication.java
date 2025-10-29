package com.reto.spring.llanogas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LlanogasApplication {

	public static void main(String[] args) {
		SpringApplication.run(LlanogasApplication.class, args);
	}

}
