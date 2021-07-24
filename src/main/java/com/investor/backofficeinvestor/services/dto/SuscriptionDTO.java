package com.investor.backofficeinvestor.services.dto;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

public class SuscriptionDTO {
    @NotNull
    private String email;
    @NotNull
    private String statuscode;
    @NotNull
    private ZonedDateTime paymentdate;

    public SuscriptionDTO(String email, String statuscode, ZonedDateTime paymentdate) {
        this.email = email;
        this.statuscode = statuscode;
        this.paymentdate = paymentdate;
    }

    public String getEmail() {
        return email;
    }

    public String getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(String statuscode) {
        this.statuscode = statuscode;
    }

    public ZonedDateTime getPaymentdate() {
        return paymentdate;
    }

    public void setPaymentdate(ZonedDateTime paymentdate) {
        this.paymentdate = paymentdate;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
