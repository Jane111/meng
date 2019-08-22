package com.shiyi.meng;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MengApplication {

	public static void main(String[] args) {
		SpringApplication.run(MengApplication.class, args);
	}
}
