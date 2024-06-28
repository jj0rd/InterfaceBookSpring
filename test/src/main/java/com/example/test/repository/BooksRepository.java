package com.example.test.repository;

import com.example.test.model.BooksModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BooksRepository extends JpaRepository<BooksModel,Integer> {
    Optional<BooksModel> findById(Integer id);

    Optional<BooksModel> findByTitleAndAuthor(String title,String author);
    List<BooksModel> findByTitleContainingIgnoreCase(String title);
}
