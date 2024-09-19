package com.igorgm.library.repository.book;

import com.igorgm.library.entity.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

	List<Book> findByGenre(String genre);
	
	List<Book> findByAuthor(String sauthor);
	
	@Query("select b.title from book b")
	List<String> findAllTitles();
	
	List<Book> findByTitleContainingIgnoreCase(String title);
}
