package com.clebergraciano.crud.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFundException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public ResourceNotFundException(String exception) {
        super(exception);
    }
}
