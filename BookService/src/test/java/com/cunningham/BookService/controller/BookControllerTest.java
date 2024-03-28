package com.cunningham.BookService.controller;

import com.cunningham.BookService.entity.Book;
import com.cunningham.BookService.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @MockBean
    private BookService bookService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void getAllBooks_ControllerTest() throws Exception {
        Book book1 = new Book(1L, "The Great Gatsby", "F. Scott Fitzgerald");
        Book book2 = new Book(2L, "1984", "George Orwell");

        given(bookService.getAllBooks()).willReturn(Arrays.asList(book1, book2));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("The Great Gatsby"))
                .andExpect(jsonPath("$[1].title").value("1984"));
    }

    @Test
    void createBook_ControllerTest() throws Exception {
        Book newBook = new Book(3L, "Brave New World", "Aldous Huxley");
        String newBookJson = "{\"title\":\"Brave New World\",\"author\":\"Aldous Huxley\"}";

        given(bookService.saveBook(any(Book.class))).willReturn(newBook);

        mockMvc.perform(post("/api/books/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newBookJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Brave New World"))
                .andExpect(jsonPath("$.author").value("Aldous Huxley"));
    }

    @Test
    void saveBooks_ControllerTest() throws Exception {
        List<Book> booksToSave = Arrays.asList(new Book(null, "To Kill a Mockingbird", "Harper Lee"), new Book(null, "The Catcher in the Rye", "J.D. Salinger"));
        String booksJson = "[{\"title\":\"To Kill a Mockingbird\",\"author\":\"Harper Lee\"}, {\"title\":\"The Catcher in the Rye\",\"author\":\"J.D. Salinger\"}]";

        given(bookService.saveAllBooks(anyList())).willReturn(booksToSave);

        mockMvc.perform(post("/api/books/saveAll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(booksJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("To Kill a Mockingbird"))
                .andExpect(jsonPath("$[1].title").value("The Catcher in the Rye"));
    }

    @Test
    void getBookById_ControllerTest() throws Exception {
        Book book = new Book(1L, "Moby Dick", "Herman Melville");

        given(bookService.getBookById(1L)).willReturn(Optional.of(book));

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Moby Dick"))
                .andExpect(jsonPath("$.author").value("Herman Melville"));
    }

    @Test
    void updateBook_ControllerTest() throws Exception {
        Book originalBook = new Book(1L, "Old Title", "Old Author");
        Book updatedBook = new Book(1L, "New Title", "New Author");
        String updatedBookJson = "{\"title\":\"New Title\",\"author\":\"New Author\"}";

        given(bookService.getBookById(1L)).willReturn(Optional.of(originalBook));
        given(bookService.saveBook(any(Book.class))).willReturn(updatedBook);

        mockMvc.perform(put("/api/books/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedBookJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Title"))
                .andExpect(jsonPath("$.author").value("New Author"));
    }

    @Test
    void deleteBook_ControllerTest() throws Exception {
        Long bookId = 1L;
        doNothing().when(bookService).deleteBook(bookId);

        given(bookService.getBookById(bookId)).willReturn(Optional.of(new Book()));

        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isOk());
    }

    @Test
    void findByTitle_BookExists_ControllerTest() throws Exception {
        Book book = new Book(1L, "Searched Title", "Author");
        given(bookService.findByTitle("Searched Title")).willReturn(Optional.of(book));

        mockMvc.perform(get("/api/books/search?title=Searched Title"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void findByTitle_BookNotFound_ControllerTest() throws Exception {
        given(bookService.findByTitle("Nonexistent Title")).willReturn(Optional.empty());

        mockMvc.perform(get("/api/books/search?title=Nonexistent Title"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void findByAuthor_ControllerTest() throws Exception {
        List<Book> books = Arrays.asList(new Book(1L, "Book One", "Queried Author"), new Book(2L, "Book Two", "Queried Author"));
        given(bookService.findByAuthor("Queried Author")).willReturn(books);

        mockMvc.perform(get("/api/books/author/Queried Author"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].author").value("Queried Author"))
                .andExpect(jsonPath("$[1].author").value("Queried Author"));
    }
}