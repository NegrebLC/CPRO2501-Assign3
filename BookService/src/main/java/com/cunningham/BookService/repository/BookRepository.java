package com.cunningham.BookService.repository;

import com.cunningham.BookService.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    // Find a single book by its title. This returns an Optional<Book>, which can be empty if no book is found.
    Optional<Book> findByTitle(String title);

    // Find all books by a given author. This returns a list of books, which can be empty if no books are found.
    List<Book> findByAuthor(String author);
}
