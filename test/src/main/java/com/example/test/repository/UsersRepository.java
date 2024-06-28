package com.example.test.repository;

import com.example.test.model.UsersModel;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<UsersModel,Integer> {
    Optional<UsersModel> findByEmailAndPassword(String email, String password);

    Optional<UsersModel> findFirstByEmail(String email);

    Optional<UsersModel> findById(Integer id);
    List<UsersModel> findAllByRole(String role);
}
