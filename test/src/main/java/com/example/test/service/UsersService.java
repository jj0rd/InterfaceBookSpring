package com.example.test.service;

import com.example.test.model.BooksModel;
import com.example.test.model.BorrowedBooksModel;
import com.example.test.model.UsersModel;
import com.example.test.repository.BooksRepository;
import com.example.test.repository.BorrowedBooksRepository;
import com.example.test.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsersService {

    private UsersRepository usersRepository;

    @Autowired
    private BorrowedBooksRepository borrowedBooksRepository;

    @Autowired
    private BooksRepository booksRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public UsersModel registerUser(String email,String password, String fName, String lName){
        if (email == null || password ==null)
        {
            return null;
        }
        else {
            if(usersRepository.findFirstByEmail(email).isPresent()){
                System.out.println("duplicate email");
                return null;
            }
            UsersModel usersModel = new UsersModel();
            usersModel.setEmail(email);
            usersModel.setPassword(password);
            usersModel.setfName(fName);
            usersModel.setlName(lName);
            usersModel.setRole("user");
            return usersRepository.save(usersModel);
        }
    }
    public UsersModel authenticate(String email,String password){
        return usersRepository.findByEmailAndPassword(email,password).orElse(null);
    }
    public UsersModel findUserById(Integer userId) {
        return usersRepository.findById(userId).orElse(null);
    }
    public List<UsersModel> getAllUsers() {
        return usersRepository.findAllByRole("user");
    }
    public List<BooksModel> getUserBooks(int userId) {
        List<BorrowedBooksModel> borrowedBooks = borrowedBooksRepository.findByUserId(userId);
        List<BooksModel> books = new ArrayList<>();
        for (BorrowedBooksModel borrowedBook : borrowedBooks) {
            books.add(booksRepository.findById(borrowedBook.getBookId()).orElse(null));
        }
        return books;
    }

}
