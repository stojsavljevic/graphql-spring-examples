# GraphQL Java Kickstart


**NOTE:** <mark>The GraphQL Java Kickstart project is archived and no longer maintained in favor of Spring for GraphQL.</mark>


## URLs

* [Apollo client that uses deprecated graphql-ws protocol](http://localhost:8080/apollo/index.html)
* [Custom GraphiQL](http://localhost:8080/custom-graphiql.html)
* [Integrated GraphiQL](http://localhost:8080/graphiql)
* [Integrated Playground](http://localhost:8080/playground)

## Issues

* Field resolvers methods can't be used with `@Secured("ROLE_USER")` because authentication is not found when using WebSocket subscriptions
	- Kickstart 14.1.0 with SB 2.7.8 handles it properly but needs additional configuration that is commented out in `SecurityConfig.java`. The same configuration doesn't work here - authentication is not found at all for subscriptions.
* WebSocket subscriptions in Apollo v3 client from `graphql-core` don't work.
	- `graphql-transport-ws` protocol not supported: [GitHub Issue](https://github.com/graphql-java-kickstart/graphql-java-servlet/issues/455).
	- It has it's own Apollo client. The source is in `/src/main/apollo-frontend` and it uses deprecated `subscriptions-transport-ws` library (`graphql-ws` protocol).
* Tests don't support sse subscriptions
* Tests don't work on Reactive stack.
* SSE don't work on Reactive stack.
* WebSocket subscriptions in integrated GraphiQL don't work.
* WebSocket subscriptions in both custom and integrated GraphiQL don't work well with security.

## SSE Subscriptions

* Use Postman or `curl`. Example `curl` request:

```
curl --location --request POST 'http://localhost:8080/graphql' \
--header 'Accept: application/json' \
--header 'Accept: text/event-stream' \
--header 'Cookie: JSESSIONID=C60A92B851B60DEDF8510395A3BDE7F5' \
--header 'Content-Type: application/json' \
--data-raw '{"query":"subscription {\n  randomPost {\n    title\n    content\n    releaseYear\n    author {\n      name\n    }\n  }\n}","variables":{}}'
```

## Reactive

* Enable Reactive stack by commenting out `spring-boot-starter-web` dependency. Reactive security is not implemented so application has to be run with `no-security` profile.
* Tests don't work because: `Caused by: java.lang.ClassNotFoundException: jakarta.websocket.Endpoint`

