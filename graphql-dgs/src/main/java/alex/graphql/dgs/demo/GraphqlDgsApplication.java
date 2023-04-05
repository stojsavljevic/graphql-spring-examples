package alex.graphql.dgs.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("alex.graphql")
public class GraphqlDgsApplication {

	public static void main(String[] args) {
		SpringApplication.run(GraphqlDgsApplication.class, args);
	}

}
