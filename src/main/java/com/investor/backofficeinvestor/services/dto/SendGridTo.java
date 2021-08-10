package com.investor.backofficeinvestor.services.dto;

import java.util.ArrayList;

public class SendGridTo {
    private ArrayList<SendgridEmail> to;

    public ArrayList<SendgridEmail> getTo() {
        return to;
    }

    public void setTo(ArrayList<SendgridEmail> to) {
        this.to = to;
    }
}
