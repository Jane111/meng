package com.shiyi.meng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MengApplication {

	public static void main(String[] args) {
		SpringApplication.run(MengApplication.class, args);
	}

}
