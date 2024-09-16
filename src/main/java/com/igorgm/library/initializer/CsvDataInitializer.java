package com.igorgm.library.initializer;

import com.igorgm.library.entity.book.Book;
import com.igorgm.library.entity.book.BookCsv;
import com.igorgm.library.repository.book.BookRepository;
import com.igorgm.library.service.book.CsvParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CsvDataInitializer implements CommandLineRunner {
	
	private final BookRepository bookRepository;
	private final CsvParserService csvParserService;
	
	@Override
	public void run(String... args) throws Exception {
		List<BookCsv> booksFromCsv = csvParserService.parseCsv();
		saveBooksIfNotExist(booksFromCsv);
	}
	
	private void saveBooksIfNotExist(List<BookCsv> booksFromCsv) {
		Set<String> existingTitles = new HashSet<>(bookRepository.findAllTitles());
		
		List<Book> booksToSave = booksFromCsv.stream()
				.filter(bookCsv -> !existingTitles.contains(bookCsv.getTitle()))
				.map(this::convertToBookEntity)
				.collect(Collectors.toList());
		
		if (!booksToSave.isEmpty()) {
			bookRepository.saveAll(booksToSave);
		}
	}
	
	private Book convertToBookEntity(BookCsv bookCsv) {
		return Book.builder()
				.title(bookCsv.getTitle())
				.author(bookCsv.getAuthor())
				.genre(bookCsv.getGenre())
				.price(cleanPriceUnit(bookCsv.getPrice()))
				.rating(bookCsv.getRating())
				.numberOfRates(parsePeopleRated(bookCsv.getNumOfRates()))
				.build();
	}
	
	private double cleanPriceUnit(String priceString) {
		String cleanPrice = priceString.replaceAll("[^\\d.]", "");
		return Double.parseDouble(cleanPrice);
	}
	
	private int parsePeopleRated(double peopleRated) {
		return  (int) peopleRated;
	}
}