package com.igorgm.library.controller;

import com.igorgm.library.entity.book.Book;
import com.igorgm.library.service.book.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("books")
@RequiredArgsConstructor
@Validated
public class BookController {
	
	private final BookService bookService;
	
	@GetMapping
	@Operation(summary = "List all books paginated",
			description = "The default size is 20 books, use the parameter size to change the value",
			tags = {"books"}
	)
	public ResponseEntity<Page<Book>> getAllBooks(@Parameter Pageable pageable) {
		return ResponseEntity.ok(bookService.listAll(pageable));
	}
	
	@GetMapping("/{id}")
	@Operation(summary = "Get a book by its id")
	public ResponseEntity<Book> getBookById(HttpSession session, @PathVariable Integer id) {
		return ResponseEntity.ok(bookService.findById(session, id));
	}
	
	@GetMapping("/genre/{genre}")
	@Operation(summary = "List all books by the giving genre")
	public ResponseEntity<List<Book>> getAllBooksByGenre(@PathVariable String genre) {
		return ResponseEntity.ok(bookService.findByGenre(genre));
	}
	
	@GetMapping("/author/{author}")
	@Operation(summary = "List all books by the giving author")
	public ResponseEntity<List<Book>> getAllBooksByAuthor(@PathVariable String author) {
		return ResponseEntity.ok(bookService.findByAuthor(author));
	}
	
	@GetMapping("/recent")
	@Operation(summary = "List the last 10 accessed books")
	public ResponseEntity<List<Book>> getRecentlyViewedBooks(HttpSession session) {
		return ResponseEntity.ok(bookService.getRecentlyViewedBooks(session));
	}
	
	@CacheEvict(value = "books", key = "#id")
	@PutMapping("/{id}/rate")
	@Operation(summary = "Let the user to add rating to a book")
	public ResponseEntity<Book> rateBook(@PathVariable Integer id, @RequestParam @Min(1) @Max(5) int rate) {
		return ResponseEntity.ok(bookService.addRating(id, rate));
	}
	
}
