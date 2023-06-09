package com.example.repository;

import com.example.domain.Book;
import com.example.domain.Genre;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface BookRepository {

    Optional<Book> findById(long id);

    Book save(@NotBlank String name, @NotBlank String isbn, Genre genre);

    Book saveWithException(@NotBlank String name, @NotBlank String isbn, Genre genre);

    void deleteById(long id);

    List<Book> findAll(@NotNull BookSortingAndOrderArguments args);

    int update(long id, @NotBlank String name, @NotBlank String isbn, Genre genre);
}
