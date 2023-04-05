import { useQuery, useSubscription, gql } from '@apollo/client';

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
  const { loading, error, data } = useQuery(GET_AUTHORS);

  if (loading) return <p>Loading...</p>;
  if (error) return <p>Error : {error.message}</p>;

  return data.allAuthors.map(({ name, email }) => (
    <p key={email}>{name}: {email}</p>
  ));
}

function GetRandomPost() {
  const { loading, error, data } = useSubscription(SUB_POST);

  if (loading) return <p>Loading...</p>;
  if (error) return <p>Error : {error.message}</p>;

  return  <div>
            <p>{data.randomPost.author.name}:</p>
            <p>{data.randomPost.title}</p>
            <p>{data.randomPost.content}</p>
            <p>{data.randomPost.releaseYear}</p>
          </div>;
}

export default function App() {
  return (
    <div>
      <h2>Spring GraphQL Examples 🤓️</h2>
      <p>This client uses new graphql-ws npm library and <b>graphql-transport-ws</b> protocol for subscriptions</p>
      <br/>
      <h4>All Authors Query</h4>
      <GetAllAuthors />
      <br/>
      <h4>Random Post Subscription</h4>
      <GetRandomPost />
    </div>
  );
}