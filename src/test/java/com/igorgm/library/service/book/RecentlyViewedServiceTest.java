package com.igorgm.library.service.book;

import com.igorgm.library.entity.book.Book;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RecentlyViewedServiceTest {
	
	private RecentlyViewedService recentlyViewedService;
	private HttpSession session;
	
	@BeforeEach
	void setUp() {
		recentlyViewedService = new RecentlyViewedService();
		session = Mockito.mock(HttpSession.class);
	}
	
	@Test
	void addRecentlyViewed_AddsBookToRecentlyViewedList_WhenBookIsNew() {
		// Given
		Book book = new Book();
		book.setId(1);
		List<Book> recentlyViewedBooks = new ArrayList<>();
		when(session.getAttribute("recently_viewed_books")).thenReturn(recentlyViewedBooks);
		
		// When
		recentlyViewedService.addRecentlyViewed(session, book);
		
		// Then
		verify(session).setAttribute("recently_viewed_books", recentlyViewedBooks);
		assertThat(recentlyViewedBooks).containsExactly(book);
	}
	
	@Test
	void addRecentlyViewed_RemovesDuplicateBook_WhenBookIsAlreadyInList() {
		// Given
		Book book1 = new Book();
		book1.setId(1);
		Book book2 = new Book();
		book2.setId(2);
		List<Book> recentlyViewedBooks = new ArrayList<>();
		recentlyViewedBooks.add(book1);
		when(session.getAttribute("recently_viewed_books")).thenReturn(recentlyViewedBooks);
		
		// When
		recentlyViewedService.addRecentlyViewed(session, book1);
		
		// Then
		verify(session).setAttribute("recently_viewed_books", recentlyViewedBooks);
		assertThat(recentlyViewedBooks).containsExactly(book1);
	}
	
	@Test
	void getRecentlyViewedIds_ReturnsEmptyList_WhenNoRecentlyViewedBooks() {
		// Given
		when(session.getAttribute("recently_viewed_books")).thenReturn(null);
		
		// When
		List<Book> result = recentlyViewedService.getRecentlyViewedIds(session);
		
		// Then
		assertThat(result).isEmpty();
	}
	
	@Test
	void getRecentlyViewedIds_ReturnsListOfRecentlyViewedBooks() {
		// Given
		Book book = new Book();
		book.setId(1);
		List<Book> recentlyViewedBooks = new ArrayList<>();
		recentlyViewedBooks.add(book);
		when(session.getAttribute("recently_viewed_books")).thenReturn(recentlyViewedBooks);
		
		// When
		List<Book> result = recentlyViewedService.getRecentlyViewedIds(session);
		
		// Then
		assertThat(result).containsExactly(book);
	}
}
