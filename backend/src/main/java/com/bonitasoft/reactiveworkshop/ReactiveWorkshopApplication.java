package com.bonitasoft.reactiveworkshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class ReactiveWorkshopApplication {

	/**
	 * Root URL of the external service API
	 */
	public static final String EXTERNAL_SERVICE_API = "http://localhost:3004";

	public static void main(String[] args) {
		SpringApplication.run(ReactiveWorkshopApplication.class, args);
	}


	@Bean
	RestTemplate client() {
		return new RestTemplateBuilder().rootUri(EXTERNAL_SERVICE_API).build();
	}

	@Bean
	WebClient webClient() {
		return WebClient.create(EXTERNAL_SERVICE_API);
	}

}
