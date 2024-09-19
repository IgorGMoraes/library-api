package com.igorgm.library.service.book;

import com.igorgm.library.entity.book.Book;
import com.igorgm.library.entity.book.OpenLibraryBookDTO;
import com.igorgm.library.exception.ResourceNotFoundException;
import com.igorgm.library.repository.book.BookRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
	
	private final RecentlyViewedService recentlyViewedService;
	private final BookRepository bookRepository;
	private final OpenLibraryService openLibraryService;
	
	@Cacheable(value = "books")
	public Page<Book> listAll(Pageable pageable) {
		return bookRepository.findAll(pageable);
	}
	
	@Cacheable(value = "books", key = "#id")
	public Book findById(HttpSession session, Integer id) {
		Book book = bookRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
		
		recentlyViewedService.addRecentlyViewed(session, book);
		
		return book;
	}
	
	@Cacheable(value = "booksByGenre")
	public List<Book> findByGenre(String genre) {
		return bookRepository.findByGenre(genre);
	}
	
	@Cacheable(value = "booksByAuthor")
	public List<Book> findByAuthor(String author) {
		return bookRepository.findByAuthor(author);
	}
	
	public List<Book> getRecentlyViewedBooks(HttpSession session) {
		return recentlyViewedService.getRecentlyViewedIds(session);
	}
	
	public Book addRating(Integer bookId, int rate) {
		
		Book book = bookRepository.findById(bookId)
				.orElseThrow(ResourceNotFoundException::new);
		
		int currentNumberOfRates = book.getNumberOfRates();
		
		double newAverageRating = ((book.getRating() * currentNumberOfRates) + rate) / (currentNumberOfRates + 1);
		
		book.setRating(newAverageRating);
		book.setNumberOfRates(currentNumberOfRates + 1);
		
		return bookRepository.save(book);
	}
	
	public List<Book> searchByTitle(String title) {
		var books = bookRepository.findByTitleContainingIgnoreCase(title);
		
		if (books.isEmpty()) {
			var openLibraryBooks = openLibraryService.searchBooksByTitle(title);
			return openLibraryBooks.stream()
					.map(this::mapToBook)
					.collect(Collectors.toList());
		}
		
		return books;
	}
	
	private Book mapToBook(OpenLibraryBookDTO openLibraryBookDTO) {
		return Book.builder()
				.title(openLibraryBookDTO.getTitle())
				.author(String.join(", ", Optional.ofNullable(openLibraryBookDTO.getAuthor_name()).orElse(Collections.emptyList())))
				.rating(openLibraryBookDTO.getRatings_average())
				.numberOfRates(openLibraryBookDTO.getRatings_count())
				.build();
	}
	
}
