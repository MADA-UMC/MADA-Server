package com.umc.mada;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MadaApplication {
	public static void main(String[] args) {
		SpringApplication.run(MadaApplication.class, args);
	}
}
