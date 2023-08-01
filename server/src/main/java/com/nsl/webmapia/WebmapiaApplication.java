package com.nsl.webmapia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class WebmapiaApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebmapiaApplication.class, args);
	}

}
