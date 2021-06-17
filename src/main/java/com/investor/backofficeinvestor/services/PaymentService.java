package com.investor.backofficeinvestor.services;

import com.investor.backofficeinvestor.model.Payment;
import com.investor.backofficeinvestor.repository.PaymentRepository;
import com.investor.backofficeinvestor.services.dto.PaymentDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class PaymentService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment updatePayment(PaymentDTO paymentDTO) {
        Payment save = null;
        Long id = paymentDTO.getId();
        Optional<Payment> bdPayment = paymentRepository.findById(id);
        if (bdPayment.isPresent()){
            Payment payment1= bdPayment.get();
            Date now = Date.from(Instant.now());
            payment1.setPaymentDate(now);
            String detail = payment1.getPayDetails();
            payment1.setPayDetails(detail);
            Long payId = payment1.getPaymentId();
            payment1.setPaymentId(payId);
            save = paymentRepository.save(payment1);

        }

        return save;
    }
}
