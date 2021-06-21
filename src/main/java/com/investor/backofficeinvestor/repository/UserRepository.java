package com.investor.backofficeinvestor.repository;

import java.util.Optional;

import com.investor.backofficeinvestor.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByValidationCodeAndEmail(Integer validationCode, String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);


}