package br.com.rafaelblomer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class BancoSimplificadoApplication {

	public static void main(String[] args) {
		SpringApplication.run(BancoSimplificadoApplication.class, args);
	}

}
