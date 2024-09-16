package com.igorgm.library.service.book;

import com.igorgm.library.entity.book.Book;
import com.igorgm.library.exception.ResourceNotFoundException;
import com.igorgm.library.repository.book.BookRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
	
	private final RecentlyViewedService recentlyViewedService;
	private final BookRepository bookRepository;
	
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
	
}
