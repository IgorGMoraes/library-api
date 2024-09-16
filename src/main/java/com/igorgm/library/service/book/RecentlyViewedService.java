package com.igorgm.library.service.book;

import com.igorgm.library.entity.book.Book;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecentlyViewedService {
	
	private static final String RECENTLY_VIEWED_KEY = "recently_viewed_books";
	private static final int MAX_RECENTLY_VIEWED = 10;
	
	public void addRecentlyViewed(HttpSession session, Book book) {
		List<Book> recentlyViewedBooks = getRecentlyViewedIds(session);
		
		recentlyViewedBooks.removeIf(b -> b.getId().equals(book.getId()));
		recentlyViewedBooks.addFirst(book);
		
		if (recentlyViewedBooks.size() > MAX_RECENTLY_VIEWED) {
			recentlyViewedBooks = recentlyViewedBooks.subList(0, 10);
		}
		
		session.setAttribute(RECENTLY_VIEWED_KEY, recentlyViewedBooks);
	}
	
	public List<Book> getRecentlyViewedIds(HttpSession session) {
		return session.getAttribute(RECENTLY_VIEWED_KEY) != null ?
				(List<Book>) session.getAttribute(RECENTLY_VIEWED_KEY) : new ArrayList<>();
	}
}
