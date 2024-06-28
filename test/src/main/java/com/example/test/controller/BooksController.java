package com.example.test.controller;

import com.example.test.model.BorrowedBooksModel;
import com.example.test.model.UsersModel;
import com.example.test.service.BooksService;
import com.example.test.model.BooksModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.awt.print.Book;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class BooksController {
    @Autowired
    private BooksService booksService;

    @GetMapping("/bookList")
    public String getBookListPage(Model model){
        model.addAttribute("books", booksService.getAllBooks());
        return "listaksiazek";
    }

    @GetMapping("/bookListAdmin")
    public String getBookListAdminPage(Model model){
        model.addAttribute("books", booksService.getAllBooks());
        return "listaksiazekAD";
    }

    @GetMapping("/addBook")
    public String getAddBookPage(Model model) {
        model.addAttribute("book", new BooksModel());
        return "dodajksiazke";
    }

    @GetMapping("/bookPage")
    public String getBookPage(@RequestParam("id") int bookId, Model model) {
        // Tutaj pobierz szczegółowe informacje o książce na podstawie jej identyfikatora
        BooksModel book = booksService.findBookById(bookId);

        // Przekazanie informacji o książce do widoku
        model.addAttribute("book", book);
        return "ksiazkastrona";
    }

    @GetMapping("/editBook")
    public String getEditBookPage(@RequestParam("id") int bookId, Model model) {
        BooksModel book = booksService.findBookById(bookId);
        model.addAttribute("book", book);
        return "edytujksiazke"; // Zwraca nazwę szablonu HTML dla strony edycji
    }

    @PostMapping("/editBook")
    public String editBook(@ModelAttribute BooksModel book) {
        booksService.saveBook(book);
        return "redirect:/bookListAdmin"; // Przekierowuje użytkownika po edycji książki
    }
    @PostMapping("/returnBook")
    public String returnBook(@RequestParam("bookId") Integer bookId, @RequestParam("userId") Integer userId) {
        booksService.returnBook(userId, bookId);

        return "redirect:/userBooks?id=" + userId;
    }
    @PostMapping("/addBook")
    public String addBook(@ModelAttribute BooksModel booksModel, @RequestParam("photo") MultipartFile photoFile) {
        try {
            System.out.println("addBook request: " + booksModel);

            // Sprawdź, czy przesłano zdjęcie
            if (!photoFile.isEmpty()) {
                String fileName = StringUtils.cleanPath(photoFile.getOriginalFilename());

                // Zapisz zdjęcie na dysku lub w odpowiednim miejscu
                // Tutaj możesz użyć kodu do zapisu zdjęcia na dysku, np. za pomocą java.nio.file.Files

                // Ustaw ścieżkę do zdjęcia w obiekcie książki
                booksModel.setPhotoPath("/images/" + fileName);
            }

            BooksModel addedBook = booksService.addBook(booksModel.getTitle(), booksModel.getAuthor(), booksModel.getDate(), booksModel.getDescription(), booksModel.getPhotoPath());
            return addedBook == null ? "errorPage" : "redirect:/addBook";
        } catch (Exception ex) {
            // Obsłuż błąd związany z przetwarzaniem zdjęcia
            ex.printStackTrace();
            return "errorPage";
        }
    }
    @GetMapping("/search")
    public String getSearchPage(Model model) {
        //model.addAttribute("loginRequest",new UsersModel());
        return "wyszukiwarka";
    }
    @GetMapping("/searchResults")
    public String searchBooks(@RequestParam("title") String title, Model model) {
        List<BooksModel> books = booksService.searchBooksByTitle(title);
        model.addAttribute("books", books);
        return "searchResults";
    }
}
