package com.igorgm.library.controller;

import com.igorgm.library.entity.book.Book;
import com.igorgm.library.service.book.BookService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(SpringExtension.class)
class BookControllerTest {
	
	@InjectMocks
	private BookController bookController;
	
	@Mock
	private BookService bookServiceMock;
	
	private Book validBook;
	
	@BeforeEach
	void setUp() {
		validBook = Book.builder()
				.id(1)
				.title("The Hobbit")
				.author("J.R.R. Tolkien")
				.genre("Fantasy")
				.rating(5)
				.price(20.0)
				.numberOfRates(1)
				.build();
		
		PageImpl<Book> bookPage = new PageImpl<>(List.of(validBook));
		
		BDDMockito.when(bookServiceMock.listAll(any()))
				.thenReturn(bookPage);
		
		BDDMockito.when(bookServiceMock.findById(any(), anyInt()))
				.thenReturn(validBook);
		
		BDDMockito.when(bookServiceMock.findByGenre(anyString()))
				.thenReturn(List.of(validBook));
		
		BDDMockito.when(bookServiceMock.findByAuthor(anyString()))
				.thenReturn(List.of(validBook));
		
		BDDMockito.when(bookServiceMock.getRecentlyViewedBooks(any()))
				.thenReturn(List.of(validBook));
		
		BDDMockito.when(bookServiceMock.addRating(anyInt(), anyInt()))
				.thenReturn(validBook);
	}
	
	@Test
	void getAllBooks_ReturnsPaginatedListOfBooks_WhenSuccessful() {
		String expectedTitle = validBook.getTitle();
		
		Page<Book> bookPage = bookController.getAllBooks(null).getBody();
		
		Assertions.assertThat(bookPage).isNotNull();
		Assertions.assertThat(bookPage.toList())
				.isNotEmpty()
				.hasSize(1);
		Assertions.assertThat(bookPage.toList().getFirst().getTitle()).isEqualTo(expectedTitle);
	}
	
	@Test
	void getBookById_ReturnsBook_WhenSuccessful() {
		Integer expectedId = validBook.getId();
		
		Book book = bookController.getBookById(null, 1).getBody();
		
		Assertions.assertThat(book).isNotNull();
		Assertions.assertThat(book.getId()).isEqualTo(expectedId);
	}
	
	@Test
	void getAllBooksByGenre_ReturnsListOfBooksByGenre_WhenSuccessful() {
		String expectedGenre = validBook.getGenre();
		
		List<Book> books = bookController.getAllBooksByGenre("genre").getBody();
		
		Assertions.assertThat(books)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1);
		Assertions.assertThat(books.getFirst().getGenre()).isEqualTo(expectedGenre);
	}
	
	@Test
	void getAllBooksByAuthor_ReturnsListOfBooksByAuthor_WhenSuccessful() {
		String expectedAuthor = validBook.getAuthor();
		
		List<Book> books = bookController.getAllBooksByAuthor("author").getBody();
		
		Assertions.assertThat(books)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1);
		Assertions.assertThat(books.getFirst().getAuthor()).isEqualTo(expectedAuthor);
	}
	
	@Test
	void getRecentlyViewedBooks_ReturnsRecentlyViewedBooks_WhenSuccessful() {
		List<Book> books = bookController.getRecentlyViewedBooks(null).getBody();
		
		Assertions.assertThat(books)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1);
	}
	
	@Test
	void rateBook_UpdatesBookRating_WhenSuccessful() {
		Book book = bookController.rateBook(1, 5).getBody();
		
		Assertions.assertThat(book).isNotNull();
		Assertions.assertThat(book.getRating()).isEqualTo(5);
	}
}
