package com.alex.graphql.kickstart;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.time.Duration;
import java.time.Year;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import com.alex.graphql.core.model.Post;
import com.alex.graphql.core.util.TestQueries;
import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestSubscription;
import com.graphql.spring.boot.test.GraphQLTestTemplate;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("no-security")
class GraphQLKickstartDemoTest {

	@Autowired
	GraphQLTestTemplate graphQLTestTemplate;

	@Autowired
	GraphQLTestSubscription graphQLTestSubscription;
	
	@Test
	void test_query_all_authors() throws IOException {
		
		GraphQLResponse response = this.graphQLTestTemplate.postForString(TestQueries.QUERY_ALL_AUTHORS);
		List<String> authorNames = response.getList(TestQueries.JSON_PATH_ALL_AUTHOR_NAMES, String.class);
		
		validateAuthorNames(authorNames);
	}
	
	@Test
	void test_query_all_posts() throws IOException {
		
		GraphQLResponse response = this.graphQLTestTemplate.postForString(TestQueries.QUERY_ALL_POSTS);
		List<String> titles = response.getList(TestQueries.JSON_PATH_ALL_POST_TITLES, String.class);

		validatePostTitles(titles);
	}
	
	@Test
	void test_create_post() throws IOException {
		
		GraphQLResponse response = this.graphQLTestTemplate.postForString(TestQueries.MUTATION_CREATE_POST);
		Post post= response.get(TestQueries.JSON_PATH_CREATE_POST, Post.class);

		validateNewPost(post);
	}
	
	@Test
	void test_subscription() {
		
	    final GraphQLResponse graphQLResponse =
	    	this.graphQLTestSubscription
            .init()
            .start("subscription-get-post.graphql")
            .awaitAndGetNextResponse(Duration.ofSeconds(5));
	    
	    Post post = graphQLResponse.get(TestQueries.JSON_PATH_RANDOM_POST, Post.class);
	    assertThat(post.getTitle()).isEqualTo(TestQueries.BOOK_1_TITLE);
	}
	
	public static void validateAuthorNames(List<String> authorNames) {
		assertThat(authorNames).contains(TestQueries.AUTHOR_1_NAME, TestQueries.AUTHOR_2_NAME, TestQueries.AUTHOR_3_NAME);
	}
	
	public static void validatePostTitles(List<String> titles) {
		assertThat(titles).contains(TestQueries.BOOK_1_TITLE, TestQueries.BOOK_2_TITLE, TestQueries.BOOK_3_TITLE);
	}

	public static void validateNewPost(Post post) {
		assertThat(post.getTitle()).isEqualTo(TestQueries.NEW_POST_TITLE);
		assertThat(post.getContent()).isEqualTo(TestQueries.NEW_POST_CONTENT);
		assertThat(post.getReleaseYear()).isEqualTo(Year.now().getValue());
	}
}
