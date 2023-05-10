package com.alex.graphql.dgs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.alex.graphql")
public class GraphqlDgsApplication {

	public static void main(String[] args) {
		SpringApplication.run(GraphqlDgsApplication.class, args);
	}

}
