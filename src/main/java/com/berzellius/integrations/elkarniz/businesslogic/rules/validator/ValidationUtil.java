package com.berzellius.integrations.elkarniz.businesslogic.rules.validator;


import com.berzellius.integrations.elkarniz.businesslogic.rules.exceptions.ValidationException;

/**
 * Created by berz on 11.01.2017.
 */
public interface ValidationUtil {
    public boolean validate(Object object) throws ValidationException;
}
