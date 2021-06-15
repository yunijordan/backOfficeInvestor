package com.investor.backofficeinvestor.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends ApiException {
    private static final String ERROR_CODE = "not_found";

    public ResourceNotFoundException(String description) {
        super(ERROR_CODE, description, HttpStatus.NOT_FOUND.value());
    }
}
