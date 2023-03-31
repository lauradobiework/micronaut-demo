package com.example.repository;

import com.example.config.ApplicationConfiguration;
import com.example.domain.Book;
import com.example.domain.Genre;
import io.micronaut.transaction.annotation.ReadOnly;
import jakarta.inject.Singleton;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
@Singleton
public class BookRepositoryImpl implements BookRepository{

    private static final List<String> VALID_PROPERTY_NAMES = Arrays.asList("id", "name", "isbn","genre");
    private final EntityManager entityManager;
    private final ApplicationConfiguration applicationConfiguration;

    public BookRepositoryImpl(EntityManager entityManager,
                               ApplicationConfiguration applicationConfiguration) {
        this.entityManager = entityManager;
        this.applicationConfiguration = applicationConfiguration;
    }
    @Override
    @ReadOnly
    public Optional<Book> findById(long id) {
        return Optional.ofNullable(entityManager.find(Book.class, id));
    }

    @Override
    @Transactional
    public Book save(@NotBlank String name, @NotBlank String isbn, Genre genre) {
        Book book = new Book(name, isbn, genre);
        entityManager.persist(book);
        return book;
    }

    @Override
    public Book saveWithException(@NotBlank String name, @NotBlank String isbn, Genre genre) {
        save(name, isbn, genre);
        throw new PersistenceException();
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        findById(id).ifPresent(entityManager::remove);
    }

    @ReadOnly
    public List<Book> findAll(@NotNull SortingAndOrderArguments args) {
        String qlString = "SELECT b FROM Book as b";
        if (args.getOrder().isPresent() && args.getSort().isPresent() && VALID_PROPERTY_NAMES.contains(args.getSort().get())) {
            qlString += " ORDER BY b." + args.getSort().get() + ' ' + args.getOrder().get().toLowerCase();
        }
        TypedQuery<Book> query = entityManager.createQuery(qlString, Book.class);
        query.setMaxResults(args.getMax().orElseGet(applicationConfiguration::getMax));
        args.getOffset().ifPresent(query::setFirstResult);

        return query.getResultList();
    }

    @Override
    public int update(long id, @NotBlank String name, @NotBlank String isbn, Genre genre) {
        return entityManager.createQuery("UPDATE Book b SET name = :name, isbn = :isbn, genre = :genre where id = :id")
                .setParameter("name", name)
                .setParameter("id", id)
                .setParameter("isbn", isbn)
                .setParameter("genre", genre)
                .executeUpdate();
    }
}
