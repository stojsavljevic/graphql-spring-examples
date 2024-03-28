package com.alex.graphql.core.model;

public record Post (String id, String title, String content, Integer releaseYear, String authorId) {}