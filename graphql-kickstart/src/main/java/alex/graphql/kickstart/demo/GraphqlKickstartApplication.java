package alex.graphql.kickstart.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("alex.graphql")
public class GraphqlKickstartApplication {

	public static void main(String[] args) {
		SpringApplication.run(GraphqlKickstartApplication.class, args);
	}

}
