package com.igorgm.library.service.book;

import com.igorgm.library.entity.book.BookCsv;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Service
public class CsvParserService {
	
	public List<BookCsv> parseCsv() {
		InputStream inputStream = getClass().getResourceAsStream("/data/books.csv");
		assert inputStream != null;
		Reader reader = new InputStreamReader(inputStream);
		
		var csvToBean = new CsvToBeanBuilder<BookCsv>(reader)
				.withType(BookCsv.class)
				.withIgnoreLeadingWhiteSpace(true)
				.build();
		
		return csvToBean.parse();
	}
}
