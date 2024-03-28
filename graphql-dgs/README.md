# Netflix Domain Graph Service (DGS) Framework

## URLs

* [Apollo client that uses graphql-transport-ws protocol](http://localhost:8080/apollo/index.html)
* [Integrated GraphiQL](http://localhost:8080/graphiql)

## Issues

* `@DgsData` methods (field resolvers) can't be used with `@Secured("ROLE_USER")` because authentication is not found when using WebSocket subscriptions. [GitHub Issue](https://github.com/Netflix/dgs-framework/issues/458)
* No support for Bearer token authentication for subscriptions [GitHub Issue](https://github.com/Netflix/dgs-framework/issues/450).
* No support for java records as inputs: [GitHub Issue](https://github.com/Netflix/dgs-framework/issues/1138)
* SSE and WebSocket subscriptions don't work at the same time.
* SSE don't work on Reactive stack.
* WebSocket subscriptions in integrated GraphiQL don't work.
* WebSocket subscriptions in both custom and integrated GraphiQL don't work well with security.

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

## Reactive

* Enable Reactive stack by commenting out `graphql-dgs-spring-boot-starter` and enabling `graphql-dgs-webflux-starter` dependency. Reactive security is not implemented so application has to be run with `no-security` profile.
* Subscription dependency `graphql-dgs-subscriptions-websockets-autoconfigure` is not required.
* SSE don't work (`graphql-dgs-subscriptions-sse-autoconfigure` dependency can't be enabled)
