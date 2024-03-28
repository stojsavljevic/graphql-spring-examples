package com.alex.graphql.dgs;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Year;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import com.alex.graphql.core.model.Post;
import com.alex.graphql.core.util.TestQueries;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.graphql.dgs.DgsQueryExecutor;

import graphql.ExecutionResult;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("no-security")
class GraphqlDgsApplicationTests {
	
	@Autowired
	DgsQueryExecutor dgsQueryExecutor;

	ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void test_query_all_authors() {
		List<String> authorNames = this.dgsQueryExecutor
				.executeAndExtractJsonPath(TestQueries.QUERY_ALL_AUTHORS, TestQueries.JSON_PATH_ALL_AUTHOR_NAMES);

		validateAuthorNames(authorNames);
	}
	
	@Test
	void test_query_all_posts() {
		List<String> titles = this.dgsQueryExecutor
				.executeAndExtractJsonPath(TestQueries.QUERY_ALL_POSTS, TestQueries.JSON_PATH_ALL_POST_TITLES);

		validatePostTitles(titles);
	}
	
	@Test
	void test_create_post() {
		Post post = this.dgsQueryExecutor
				.executeAndExtractJsonPathAsObject(TestQueries.MUTATION_CREATE_POST, TestQueries.JSON_PATH_CREATE_POST, Post.class);

		validateNewPost(post);
	}
	
	@Test
	void test_subscription() {
		ExecutionResult executionResult = this.dgsQueryExecutor.execute(TestQueries.SUBSCRIPTION_GET_RANDOM_POST);
		Publisher<ExecutionResult> publisher = executionResult.getData();
		
		StepVerifier.create(publisher)
	        .assertNext(result -> assertThat(toPost(result).title()).isEqualTo(TestQueries.BOOK_1_TITLE))
	        .assertNext(result -> assertThat(toPost(result).title()).isEqualTo(TestQueries.BOOK_2_TITLE))
	        .assertNext(result -> assertThat(toPost(result).title()).isEqualTo(TestQueries.BOOK_3_TITLE)).thenCancel()
	    .verify();
	}

	@SuppressWarnings("unchecked")
	private Post toPost(ExecutionResult result) {
		Map<String, Object> data = (Map<String, Object>) result.getData();
		return this.objectMapper.convertValue(data.get("randomPost"), Post.class);
	}
	
	public static void validateAuthorNames(List<String> authorNames) {
		assertThat(authorNames).contains(TestQueries.AUTHOR_1_NAME, TestQueries.AUTHOR_2_NAME, TestQueries.AUTHOR_3_NAME);
	}
	
	public static void validatePostTitles(List<String> titles) {
		assertThat(titles).contains(TestQueries.BOOK_1_TITLE, TestQueries.BOOK_2_TITLE, TestQueries.BOOK_3_TITLE);
	}

	public static void validateNewPost(Post post) {
		assertThat(post.title()).isEqualTo(TestQueries.NEW_POST_TITLE);
		assertThat(post.content()).isEqualTo(TestQueries.NEW_POST_CONTENT);
		assertThat(post.releaseYear()).isEqualTo(Year.now().getValue());
	}
}
