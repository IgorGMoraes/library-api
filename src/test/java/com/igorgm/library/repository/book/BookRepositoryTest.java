package com.igorgm.library.repository.book;

import com.igorgm.library.entity.book.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BookRepositoryTest {
	
	@Autowired
	private BookRepository bookRepository;
	
	@BeforeEach
	void setUp() {
		bookRepository.saveAll(List.of(
				Book.builder()
						.title("The Hobbit")
						.author("J.R.R. Tolkien")
						.genre("Teen & Young Adult")
						.price(9.99)
						.rating(4.8)
						.numberOfRates(15000)
						.build(),
				
				Book.builder()
						.title("The Lord of the Rings")
						.author("J.R.R. Tolkien")
						.genre("Teen & Young Adult")
						.price(19.99)
						.rating(4.9)
						.numberOfRates(25000)
						.build(),
				
				Book.builder()
						.title("The Catcher in the Rye")
						.author("J.D. Salinger")
						.genre("Literature & Fiction")
						.price(8.99)
						.rating(4.2)
						.numberOfRates(18000)
						.build(),
				
				Book.builder()
						.title("1984")
						.author("George Orwell")
						.genre("Society & Social Sciences")
						.price(10.99)
						.rating(4.7)
						.numberOfRates(20000)
						.build()
		));
	}
	
	@Test
	void findByGenre_ReturnsBooks_WhenGenreMatches() {
		List<Book> books = bookRepository.findByGenre("Teen & Young Adult");
		assertThat(books).hasSize(2);
		
		assertThat(books).extracting(Book::getTitle)
				.containsExactlyInAnyOrder("The Hobbit", "The Lord of the Rings");
	}
	
	@Test
	void findByAuthor_ReturnsBooks_WhenAuthorMatches() {
		List<Book> books = bookRepository.findByAuthor("J.R.R. Tolkien");
		assertThat(books).hasSize(2);
		
		assertThat(books).extracting(Book::getTitle)
				.containsExactlyInAnyOrder("The Hobbit", "The Lord of the Rings");
	}
	
	@Test
	void findAllTitles_ReturnsAllBookTitles_WhenBooksExist() {
		List<String> titles = bookRepository.findAllTitles();
		assertThat(titles).hasSize(4);
		
		assertThat(titles).containsExactlyInAnyOrder(
				"The Hobbit", "The Lord of the Rings", "The Catcher in the Rye", "1984"
		);
	}
}
