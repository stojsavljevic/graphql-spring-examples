package alex.graphql.kickstart.demo.resolvers;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import alex.graphql.core.data.DataHandler;
import alex.graphql.core.model.Post;
import graphql.kickstart.tools.GraphQLSubscriptionResolver;
import graphql.schema.DataFetchingEnvironment;

@Component
@Secured("ROLE_USER")
class SubscriptionResolver implements GraphQLSubscriptionResolver {

	@Autowired
	DataHandler dataHandler;


	public Publisher<Post> randomPost(DataFetchingEnvironment env) {

		return dataHandler.getRandomPostPublisher();
	}
}