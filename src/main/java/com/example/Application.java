package com.example;

import com.example.model.Order;
import com.example.services.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class Application {

	@Autowired
	private Producer producer;

	@PostConstruct
	public void run() {
		Order order = new Order();
		order.setId(42);
		producer.sendMessage(order);
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
