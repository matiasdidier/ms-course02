package com.bcp.pe.customer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@Slf4j
@SpringBootApplication
@EnableEurekaClient
public class CustomerServiceApplication {

	public static void main(String[] args) {
		log.info("[main] Inicio");
		SpringApplication.run(CustomerServiceApplication.class, args);
		log.info("[main] Inicio");
	}

}
