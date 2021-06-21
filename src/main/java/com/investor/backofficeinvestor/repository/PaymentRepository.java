package com.investor.backofficeinvestor.repository;


import com.investor.backofficeinvestor.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findById(Long id);

    List<Payment> findByPaymentEmailAndPayStatusCodeOrderByIdDesc(String paymentEmail, String statusCode);

////    @Query("SELECT new net.veritran.backoffice.services.dto.response_logs.ResponseLogTotalsDTO"
////            + "(COUNT(distinct rl.id), COUNT(rl.tokenReferenceId), COUNT(rl.panReferenceId)) "
////            + "FROM ResponseLog rl "
////            + "WHERE rl.issuer.id = :issuerId")
//
//    @Query("SELECT * FROM PaymentDTO t" +
//    "WHERE t.email = t.paymentEmail and t.status = "
//    ORDER BY t.date DESC")

//    List<Payment> findPaymentByPaymentEmail(String paymentEmail);
}
