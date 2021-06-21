package com.investor.backofficeinvestor.controller;


import com.google.gson.Gson;
import com.investor.backofficeinvestor.exceptions.ResourceNotFoundException;
import com.investor.backofficeinvestor.model.Payment;
import com.investor.backofficeinvestor.model.User;
import com.investor.backofficeinvestor.services.PaymentService;
import com.investor.backofficeinvestor.services.UserService;
import com.investor.backofficeinvestor.services.dto.PaymentDTO;
import com.investor.backofficeinvestor.services.dto.ResponseDTO;
import com.investor.backofficeinvestor.services.dto.SuscriptionDTO;
import com.investor.backofficeinvestor.services.dto.ValidateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
public class InvestorController {
    private final PaymentService paymentService;
    private final UserService userService;

    public InvestorController(PaymentService paymentService, UserService userService) {
        this.paymentService = paymentService;
        this.userService = userService;
    }


    @PostMapping("/test")
    public String Concept (@RequestBody  String ConceptRequest){
       return ConceptRequest;
    }

//    @PostMapping("/test1")
//    public Map<String, Object> Concept2 (@RequestBody String conceptRequest){
//
////        Gson gson = new Gson();
////        JsonParser parser = new JsonParser();
////        String json = gson.toJson(conceptRequest);
////
////
////        JsonObject o = parser.parse("{\"a\": \"A\"}").getAsJsonObject();
////        Map<String,Object> result =
////                new ObjectMapp().readValue(o, HashMap.class);
//
////
////        Gson gson = new Gson();
////        String json = gson.toJson(conceptRequest);
////        Map<String,Object> map = new HashMap<String,Object>();
////        map = (Map<String,Object>) gson.fromJson(json, map.getClass());
//
////        String jsonString = "{'employee.name':'Bob','employee.salary':10000}";
//        Gson gson = new Gson();
////        System.out.println(conceptRequest);
//        Map map = gson.fromJson(conceptRequest, Map.class);
//
//        String type = map.get("type").toString();
//        Map<String,Object> data = (Map<String, Object>) map.get("data");
//        Map<String,Object> objectT = (Map<String, Object>)data.get("object");
//        String id = objectT.get("id").toString();
//        String object = objectT.get("object").toString();
//        if (object.equals("charge"))
//        {
//            System.out.println(object);
//        }else
//            System.out.println("no es charge");
//        System.out.println(type);
//        System.out.println(id);
//        return  gson.fromJson(conceptRequest, Map.class);
//
////        return map;
//
//    }
    @Operation(summary = "Get a user by Id")
    @GetMapping("/payment/{email}")
    public ResponseEntity<PaymentDTO> getPaymentById(@Parameter(description = "email of user to be searched") @PathVariable(value = "email") String email) {
        String status = "succeeded";
        List<Payment> result = paymentService.findByPaymentEmail(email,status);
    PaymentDTO payment = new PaymentDTO();
    Long id = result.get(0).getId();
    payment.setPaymentId(id);
//            .orElseThrow(() -> new ResourceNotFoundException(String.format("User: %s not found", email)));

    return ResponseEntity.ok(payment);
}

    @PostMapping("/pay")
    public ResponseEntity<ResponseDTO> Concept1 ( @RequestBody String stripeRequest){
        Gson gson = new Gson();
        User user = new User();
//        auth.getPrincipal();
        Map map = gson.fromJson(stripeRequest, Map.class);
        Payment payment = new Payment();
        String type = map.get("type").toString();
        Map<String,Object> data = (Map<String, Object>) map.get("data");
        Map<String,Object> objectT = (Map<String, Object>)data.get("object");
        String id =  objectT.get("id").toString();
        String object = objectT.get("object").toString();
        String status = objectT.get("status").toString();
        Map<String,Object> billing_details = (Map)objectT.get("billing_details");
        String email = billing_details.get("email").toString();
        Double created = (Double) map.get("created");
        Long l =  created.longValue();
        Date date = new Date(Double.doubleToRawLongBits(l)*1000);
        Instant instant = Instant.ofEpochSecond(l);
        ZonedDateTime d = ZonedDateTime.ofInstant(instant, ZoneOffset.UTC);

        if (object.equals("charge"))
        {
//            payment.setUser(user);
            if(status.equals("succeeded")){
                payment.setStripeId(id);
                payment.setPayStatusCode(status);
                payment.setPaymentEmail(email);
                Optional<User> emailPosta = userService.findByEmail(email);
                payment.setUser(emailPosta.get());
                payment.setPaymentDate(d);
                System.out.println(created);
                System.out.println(l);
                System.out.println(date);
                System.out.println(d);

                paymentService.save(payment);
                return ResponseEntity.status(201).body(new ResponseDTO(true));
            }else if (status.equals("failed")){
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
        public ResponseEntity<ResponseDTO> Suscription (@Valid @RequestBody ValidateDTO suscriptionDTO){
        String email = suscriptionDTO.getEmail();
            String status = "succeeded";
            List<Payment> result = paymentService.findByPaymentEmail(email,status);
            Payment payment = result.get(0);
            Long id = result.get(0).getId();
            if (payment.getId().equals(id)) {
                ZonedDateTime date = ZonedDateTime.now();
                Long month= 1L;
                ZonedDateTime datePay = payment.getPaymentDate();
                if ((datePay.plusMonths(month)).isAfter(date)){
                    return ResponseEntity.status(200).body(new ResponseDTO(true));
                }
                return ResponseEntity.status(200).body(new ResponseDTO(false));
                }
            return ResponseEntity.status(400).body(new ResponseDTO(false));

        }


}
