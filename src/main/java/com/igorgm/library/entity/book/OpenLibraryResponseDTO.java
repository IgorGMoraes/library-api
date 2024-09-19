package com.igorgm.library.entity.book;

import lombok.Data;

import java.util.List;

@Data
public class OpenLibraryResponseDTO {
	private List<OpenLibraryBookDTO> docs;
}