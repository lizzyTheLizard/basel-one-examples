package ch.baselone.springsecurity.example.flux.username;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * An application with a very simple username / passwort setup
 */
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(ch.baselone.springsecurity.example.flux.username.Application.class, args);
	}
}
