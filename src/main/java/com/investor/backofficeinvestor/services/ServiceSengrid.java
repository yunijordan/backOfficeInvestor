package com.investor.backofficeinvestor.services;


import com.investor.backofficeinvestor.services.dto.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.*;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

public class ServiceSengrid {

//    @Autowired
//   RestTemplate restTemplate;
//
//    public String SendMail()  {
//
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("Authorization", "Bearer SG.S01CnJ5HSYGapz8vjm7pwg.n6lMYQpJIWeWa1HjJpehNMot0E");
//        String url = "https://api.sendgrid.com/v3/mail/send";
//
//
//        SendGridData sendGridData = new SendGridData();
//        sendGridData.setSubject("Hello");
//
//
//
//        SendGridBody sendGridBody = new SendGridBody();
//        sendGridBody.setType("text/plain");
//        sendGridBody.setValue("Hola mundo");
//        ArrayList<SendGridBody> content = new ArrayList<>();
//        content.add(sendGridBody);
//        sendGridData.setContent(content);
//
//        SendgridEmail sendgridEmailFrom = new SendgridEmail();
//        sendgridEmailFrom.setEmail("oficialjoaquinlopez@gmail.com");
//
//        sendGridData.setFrom(sendgridEmailFrom);
//
//        SendgridEmail sendgridEmailTo = new SendgridEmail();
//        sendgridEmailTo.setEmail("garciayunnier@gmail.com");
//
//        ArrayList<SendgridEmail> sendgridEmailsTo = new ArrayList<>();
//        sendgridEmailsTo.add(sendgridEmailTo);
//
//        SendGridTo sendGridTo = new SendGridTo();
//        sendGridTo.setTo(sendgridEmailsTo);
//
//        ArrayList<SendGridTo> arraySendgridTo = new ArrayList<>();
//        arraySendgridTo.add(sendGridTo);
//
//        SendGridPersonalization sendGridPersonalization = new SendGridPersonalization();
//        sendGridPersonalization.setPersonalization(arraySendgridTo);
//
//        ArrayList<SendGridPersonalization> personalizations = new ArrayList<>();
//        personalizations.add(sendGridPersonalization);
//
//        sendGridData.setPersonalizations(personalizations);
//
//
//
//
//        HttpEntity<SendGridData> entity = new HttpEntity<SendGridData>(sendGridData,headers);
//
//
//        return restTemplate.exchange(
//                url, HttpMethod.POST, entity, String.class).getBody();
//
//    }
}








