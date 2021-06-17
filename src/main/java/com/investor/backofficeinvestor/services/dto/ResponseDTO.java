package com.investor.backofficeinvestor.services.dto;

public class ResponseDTO {

    private Boolean status;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public ResponseDTO(Boolean status) {
        this.status = true;
    }
}
