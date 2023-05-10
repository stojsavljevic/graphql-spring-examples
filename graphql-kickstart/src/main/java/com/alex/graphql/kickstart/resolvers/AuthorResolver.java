package com.alex.graphql.kickstart.resolvers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alex.graphql.core.data.DataHandler;
import com.alex.graphql.core.model.Author;
import com.alex.graphql.core.model.Post;

import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

@Component
public class AuthorResolver implements GraphQLResolver<Post> {
	
	Logger logger = LoggerFactory.getLogger(AuthorResolver.class);
	
	@Autowired
	DataHandler dataHandler;
	
	// security annotation here doesn't work for subscriptions some reason - check SecurityConfig.java and README
	// @Secured("ROLE_USER")
	public Author author(Post post, DataFetchingEnvironment dfe) {
		return dataHandler.getAuthorById(post.getAuthorId());
	}
}
