package com.stockmanagement.stockmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class StockmanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockmanagementApplication.class, args);
	}

}
