package com.investor.backofficeinvestor.services.dto;

import java.util.ArrayList;

public class SendGridPersonalization {
    private ArrayList<SendGridTo> personalization;

    public ArrayList<SendGridTo> getPersonalization() {
        return personalization;
    }

    public void setPersonalization(ArrayList<SendGridTo> personalization) {
        this.personalization = personalization;
    }
}
