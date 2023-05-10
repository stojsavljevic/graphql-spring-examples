package com.alex.graphql.kickstart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.alex.graphql")
public class GraphqlKickstartApplication {

	public static void main(String[] args) {
		SpringApplication.run(GraphqlKickstartApplication.class, args);
	}

}
