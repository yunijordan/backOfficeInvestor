package com.investor.backofficeinvestor.controller;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.investor.backofficeinvestor.model.Payment;
import com.investor.backofficeinvestor.model.User;
import com.investor.backofficeinvestor.services.PaymentService;
import com.investor.backofficeinvestor.services.UserService;
import com.investor.backofficeinvestor.services.dto.ResponseDTO;
import jdk.nashorn.internal.ir.ObjectNode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
public class InvestorController {
    private final PaymentService paymentService;

    public InvestorController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    @PostMapping("/test")
    public String Concept (@RequestBody  String ConceptRequest){
       return ConceptRequest;
    }

    @PostMapping("/test1")
    public Map<String, Object> Concept2 (@RequestBody String conceptRequest){

//        Gson gson = new Gson();
//        JsonParser parser = new JsonParser();
//        String json = gson.toJson(conceptRequest);
//
//
//        JsonObject o = parser.parse("{\"a\": \"A\"}").getAsJsonObject();
//        Map<String,Object> result =
//                new ObjectMapp().readValue(o, HashMap.class);

//
//        Gson gson = new Gson();
//        String json = gson.toJson(conceptRequest);
//        Map<String,Object> map = new HashMap<String,Object>();
//        map = (Map<String,Object>) gson.fromJson(json, map.getClass());

//        String jsonString = "{'employee.name':'Bob','employee.salary':10000}";
        Gson gson = new Gson();
//        System.out.println(conceptRequest);
        Map map = gson.fromJson(conceptRequest, Map.class);

        String type = map.get("type").toString();
        Map<String,Object> data = (Map<String, Object>) map.get("data");
        Map<String,Object> objectT = (Map)data.get("object");
        String id = objectT.get("id").toString();
        String object = objectT.get("object").toString();
        if (object.equals("charge"))
        {
            System.out.println(object);
        }else
            System.out.println("no es charge");
        System.out.println(type);
        System.out.println(id);
        return  gson.fromJson(conceptRequest, Map.class);

//        return map;

    }
    @PostMapping("/pay")
    public ResponseEntity<ResponseDTO> Concept1 (Authentication auth, @RequestBody String stripeRequest){
        Gson gson = new Gson();
//        auth.getPrincipal();
        Map map = gson.fromJson(stripeRequest, Map.class);
        Payment payment = new Payment();
        String type = map.get("type").toString();
        Map<String,Object> data = (Map<String, Object>) map.get("data");
        Map<Integer,Object> objectT = (Map)data.get("object");
        String id =  objectT.get("id").toString();
        String object = objectT.get("object").toString();
        String status = objectT.get("status").toString();


        if (object.equals("charge"))
        {
//            payment.setUser(user);
            if(status.equals("succeeded")){
                payment.setPaymentId(id);
                payment.setPayStatusCode(status);
                paymentService.save(payment);
                return ResponseEntity.status(201).body(new ResponseDTO(true));
            }else if (status.equals("failed")){
                String failureCode = objectT.get("failure_code").toString();
                String failureMmessage = objectT.get("failure_message").toString();
                payment.setPaymentId(id);
                payment.setPayStatusCode(status);
                payment.setFailureCode(failureCode);
                payment.setFailureMessage(failureMmessage);
                paymentService.save(payment);
                return ResponseEntity.status(202).body(new ResponseDTO(false));
            }
        }
        return ResponseEntity.status(400).body(new ResponseDTO(false));
        }

}
