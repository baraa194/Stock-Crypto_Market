package com.myProject.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StockMarketCryptoSimulatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockMarketCryptoSimulatorApplication.class, args);
	}

}
