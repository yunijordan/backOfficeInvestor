package com.investor.backofficeinvestor.repository;


import com.investor.backofficeinvestor.model.Payment;
import com.investor.backofficeinvestor.services.dto.SuscriptionDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findById(Long id);

    @Query(value = "SELECT new com.investor.backofficeinvestor.services.dto.SuscriptionDTO"
            + "(p.paymentEmail , p.payStatusCode, p.paymentDate)"
            + "FROM Payment p "
            + "WHERE p.paymentEmail = :paymentEmail "
            + "AND p.payStatusCode = :statusCode "
            + "ORDER BY p.paymentDate desc")
    List<SuscriptionDTO> findPaymentSuscription(String paymentEmail, String statusCode);

}
