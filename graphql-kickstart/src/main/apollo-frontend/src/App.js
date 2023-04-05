import gql from 'graphql-tag'
import { Subscription, Query } from "react-apollo"

const GET_AUTHORS = gql`
  query {
    allAuthors {
      name
      email
    }
  }
`;

const SUB_POST = gql`
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
`

function GetAllAuthors() {
  return  <Query query={GET_AUTHORS} >
            {({ loading, error, data }) => {
              if (loading) return <div>Loading...</div>
              if (error) return <div>Error! ${error.message}</div>

              return (
                data.allAuthors.map(author => (
                  <p key={author.email}>{author.name}: {author.email}</p>
                ))
              );
            }}
          </Query>
}

function GetRandomPost() {

  return  <Subscription subscription={SUB_POST}>
            {({ data, loading }) => {
              if (loading) return <div><p>Loading...</p></div>
              
              return  <div>
                        <p>{data.randomPost.author.name}:</p>
                        <p>{data.randomPost.title}</p>
                        <p>{data.randomPost.content}</p>
                        <p>{data.randomPost.releaseYear}</p>
                      </div>;
            }}
          </Subscription>
}

export default function App() {
  return (
    <div>
      <h2>Spring GraphQL Examples 🤓️</h2>
      <p>This client uses deprecated subscriptions-transport-ws npm library and <b>graphql-ws</b> protocol for subscriptions</p>
      <br/>
      <h4>All Authors Query</h4>
      <GetAllAuthors />
      <br/>
      <h4>Random Post Subscription</h4>
      <GetRandomPost />
    </div>
  );
}