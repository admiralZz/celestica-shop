package com.admiral.adminshop;

import com.admiral.common.conf.CommonConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Import;

@ConfigurationPropertiesScan
@SpringBootApplication
@Import(CommonConfiguration.class)
public class AdminOnlineShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdminOnlineShopApplication.class, args);
	}

}
