package com.cunningham.BookService.controller;


import com.cunningham.BookService.entity.Book;
import com.cunningham.BookService.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // Get all books
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    // Save a new book
    @PostMapping("/save")
    public Book createBook(@Valid @RequestBody Book book) {
        return bookService.saveBook(book);
    }

    // Saves all books in list
    @PostMapping("/saveAll")
    public List<Book> saveBooks(@Valid @RequestBody List<Book> books) {
        return bookService.saveAllBooks(books);
    }

    // Get a single book by ID
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookService.getBookById(id);
        return book.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update a book
    @PutMapping("/update/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @Valid @RequestBody Book bookDetails) {
        return bookService.getBookById(id)
                .map(book -> {
                    book.setTitle(bookDetails.getTitle());
                    book.setAuthor(bookDetails.getAuthor());
                    Book updatedBook = bookService.saveBook(book);
                    return ResponseEntity.ok(updatedBook);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete a book
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        return bookService.getBookById(id)
                .map(book -> {
                    bookService.deleteBook(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Search for a book by title
    @GetMapping("/search")
    public ResponseEntity<Boolean> findByTitle(@RequestParam String title) {
        Optional<Book> book = bookService.findByTitle(title);
        return ResponseEntity.ok(book.isPresent());
    }

    // Find all books by an author
    @GetMapping("/author/{author}")
    public List<Book> findByAuthor(@PathVariable String author) {
        return bookService.findByAuthor(author);
    }
}