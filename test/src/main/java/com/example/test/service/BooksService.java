package com.example.test.service;

import com.example.test.model.BooksModel;
import com.example.test.model.BorrowedBooksModel;
import com.example.test.repository.BooksRepository;
import com.example.test.repository.BorrowedBooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BooksService {
    private BooksRepository booksRepository;
    @Autowired
    private BorrowedBooksRepository borrowedBooksRepository;

    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public BooksModel findBookById(Integer bookId) {
        return booksRepository.findById(bookId).orElse(null);
    }
    public BooksModel addBook(String title, String author, String date, String description, String photoPath){
        if (title == null || author ==null)
        {
            return null;
        }
        else {
            if(booksRepository.findByTitleAndAuthor(title,author).isPresent()){
                System.out.println("duplicate book");
                return null;
            }
            BooksModel booksModel = new BooksModel();
            booksModel.setTitle(title);
            booksModel.setAuthor(author);
            booksModel.setDate(date);
            booksModel.setDescription(description);
            booksModel.setPhotoPath(photoPath);
            return booksRepository.save(booksModel);
        }
    }
    public Optional<BorrowedBooksModel> deleteById(int id) {
        // Implementacja usuwania książki (np. usunięcie z listy, bazy danych itp.)
        // Przykładowa implementacja:
        return borrowedBooksRepository.deleteById(id);
    }
    public void returnBook(Integer userId, Integer bookId) {
        borrowedBooksRepository.deleteByUserIdAndBookId(userId, bookId);
    }
    public Optional<BorrowedBooksModel> deleteBookById(int bookId) {
        // Implementacja usuwania książki (np. usunięcie z listy, bazy danych itp.)
        // Przykładowa implementacja:
        return borrowedBooksRepository.deleteById(bookId);
    }

    public List<BooksModel> getAllBooks() {
        return booksRepository.findAll();
    }

    public void saveBook(BooksModel book) {
        // Tutaj możesz dodać logikę walidacji lub inne operacje przed zapisaniem książki
        booksRepository.save(book);
    }
    public List<BooksModel> searchBooksByTitle(String title) {
        return booksRepository.findByTitleContainingIgnoreCase(title);
    }

}
