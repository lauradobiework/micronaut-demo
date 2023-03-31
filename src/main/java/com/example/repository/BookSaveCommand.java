package com.example.repository;

import com.example.domain.Genre;
import io.micronaut.serde.annotation.Serdeable;

import javax.validation.constraints.NotBlank;
@Serdeable
public class BookSaveCommand {

    @NotBlank
    String name;
    @NotBlank
    private String isbn;
    private Genre genre;

    public BookSaveCommand(String name, String isbn, Genre genre) {
        this.name = name;
        this.isbn = isbn;
        this.genre = genre;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }
}
