package com.investor.backofficeinvestor.services.dto;

import javax.validation.constraints.NotNull;

public class SuscriptionDTO {
    @NotNull
    private String email;

    public SuscriptionDTO(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
