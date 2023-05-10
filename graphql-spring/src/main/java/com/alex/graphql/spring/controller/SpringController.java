package com.alex.graphql.spring.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;

import com.alex.graphql.core.data.DataHandler;
import com.alex.graphql.core.model.Author;
import com.alex.graphql.core.model.Post;
import com.alex.graphql.core.model.PostInput;

import reactor.core.publisher.Flux;

@Controller
@Secured("ROLE_USER")
class SpringController {

	Logger logger = LoggerFactory.getLogger(SpringController.class);

	@Autowired
	DataHandler dataHandler;

	@QueryMapping
	public List<Post> allPosts() {

		return this.dataHandler.getAllPosts();
	}

	@QueryMapping
	public List<Author> allAuthors() {

		return this.dataHandler.getAllAuthors();
	}

	@MutationMapping
	public Post createPost(@Argument PostInput postInput) {
		
		return this.dataHandler.createPost(postInput);
	}

	@SubscriptionMapping
	public Flux<Post> randomPost() {

		return dataHandler.getRandomPostPublisher();
	}

	@SchemaMapping(typeName = "Post", field = "author")
	public Author author(Post post) {

		return this.dataHandler.getAuthorById(post.getAuthorId());
	}

}
