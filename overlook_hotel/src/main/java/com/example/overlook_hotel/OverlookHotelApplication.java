package com.example.overlook_hotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;


@SpringBootApplication
public class OverlookHotelApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load(); 
		System.setProperty("DB_URL", dotenv.get("DB_URL"));
        System.setProperty("DB_USER", dotenv.get("DB_USER"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
        System.setProperty("SECURITY_USER", dotenv.get("SECURITY_USER"));
        System.setProperty("SECURITY_PASSWORD", dotenv.get("SECURITY_PASSWORD"));
        System.setProperty("SECURITY_USER_ROLE", dotenv.get("SECURITY_USER_ROLE"));
		SpringApplication.run(OverlookHotelApplication.class, args);
	}

}