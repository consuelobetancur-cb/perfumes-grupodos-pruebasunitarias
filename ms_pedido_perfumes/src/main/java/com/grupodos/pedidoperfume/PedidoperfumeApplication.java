package com.grupodos.pedidoperfume;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PedidoperfumeApplication {

	public static void main(String[] args) {
		SpringApplication.run(PedidoperfumeApplication.class, args);
	}

}
