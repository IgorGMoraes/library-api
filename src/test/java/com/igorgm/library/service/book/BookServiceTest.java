package com.igorgm.library.service.book;

import com.igorgm.library.entity.book.Book;
import com.igorgm.library.exception.ResourceNotFoundException;
import com.igorgm.library.repository.book.BookRepository;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
	
	@InjectMocks
	private BookService bookService;
	
	@Mock
	private RecentlyViewedService recentlyViewedService;
	
	@Mock
	private BookRepository bookRepository;
	
	@Mock
	private HttpSession session;
	
	private Book validBook;
	
	@BeforeEach
	void setUp() {
		validBook = Book.builder()
				.id(1)
				.title("Test Book")
				.author("Test Author")
				.genre("Fiction")
				.rating(1)
				.numberOfRates(1)
				.build();
	}
	
	@Test
	void listAll_ReturnsPageOfBooks_WhenSuccessful() {
		Pageable pageable = Pageable.ofSize(10);
		Page<Book> expectedPage = new PageImpl<>(List.of(validBook), pageable, 1);
		
		when(bookRepository.findAll(pageable)).thenReturn(expectedPage);
		
		Page<Book> result = bookService.listAll(pageable);
		
		assertThat(result).isNotNull();
		assertThat(result.getContent()).isNotEmpty().hasSize(1);
		assertThat(result.getContent().getFirst()).isEqualTo(validBook);
	}
	
	@Test
	void findById_ReturnsBook_WhenSuccessful() {
		when(bookRepository.findById(validBook.getId())).thenReturn(Optional.of(validBook));
		
		Book result = bookService.findById(session, validBook.getId());
		
		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(validBook.getId());
		verify(recentlyViewedService, times(1)).addRecentlyViewed(session, validBook);
	}
	
	@Test
	void findById_ThrowsResourceNotFoundException_WhenBookDoesNotExist() {
		when(bookRepository.findById(validBook.getId())).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> bookService.findById(session, validBook.getId()))
				.isInstanceOf(ResourceNotFoundException.class);
	}
	
	@Test
	void findByGenre_ReturnsListOfBooks_WhenSuccessful() {
		String genre = "Fiction";
		when(bookRepository.findByGenre(genre)).thenReturn(List.of(validBook));
		
		List<Book> result = bookService.findByGenre(genre);
		
		assertThat(result).isNotNull().isNotEmpty().hasSize(1);
		assertThat(result.getFirst()).isEqualTo(validBook);
	}
	
	@Test
	void findByAuthor_ReturnsListOfBooks_WhenSuccessful() {
		String author = "Test Author";
		when(bookRepository.findByAuthor(author)).thenReturn(List.of(validBook));
		
		List<Book> result = bookService.findByAuthor(author);
		
		assertThat(result).isNotNull().isNotEmpty().hasSize(1);
		assertThat(result.getFirst()).isEqualTo(validBook);
	}
	
	@Test
	void addRating_ReturnsUpdatedBook_WhenSuccessful() {
		int newRating = 5;
		when(bookRepository.findById(validBook.getId())).thenReturn(Optional.of(validBook));
		when(bookRepository.save(validBook)).thenReturn(validBook);
		
		Book result = bookService.addRating(validBook.getId(), newRating);
		
		assertThat(result).isNotNull();
		assertThat(result.getRating()).isEqualTo(3);
		assertThat(result.getNumberOfRates()).isEqualTo(2);
	}
	
	@Test
	void addRating_ThrowsResourceNotFoundException_WhenBookDoesNotExist() {
		when(bookRepository.findById(validBook.getId())).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> bookService.addRating(validBook.getId(), 5))
				.isInstanceOf(ResourceNotFoundException.class);
	}
}
