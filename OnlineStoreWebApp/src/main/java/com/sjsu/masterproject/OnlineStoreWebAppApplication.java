package com.sjsu.masterproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class OnlineStoreWebAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineStoreWebAppApplication.class, args);
	}
}
