package com.investor.backofficeinvestor.services.dto;

import java.util.ArrayList;

public class SendGridData {

    private SendgridEmail from;
    private String subject;
    private ArrayList<SendGridBody> content;
    private ArrayList<SendGridPersonalization> personalizations;


    public SendgridEmail getFrom() {
        return from;
    }

    public void setFrom(SendgridEmail from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public ArrayList<SendGridBody> getContent() {
        return content;
    }

    public void setContent(ArrayList<SendGridBody> content) {
        this.content = content;
    }

    public ArrayList<SendGridPersonalization> getPersonalizations() {
        return personalizations;
    }

    public void setPersonalizations(ArrayList<SendGridPersonalization> personalizations) {
        this.personalizations = personalizations;
    }
}
