package com.investor.backofficeinvestor.repository;


import com.investor.backofficeinvestor.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findById(Payment iPayment);
}
