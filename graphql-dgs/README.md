# Netflix Domain Graph Service (DGS) Framework

## URLs

* [Apollo client that uses graphql-transport-ws protocol](http://localhost:8080/apollo/index.html)
* [Integrated GraphiQL](http://localhost:8080/graphiql)

## Issues

* `@DgsData` methods (field resolvers) can't be used with `@Secured("ROLE_USER")` because authentication is not found when using WebSocket subscriptions. [GitHub Issue](https://github.com/Netflix/dgs-framework/issues/458)
* No support for Bearer token authentication for subscriptions [GitHub Issue](https://github.com/Netflix/dgs-framework/issues/450).
* WebSocket subscriptions in embedded GraphiQL don't work.

## SSE Subscriptions

* Change subscription related dependencies in `pom.xml`.
* Use Postman or `curl`. Example `curl` request:

```
curl --location --request POST 'http://localhost:8080/subscriptions' \
--header 'Accept: application/json' \
--header 'Accept: text/event-stream' \
--header 'Cookie: JSESSIONID=F022D2476711DD88FA5910D822E1D876' \
--header 'Content-Type: application/json' \
--data-raw '{"query":"subscription {\n  randomPost {\n    title\n    content\n    releaseYear\n    author {\n      name\n    }\n  }\n}","variables":{}}'
```