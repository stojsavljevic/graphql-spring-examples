package com.alex.graphql.core.model;

/**
 * DGS doesn't support java records as inputs.
 */
public class PostInput {

	String title;
	String content;
	String authorId;
	
	public PostInput() {}
	
	public PostInput(String title, String content, String authorId) {
		super();
		this.title = title;
		this.content = content;
		this.authorId = authorId;
	}

	public String title() {
		return title;
	}

	public String content() {
		return content;
	}

	public String authorId() {
		return authorId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
}
