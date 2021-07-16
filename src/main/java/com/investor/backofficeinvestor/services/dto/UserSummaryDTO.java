package com.investor.backofficeinvestor.services.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserSummaryDTO {
    @Schema( type = "Boolean", description = "Indicates whether the user is active or not", example = "false")
    private Boolean active;

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
