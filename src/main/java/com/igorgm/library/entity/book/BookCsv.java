package com.igorgm.library.entity.book;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookCsv {
	
	@CsvBindByName(column = "Title")
	private String title;
	
	@CsvBindByName(column = "Author")
	private String author;
	
	@CsvBindByName(column = "Main Genre")
	private String genre;
	
	@CsvBindByName(column = "Price")
	private String price;
	
	@CsvBindByName(column = "Rating")
	private double rating;
	
	@CsvBindByName(column = "No. of People rated")
	private double numOfRates;
	
}
