package com.food.manager;

import com.food.manager.config.OAuthService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FridgeManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FridgeManagerApplication.class, args);
		OAuthService oAuthService = new OAuthService();
		System.out.println(oAuthService.getOAuthToken());
	}

}
