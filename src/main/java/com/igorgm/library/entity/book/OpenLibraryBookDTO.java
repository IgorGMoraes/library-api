package com.igorgm.library.entity.book;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OpenLibraryBookDTO {
	private String title;
	private List<String> author_name;
	private double ratings_average;
	private int ratings_count;
}