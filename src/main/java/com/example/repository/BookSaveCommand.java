package com.example.repository;

import com.example.domain.Genre;
import io.micronaut.serde.annotation.Serdeable;

import javax.validation.constraints.NotBlank;
@Serdeable
public class BookSaveCommand {

    @NotBlank
    String title;
    @NotBlank
    private String isbn;
    private Genre genre;

    public BookSaveCommand(String title, String isbn, Genre genre) {
        this.title = title;
        this.isbn = isbn;
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
