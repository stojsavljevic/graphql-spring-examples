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
import org.springframework.graphql.test.tester.GraphQlTester.Response;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.graphql.test.tester.WebSocketGraphQlTester;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.Builder;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;

import com.alex.graphql.core.model.Post;
import com.alex.graphql.core.util.TestQueries;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("no-security")
class GraphqlSpringApplicationTest {


	@Value("http://localhost:${local.server.port}${spring.graphql.websocket.path}")
	String baseWebsocketPath;
	
	@Value("http://localhost:${local.server.port}${spring.graphql.path:/graphql}")
	String baseHttpPath;
	
	WebSocketGraphQlTester webSocketGraphQlTester;
	
	HttpGraphQlTester httpGraphQlTester;

	// testing with websocket and http clients:
	@BeforeAll
	void setUp() {
		this.webSocketGraphQlTester = WebSocketGraphQlTester
				.builder(URI.create(this.baseWebsocketPath), new ReactorNettyWebSocketClient())
				.build();
		
		Builder httpBuilder = WebTestClient.bindToServer()
                .baseUrl(this.baseHttpPath)
                .defaultHeader("Accept", "text/event-stream");
		this.httpGraphQlTester = HttpGraphQlTester.builder(httpBuilder).build();
	}

	@Test
	void test_ws_query_all_authors() throws Exception {
		
		Response response = this.webSocketGraphQlTester.document(TestQueries.QUERY_ALL_AUTHORS).execute();
		List<String> authorNames = response.path(TestQueries.JSON_PATH_ALL_AUTHOR_NAMES).entityList(String.class).get();

		validateAuthorNames(authorNames);
	}
	
	@Test
	void test_http_query_all_authors() throws Exception {
		
		Response response = this.httpGraphQlTester.document(TestQueries.QUERY_ALL_AUTHORS).execute();
		List<String> authorNames = response.path(TestQueries.JSON_PATH_ALL_AUTHOR_NAMES).entityList(String.class).get();

		validateAuthorNames(authorNames);
	}

	@Test
	void test_ws_query_all_posts() throws Exception {

		Response response = this.webSocketGraphQlTester.document(TestQueries.QUERY_ALL_POSTS).execute();
		List<String> titles = response.path(TestQueries.JSON_PATH_ALL_POST_TITLES).entityList(String.class).get();

		validatePostTitles(titles);
	}
	
	@Test
	void test_http_query_all_posts() throws Exception {

		Response response = this.httpGraphQlTester.document(TestQueries.QUERY_ALL_POSTS).execute();
		List<String> titles = response.path(TestQueries.JSON_PATH_ALL_POST_TITLES).entityList(String.class).get();

		validatePostTitles(titles);
	}

	@Test
	void test_ws_create_post() {

		Response response = this.webSocketGraphQlTester.document(TestQueries.MUTATION_CREATE_POST).execute();
		Post post = response.path(TestQueries.JSON_PATH_CREATE_POST).entity(Post.class).get();
		
		validateNewPost(post);
	}
	
	@Test
	void test_http_create_post() {

		Response response = this.httpGraphQlTester.document(TestQueries.MUTATION_CREATE_POST).execute();
		Post post = response.path(TestQueries.JSON_PATH_CREATE_POST).entity(Post.class).get();
		
		validateNewPost(post);
	}

	@Test
	void test_ws_subscriptions() throws Exception {
		
		Flux<Post> subscriptionPost = this.webSocketGraphQlTester.document(TestQueries.SUBSCRIPTION_GET_RANDOM_POST).executeSubscription()
				.toFlux("randomPost", Post.class);

		StepVerifier.create(subscriptionPost)
	        .assertNext(result -> assertThat(result.title()).isEqualTo(TestQueries.BOOK_1_TITLE))
	        .assertNext(result -> assertThat(result.title()).isEqualTo(TestQueries.BOOK_2_TITLE))
	        .assertNext(result -> assertThat(result.title()).isEqualTo(TestQueries.BOOK_3_TITLE)).thenCancel()
        .verify();
	}
	
	// Testing subscriptions over http (sse) is still not supported
//	 @Test
	void test_http_subscriptions() throws Exception {

		Post post = this.httpGraphQlTester.document(TestQueries.SUBSCRIPTION_GET_RANDOM_POST).executeSubscription()
				.toFlux("randomPost", Post.class).blockFirst();
		assertThat(post.title()).isEqualTo(TestQueries.BOOK_1_TITLE);
	}
	
	void validateAuthorNames(List<String> authorNames) {
		assertThat(authorNames).contains(TestQueries.AUTHOR_1_NAME, TestQueries.AUTHOR_2_NAME, TestQueries.AUTHOR_3_NAME);
	}
	
	void validatePostTitles(List<String> titles) {
		assertThat(titles).contains(TestQueries.BOOK_1_TITLE, TestQueries.BOOK_2_TITLE, TestQueries.BOOK_3_TITLE);
	}

	void validateNewPost(Post post) {
		assertThat(post.title()).isEqualTo(TestQueries.NEW_POST_TITLE);
		assertThat(post.content()).isEqualTo(TestQueries.NEW_POST_CONTENT);
		assertThat(post.releaseYear()).isEqualTo(Year.now().getValue());
	}
}