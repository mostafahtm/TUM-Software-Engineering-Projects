package de.tum.in.ase.eist.eventmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EventMicroserviceApplication {

	public static void main(String[] args) {
		System.setProperty("server.port", "8081");
		SpringApplication.run(EventMicroserviceApplication.class, args);
	}

}
