# GraphQL support in Spring  ![build status](https://github.com/stojsavljevic/graphql-spring-examples/actions/workflows/maven.yml/badge.svg)

This project demonstrates 3 different Spring implementations of GraphQL protocol: [Netflix DGS](https://github.com/Netflix/dgs-framework), [Java Kickstart](https://github.com/graphql-java-kickstart/graphql-spring-boot) and [Spring for GraphQL](https://github.com/spring-projects/spring-graphql). Every implementation is in its own module and they all share the same GraphQL schema and fetch data using common class. Security configuration is also the same. Every module is covered with integration tests.

It also has two [Apollo React](https://www.apollographql.com/docs/react) clients that uses different WebSocket implementations.


## Modules

* `graphql-core` library with common code and resources
* `graphql-dgs` Netflix DGS module
* `graphql-kickstart` Java Kickstart module - DEPRECATED
* `graphql-spring` Spring for GraphQL module

## Security

* Every module uses form login: `admin/admin`.
* In order to disable security start components with `no-security` spring profile.
* WebSocket authentication can be done in two ways:
	- using session cookies when client is embedded in a page
	- using `authToken` in connection payload that simulates Bearer token authentication. This is demonstrated in Apollo React clients. Note that DGS implementation doesn't support it.

## Misc

* Every component starts on `8080` port by default.
* Every component exposes Apollo React client on <http://localhost:8080/apollo/index.html>. Note that DGS and Spring implementations shares the same Apollo client in `graphql-core` module. That client uses new WebSocket protocol. Kickstart has its own Apollo client that uses deprecated protocol.

## Example Queries

```
query {
  allPosts {
    title
    content
    releaseYear
    author {
      name
    }
  }
}
```

```
subscription {
  randomPost {
    title
    content
    releaseYear
    author {
      name
    }
  }
}

```

```
mutation {
  createPost(postInput: {
    title: "The Shining"
    content: "I believe that children are our future. Unless we stop them now."
    authorId: 1
  }) {
    title
    content
    releaseYear
    author {
      name
    }
  }
}
```