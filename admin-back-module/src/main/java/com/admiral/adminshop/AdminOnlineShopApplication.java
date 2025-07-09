package com.admiral.adminshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class AdminOnlineShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdminOnlineShopApplication.class, args);
	}

}
