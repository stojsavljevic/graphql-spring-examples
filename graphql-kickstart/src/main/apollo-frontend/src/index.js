import React from 'react';
import * as ReactDOM from 'react-dom/client';
import App from './App';
import { ApolloProvider } from "react-apollo"
import { split } from 'apollo-link';
import { HttpLink } from 'apollo-link-http';
import { WebSocketLink } from 'apollo-link-ws';
import { getMainDefinition } from 'apollo-utilities';
import { ApolloClient } from 'apollo-client'
import { InMemoryCache } from 'apollo-cache-inmemory'


// Create an http link:
const httpLink = new HttpLink({
  uri: 'http://localhost:8080/graphql'
})

// Create an ws link
// set authToken in connection payload to simulate bearer token authentication
const wsLink = new WebSocketLink({
  uri: `ws://localhost:8080/subscriptions`,
  options: {
    reconnect: true,
    connectionParams: {
      authToken: 'admin',
    }
  }
});

// using the ability to split links, you can send data to each link
// depending on what kind of operation is being sent
const link = split(
    // split based on operation type
    ({ query }) => {
      const { kind, operation } = getMainDefinition(query);
      return kind === 'OperationDefinition' && operation === 'subscription';
    },
    wsLink,
    httpLink,
)

const client = new ApolloClient({
  link,
  cache: new InMemoryCache()
})

// Supported in React 18+
const root = ReactDOM.createRoot(document.getElementById('root'));

root.render(
  <ApolloProvider client={client}>
    <App />
  </ApolloProvider>,
);

