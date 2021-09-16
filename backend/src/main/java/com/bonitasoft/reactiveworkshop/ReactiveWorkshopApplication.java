package com.bonitasoft.reactiveworkshop;

import com.bonitasoft.reactiveworkshop.configuration.ExternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@RequiredArgsConstructor
public class ReactiveWorkshopApplication {

	private final ExternalService externalService;

	public static void main(String[] args) {
		SpringApplication.run(ReactiveWorkshopApplication.class, args);
	}


	@Bean
	RestTemplate client() {
		return new RestTemplateBuilder().rootUri(externalService.getUrl()).build();
	}

	@Bean
	WebClient webClient() {
		return WebClient.create(externalService.getUrl());
	}

}
