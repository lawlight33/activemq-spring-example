package com.example;

import com.example.model.Order;
import com.example.services.Producer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(Application.class, args);

		// Send message
		Producer producer = applicationContext.getBean(Producer.class);
		producer.sendMessage(new Order(42));
	}
}
