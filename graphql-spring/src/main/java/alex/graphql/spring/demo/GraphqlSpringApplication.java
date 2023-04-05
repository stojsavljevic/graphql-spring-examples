package alex.graphql.spring.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("alex.graphql")
public class GraphqlSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(GraphqlSpringApplication.class, args);
	}
	
}
