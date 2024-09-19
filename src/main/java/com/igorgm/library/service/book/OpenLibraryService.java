package com.igorgm.library.service.book;

import com.igorgm.library.entity.book.OpenLibraryBookDTO;
import com.igorgm.library.entity.book.OpenLibraryResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;

@Service
public class OpenLibraryService {
	
	private static final String OPEN_LIBRARY_URL = "https://openlibrary.org/search.json";
	
	public List<OpenLibraryBookDTO> searchBooksByTitle(String title) {
		RestTemplate restTemplate = new RestTemplate();
		title = title.replace(' ', '+');
		String url = UriComponentsBuilder.fromHttpUrl(OPEN_LIBRARY_URL)
				.queryParam("q", title)
				.queryParam("fields", "title,author_name,ratings_average,ratings_count")
				.queryParam("limit", 20)
				.toUriString();
		
		OpenLibraryResponseDTO response = restTemplate.getForObject(url, OpenLibraryResponseDTO.class);
		return response != null ? response.getDocs() : Collections.emptyList();
	}
}