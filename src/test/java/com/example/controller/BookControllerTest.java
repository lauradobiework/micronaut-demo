package com.example.controller;

import com.example.domain.Book;
import com.example.domain.Genre;
import com.example.repository.BookSaveCommand;
import com.example.repository.GenreSaveCommand;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.micronaut.http.HttpHeaders.LOCATION;
import static io.micronaut.http.HttpStatus.*;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class BookControllerTest {

    private BlockingHttpClient blockingClient;

    @Inject
    @Client("/")
    HttpClient client;

    @BeforeEach
    void setup() {
        blockingClient = client.toBlocking();
    }

    @Test
    void supplyAnInvalidOrderTriggersValidationFailure() {
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () ->
                blockingClient.exchange(HttpRequest.GET("/books/list?order=blah"))
        );

        assertNotNull(thrown.getResponse());
        assertEquals(BAD_REQUEST, thrown.getStatus());
    }

    @Test
    void testFindNonExistingBookReturns404() {
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () ->
                blockingClient.exchange(HttpRequest.GET("/books/99"))
        );

        assertNotNull(thrown.getResponse());
        assertEquals(NOT_FOUND, thrown.getStatus());
    }

    @Test
    void testGenericCrudOperations(){
        List<Long> genreIds = new ArrayList<>();
        List<Long> bookIds = new ArrayList<>();

        HttpRequest<?> genreRequest = HttpRequest.POST("/genres", new GenreSaveCommand("Horror"));
        HttpResponse<?> genreResponse = blockingClient.exchange(genreRequest);
        Long genreId = entityId(genreResponse, "/genres/");
        genreIds.add(genreId);

        assertEquals(CREATED, genreResponse.getStatus());

        genreRequest = HttpRequest.GET("/genres/" + genreId);

        Genre genre = blockingClient.retrieve(genreRequest, Genre.class);

        System.out.println(genre);

        HttpRequest<?> bookRequest = bookRequest = HttpRequest.POST("/books", new BookSaveCommand("The Haunting of Hill House", "666", genre));
        HttpResponse<?> bookResponse = blockingClient.exchange(genreRequest);
        Long bookId = entityId(bookResponse, "/books/");
        System.out.println("book id: " + bookId);
        bookIds.add(bookId);

        assertEquals(CREATED, bookResponse.getStatus());

        bookRequest = HttpRequest.GET("/books/" + bookId);

        Book book = blockingClient.retrieve(bookRequest, Book.class);

        assertEquals("The Haunting of Hill House", book.getTitle());

        // cleanup:
        for (Long id : genreIds) {
            genreRequest = HttpRequest.DELETE("/genres/" + id);
            genreResponse = blockingClient.exchange(genreRequest);
            assertEquals(NO_CONTENT, genreResponse.getStatus());
        }

        for (Long id : bookIds) {
            bookRequest = HttpRequest.DELETE("/books/" + id);
            bookResponse = blockingClient.exchange(bookRequest);
            assertEquals(NO_CONTENT, bookResponse.getStatus());
        }
    }

    private Long entityId(HttpResponse response, String path) {
        String value = response.header(LOCATION);
        if (value == null) {
            return null;
        }

        int index = value.indexOf(path);
        if (index != -1) {
            return Long.valueOf(value.substring(index + path.length()));
        }

        return null;
    }

}