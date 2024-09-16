package com.igorgm.library.entity.book;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Table(name = "book")
@Entity(name = "book")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotBlank
	@Column(length = 500)
	private String title;
	
	@NotBlank
	private String author;
	
	@NotBlank
	private String genre;
	
	private double price;
	private double rating;
	private int numberOfRates;
	
}