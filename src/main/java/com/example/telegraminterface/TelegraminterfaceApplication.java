package com.example.telegraminterface;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration(exclude={org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration.class})
public class TelegraminterfaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TelegraminterfaceApplication.class, args);
	}

}
