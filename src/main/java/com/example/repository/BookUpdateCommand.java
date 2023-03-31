package com.example.repository;

import com.example.domain.Genre;
import io.micronaut.serde.annotation.Serdeable;

import javax.validation.constraints.NotBlank;
@Serdeable
public class BookUpdateCommand {

    private long id;
    @NotBlank
    String name;
    @NotBlank
    private String isbn;
    private Genre genre;

    public BookUpdateCommand(long id, String name, String isbn, Genre genre) {
        this.id = id;
        this.name = name;
        this.isbn = isbn;
        this.genre = genre;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
