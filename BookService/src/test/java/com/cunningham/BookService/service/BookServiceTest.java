package com.cunningham.BookService.service;

import com.cunningham.BookService.entity.Book;
import com.cunningham.BookService.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void getAllBooks_ReturnsListOfBooks() {
        List<Book> expectedBooks = Arrays.asList(
                new Book(1L, "Test Book 1", "Test Author 1"),
                new Book(2L, "Test Book 2", "Test Author 2")
        );
        when(bookRepository.findAll()).thenReturn(expectedBooks);

        List<Book> result = bookService.getAllBooks();

        assertThat(result).isEqualTo(expectedBooks);
        verify(bookRepository).findAll();
    }

    @Test
    void getBookById_ReturnsBook() {
        Optional<Book> expectedBook = Optional.of(new Book(1L, "Test Book", "Test Author"));
        when(bookRepository.findById(1L)).thenReturn(expectedBook);

        Optional<Book> result = bookService.getBookById(1L);

        assertThat(result).isEqualTo(expectedBook);
        verify(bookRepository).findById(1L);
    }

    @Test
    void saveBook_SavesAndReturnsBook() {
        Book bookToSave = new Book(null, "New Book", "New Author");
        Book savedBook = new Book(1L, "New Book", "New Author");
        when(bookRepository.save(bookToSave)).thenReturn(savedBook);

        Book result = bookService.saveBook(bookToSave);

        assertThat(result).isEqualTo(savedBook);
        verify(bookRepository).save(bookToSave);
    }

    @Test
    void saveAllBooks_SavesAndReturnsBooks() {
        List<Book> booksToSave = Arrays.asList(
                new Book(null, "New Book 1", "New Author 1"),
                new Book(null, "New Book 2", "New Author 2")
        );
        when(bookRepository.saveAll(booksToSave)).thenReturn(booksToSave);

        List<Book> result = bookService.saveAllBooks(booksToSave);

        assertThat(result).isEqualTo(booksToSave);
        verify(bookRepository).saveAll(booksToSave);
    }

    @Test
    void deleteBook_DeletesById() {
        Long bookId = 1L;
        doNothing().when(bookRepository).deleteById(bookId);

        bookService.deleteBook(bookId);

        verify(bookRepository).deleteById(bookId);
    }

    @Test
    void findByTitle_ReturnsBook() {
        String title = "Existing Book";
        Optional<Book> expectedBook = Optional.of(new Book(1L, title, "Author"));
        when(bookRepository.findByTitle(title)).thenReturn(expectedBook);

        Optional<Book> result = bookService.findByTitle(title);

        assertThat(result).isEqualTo(expectedBook);
        verify(bookRepository).findByTitle(title);
    }

    @Test
    void findByAuthor_ReturnsListOfBooks() {
        String author = "Existing Author";
        List<Book> expectedBooks = Arrays.asList(new Book(1L, "Book 1", author));
        when(bookRepository.findByAuthor(author)).thenReturn(expectedBooks);

        List<Book> result = bookService.findByAuthor(author);

        assertThat(result).isEqualTo(expectedBooks);
        verify(bookRepository).findByAuthor(author);
    }
}