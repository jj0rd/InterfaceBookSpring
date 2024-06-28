package com.example.test.repository;

import com.example.test.model.BorrowedBooksModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface BorrowedBooksRepository extends JpaRepository<BorrowedBooksModel,Integer> {
    List<BorrowedBooksModel> findByUserId(int userId);
    Optional<BorrowedBooksModel> deleteById(int id);
    Optional<BorrowedBooksModel> findByUserIdAndBookId(int userId,int bookId);

    @Modifying
    @Transactional
    @Query("DELETE FROM BorrowedBooksModel b WHERE b.userId = :userId AND b.bookId = :bookId")
    void deleteByUserIdAndBookId(@Param("userId") int userId, @Param("bookId") int bookId);
}