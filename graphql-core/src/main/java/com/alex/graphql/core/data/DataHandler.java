package com.alex.graphql.core.data;

import java.time.Duration;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.alex.graphql.core.model.Author;
import com.alex.graphql.core.model.Post;
import com.alex.graphql.core.model.PostInput;

import reactor.core.publisher.Flux;

@Component
public class DataHandler {
	
	ArrayList<Post> posts = new ArrayList<>(Arrays.asList(
			new Post("1", "Pet Sematary", "Television! Teacher, mother, secret lover.", 1983, "1"),
            new Post("2", "Animal Farm", "Even communism works… in theory.", 1945, "2"),
            new Post("3", "The Brothers Karamazov", "Don’t eat me. I have a wife and kids. Eat them.", 1880, "3"),
            new Post("4", "The Trial", "Trying is the first step towards failure", 1925, "4"),
            new Post("5", "Murder on the Orient Express", "To alcohol! The cause of, and solution to, all of life’s problems.", 1934, "5")
		)
	);
    
	ArrayList<Author> authors = new ArrayList<>(Arrays.asList(
            new Author("1", "Stephen King", "king@gmail.com"),
            new Author("2", "George Orwell", "orwell@gmail.com"),
            new Author("3", "Fyodor Dostoevsky", "dostoevsky@gmail.com"),
            new Author("4", "Franz Kafka", "kafka@gmail.com"),
            new Author("5", "Agatha Christie", "christie@gmail.com")
        )
    );

	int nextPostId = -1;

	public List<Post> getAllPosts() {
		return this.posts;
	}
	
	public Post createPost(PostInput postInput) {
		if(getAuthorById(postInput.authorId()) == null) {
			throw new RuntimeException("Author doesn't exist");
		}
		
		Post post = new Post(String.valueOf(this.posts.size()+1), postInput.title(), postInput.content(), Year.now().getValue(), postInput.authorId());
		this.posts.add(post);
		return post;
	}
	
	public List<Author> getAllAuthors() {
		return this.authors;
	}
	
	Post getNextPost() {
		if (nextPostId < posts.size()-1)
			nextPostId++;
		else
			nextPostId = 0;
		return this.posts.get(nextPostId);
	}
	
	public Flux<Post> getRandomPostPublisher() {
		return Flux.interval(Duration.ofSeconds(0), Duration.ofSeconds(2))
				.map(t -> getNextPost())
				.delayElements(Duration.ofSeconds(1));
	}

	public Author getAuthorById(String authorId) {
		Optional<Author> author = this.authors.stream().filter(a -> a.id().equals(authorId)).findAny();
		return author.get();
	}

}

