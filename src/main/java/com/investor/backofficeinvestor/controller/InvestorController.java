package com.investor.backofficeinvestor.controller;

import com.google.gson.Gson;
import com.investor.backofficeinvestor.model.Payment;
import com.investor.backofficeinvestor.model.User;
import com.investor.backofficeinvestor.payload.response.MessageResponse;
import com.investor.backofficeinvestor.repository.UserRepository;
import com.investor.backofficeinvestor.services.PaymentService;
import com.investor.backofficeinvestor.services.UserService;
import com.investor.backofficeinvestor.services.dto.ResponseDTO;

import com.investor.backofficeinvestor.services.dto.SuscriptionDTO;
import com.investor.backofficeinvestor.services.dto.ValidateDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
public class InvestorController {
    private final PaymentService paymentService;
    private final UserRepository userRepository;
    private final UserService userService;

    public InvestorController(PaymentService paymentService, UserRepository userRepository, UserService userService) {
        this.paymentService = paymentService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/pay")
    public ResponseEntity<ResponseDTO> Concept1(@RequestBody String stripeRequest) {
        Gson gson = new Gson();
        Map map = gson.fromJson(stripeRequest, Map.class);
        Payment payment = new Payment();
        Map<String, Object> data = (Map<String, Object>) map.get("data");
        Map<String, Object> objectT = (Map<String, Object>) data.get("object");
        String id = objectT.get("id").toString();
        String object = objectT.get("object").toString();
        String status = objectT.get("status").toString();
        Map<String, Object> billing_details = (Map) objectT.get("billing_details");
        String email = billing_details.get("email").toString();
        Double created = (Double) map.get("created");
        Long l = created.longValue();
        Instant instant = Instant.ofEpochSecond(l);
        ZonedDateTime d = ZonedDateTime.ofInstant(instant, ZoneOffset.UTC);
        if (object.equals("charge")) {
            if (status.equals("succeeded")) {
                payment.setStripeId(id);
                payment.setPayStatusCode(status);
                payment.setPaymentEmail(email);
                Optional<User> emailPosta = userService.findByEmail(email);
                payment.setUser(emailPosta.get());
                payment.setPaymentDate(d);
                paymentService.save(payment);
                return ResponseEntity.status(201).body(new ResponseDTO(true));
            } else if (status.equals("failed")) {
                String failureCode = objectT.get("failure_code").toString();
                String failureMmessage = objectT.get("failure_message").toString();
                payment.setStripeId(id);
                payment.setPayStatusCode(status);
                payment.setFailureCode(failureCode);
                payment.setFailureMessage(failureMmessage);
                paymentService.save(payment);
                return ResponseEntity.status(202).body(new ResponseDTO(false));
            }
        }
        return ResponseEntity.status(400).body(new ResponseDTO(false));
    }

    @PostMapping("/suscription")
    public ResponseEntity<?> Suscription(@Valid @RequestBody ValidateDTO suscriptionDTO) {

        String email = suscriptionDTO.getEmail();
        String status = "succeeded";
        List<SuscriptionDTO> result = paymentService.findByPaymentEmail(email, status);

        if (!result.isEmpty()) {
            SuscriptionDTO payment = result.get(0);

            ZonedDateTime date = ZonedDateTime.now();
            ZonedDateTime datePay = payment.getPaymentdate();
            Long month = 1L;

            if ((datePay.plusMonths(month)).isAfter(date)) {
                MessageResponse messageResponse = new MessageResponse();
                messageResponse.setStatus(0);
                messageResponse.setMessage("Valid suscription!");
                return ResponseEntity.ok(messageResponse);
            }
        }

        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setStatus(1);
        messageResponse.setMessage("Invalid suscription!");
        return ResponseEntity.ok(messageResponse);
    }

    @GetMapping("/payment_url")
    public String paymentUrl() {
        return userService.paymentUrl();
    }

}
