package com.alex.graphql.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Post {

	String id;
	String title;
	String content;
	Integer releaseYear;
	String authorId;

}