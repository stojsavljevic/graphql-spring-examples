package com.alex.graphql.spring;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.time.Year;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.graphql.test.tester.GraphQlTester.Response;
import org.springframework.graphql.test.tester.WebSocketGraphQlTester;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;

import com.alex.graphql.core.model.Post;
import com.alex.graphql.core.util.TestQueries;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("no-security")
public class GraphqlSpringApplicationTest {


	@Value("http://localhost:${local.server.port}${spring.graphql.websocket.path}")
	String baseUrl;
	
	GraphQlTester graphQlTester;

	// testing with reactive client over websocket:
	@BeforeAll
	void setUp() {
		this.graphQlTester = WebSocketGraphQlTester.builder(URI.create(this.baseUrl), new ReactorNettyWebSocketClient())
				.build();
	}

	@Test
	void test_query_all_authors() throws Exception {

		Response response = this.graphQlTester.document(TestQueries.QUERY_ALL_AUTHORS).execute();
		List<String> authorNames = response.path(TestQueries.JSON_PATH_ALL_AUTHOR_NAMES).entityList(String.class).get();

		validateAuthorNames(authorNames);
	}

	@Test
	void test_query_all_posts() throws Exception {

		Response response = this.graphQlTester.document(TestQueries.QUERY_ALL_POSTS).execute();
		List<String> titles = response.path(TestQueries.JSON_PATH_ALL_POST_TITLES).entityList(String.class).get();

		validatePostTitles(titles);
	}

	@Test
	void test_create_post() {

		Response response = this.graphQlTester.document(TestQueries.MUTATION_CREATE_POST).execute();
		Post post = response.path(TestQueries.JSON_PATH_CREATE_POST).entity(Post.class).get();
		
		validateNewPost(post);
	}

	@Test
	void test_subscriptions() throws Exception {

		Post post = this.graphQlTester.document(TestQueries.SUBSCRIPTION_GET_RANDOM_POST).executeSubscription()
				.toFlux("randomPost", Post.class).blockFirst();

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