package com.investor.backofficeinvestor.services;

import java.io.IOException;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;


@Service
@Transactional
public class EmailService {
	
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
    
	@Value( "${sendgrid.api.key}" )
    private String sendGridApiKey;
    
    public Boolean sendEmail(String email, String emailContent) {
    	Email from = new Email("info@inversorinmobiliario.org");
        String subject = "Contacto Inversor Inmobiliario";
        Email to = new Email(email);
        Content content = new Content("text/plain", emailContent);
        Mail mail = new Mail(from, subject, to, content);

        
        String sendGridApiKeyEnvVariable = System.getenv("sendgrid.api.key");
        SendGrid sg = new SendGrid(sendGridApiKeyEnvVariable);
        Request request = new Request();
        try {
          request.setMethod(Method.POST);
          request.setEndpoint("mail/send");
          request.setBody(mail.build());
          Response response = sg.api(request);
          System.out.println(response.getStatusCode());
          System.out.println(response.getBody());
          System.out.println(response.getHeaders());
          
          return true;
        } catch (IOException ex) {
          return false;
        }
    }
}
