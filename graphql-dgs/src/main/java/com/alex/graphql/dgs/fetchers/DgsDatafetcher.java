package com.alex.graphql.dgs.fetchers;

import java.util.List;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import com.alex.graphql.core.data.DataHandler;
import com.alex.graphql.core.model.Author;
import com.alex.graphql.core.model.Post;
import com.alex.graphql.core.model.PostInput;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.DgsSubscription;

@DgsComponent
public class DgsDatafetcher {
	
	@Autowired
	DataHandler dataHandler;


	@DgsQuery
	@Secured("ROLE_USER")
	public List<Post> allPosts() {

		return dataHandler.getAllPosts();
	}
	
	@DgsQuery
	@Secured("ROLE_USER")
	public List<Author> allAuthors() {

		return dataHandler.getAllAuthors();
	}
	
	@DgsMutation
	@Secured("ROLE_USER")
	public Post createPost(PostInput postInput) {

		return dataHandler.createPost(postInput);
	}

	@DgsSubscription
	@Secured("ROLE_USER")
	public Publisher<Post> randomPost() {
		
		return dataHandler.getRandomPostPublisher();
	}

	// security annotation here doesn't work for subscriptions some reason
	// @Secured("ROLE_USER")
	@DgsData(parentType = "Post", field = "author")
	public Author author(DgsDataFetchingEnvironment dfe) {

		Post post = dfe.getSource();
		return dataHandler.getAuthorById(post.getAuthorId());
	}

}
