package com.alex.graphql.kickstart.resolvers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import com.alex.graphql.core.data.DataHandler;
import com.alex.graphql.core.model.Post;
import com.alex.graphql.core.model.PostInput;

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
