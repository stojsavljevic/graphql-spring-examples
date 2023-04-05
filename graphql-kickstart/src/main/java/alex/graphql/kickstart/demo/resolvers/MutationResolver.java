package alex.graphql.kickstart.demo.resolvers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import alex.graphql.core.data.DataHandler;
import alex.graphql.core.model.Post;
import alex.graphql.core.model.PostInput;
import graphql.kickstart.tools.GraphQLMutationResolver;

@Component
@Secured("ROLE_USER")
public class MutationResolver implements GraphQLMutationResolver {

	@Autowired
	DataHandler dataHandler;

	public Post createPost(PostInput postInput) {

		return dataHandler.createPost(postInput);
	}
}
