package com.example.test;

import com.example.test.controller.BooksController;
import com.example.test.model.BooksModel;
import com.example.test.service.BooksService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class BooksControllerTest {

    @Mock
    private BooksService booksService;

    @Mock
    private Model model;

    @InjectMocks
    private BooksController booksController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetBookListPage() {
        List<BooksModel> books = Collections.singletonList(new BooksModel());
        when(booksService.getAllBooks()).thenReturn(books);

        String viewName = booksController.getBookListPage(model);

        verify(model).addAttribute("books", books);
        assertEquals("listaksiazek", viewName);
    }

    @Test
    public void testGetBookPage() {
        int bookId = 1;
        BooksModel book = new BooksModel();
        when(booksService.findBookById(bookId)).thenReturn(book);

        String viewName = booksController.getBookPage(bookId, model);

        verify(model).addAttribute("book", book);
        assertEquals("ksiazkastrona", viewName);
    }

    @Test
    public void testAddBook() throws Exception {
        BooksModel booksModel = new BooksModel();
        MultipartFile photoFile = mock(MultipartFile.class);
        when(photoFile.isEmpty()).thenReturn(true);

        String viewName = booksController.addBook(booksModel, photoFile);

        verify(booksService).addBook(anyString(), anyString(), any(), anyString(), anyString());
        assertEquals("redirect:/addBook", viewName);
    }

    @Test
    public void testSearchBooks() {
        String title = "test";
        List<BooksModel> books = Collections.singletonList(new BooksModel());
        when(booksService.searchBooksByTitle(title)).thenReturn(books);

        String viewName = booksController.searchBooks(title, model);

        verify(model).addAttribute("books", books);
        assertEquals("searchResults", viewName);
    }
}
