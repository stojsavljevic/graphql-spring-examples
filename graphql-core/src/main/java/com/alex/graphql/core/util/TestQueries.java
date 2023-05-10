package com.alex.graphql.core.util;

public class TestQueries {

	public static final String QUERY_ALL_AUTHORS = "query {allAuthors { name email }}";
	
	public static final String QUERY_ALL_POSTS = "query {allPosts { title releaseYear author { name }}}";
	
	public static final String NEW_POST_TITLE = "The Shining";
	
	public static final String NEW_POST_CONTENT = "I believe that children are our future. Unless we stop them now.";
	
	public static final String MUTATION_CREATE_POST = "mutation {\n"
			+ "  createPost(postInput: {\n"
			+ "    title: \"" + NEW_POST_TITLE + "\"\n"
			+ "    content: \"" + NEW_POST_CONTENT +  "\"\n"
			+ "    authorId: \"1\"\n"
			+ "  }) {\n"
			+ "    title\n"
			+ "    content\n"
			+ "    releaseYear\n"
			+ "    author {\n"
			+ "      name\n"
			+ "    }\n"
			+ "  }\n"
			+ "}";
	
	public static final String SUBSCRIPTION_GET_RANDOM_POST = "subscription Posts { randomPost { title, releaseYear } }"; 
	
	public static final String JSON_PATH_ALL_AUTHOR_NAMES = "data.allAuthors[*].name";
	
	public static final String JSON_PATH_ALL_POST_TITLES = "data.allPosts[*].title";
	
	public static final String JSON_PATH_CREATE_POST = "data.createPost";
	
	public static final String JSON_PATH_RANDOM_POST = "data.randomPost";
	
	public static final String BOOK_1_TITLE = "Pet Sematary";
	
	public static final String BOOK_2_TITLE = "Animal Farm";
	
	public static final String BOOK_3_TITLE = "The Brothers Karamazov";
	
	public static final String AUTHOR_1_NAME = "Stephen King";
	
	public static final String AUTHOR_2_NAME = "George Orwell";
	
	public static final String AUTHOR_3_NAME = "Fyodor Dostoevsky";
	
	
}
