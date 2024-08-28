package com.prachiSawantBajajProject;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PrachiBajajProjectApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(PrachiBajajProjectApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		GenerateMD5WithPRN.execute(args);
	}
}
