package com.investor.backofficeinvestor.repository;

import java.util.Optional;

import com.investor.backofficeinvestor.model.ERole;
import com.investor.backofficeinvestor.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}