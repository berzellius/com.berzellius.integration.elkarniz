package com.berzellius.integrations.elkarniz.businesslogic.rules.exceptions;

/**
 * Created by berz on 11.01.2017.
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message){
        super(message);
    }
}
