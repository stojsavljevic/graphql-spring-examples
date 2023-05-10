package com.alex.graphql.kickstart.resolvers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import com.alex.graphql.core.data.DataHandler;
import com.alex.graphql.core.model.Author;
import com.alex.graphql.core.model.Post;

import graphql.kickstart.tools.GraphQLQueryResolver;

@Component
@Secured("ROLE_USER")
public class QueryResolver implements GraphQLQueryResolver {
	
	@Autowired
	DataHandler dataHandler;
	
	public List<Author> allAuthors() {

		return dataHandler.getAllAuthors();
	}
	
	public List<Post> allPosts() {

		return dataHandler.getAllPosts();
	}

}
