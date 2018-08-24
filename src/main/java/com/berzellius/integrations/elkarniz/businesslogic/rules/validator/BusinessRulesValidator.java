package com.berzellius.integrations.elkarniz.businesslogic.rules.validator;

import com.berzellius.integrations.elkarniz.businesslogic.rules.exceptions.ValidationException;
import org.springframework.stereotype.Service;

/**
 * Created by berz on 11.01.2017.
 */
@Service
public interface BusinessRulesValidator {
    public ValidationUtil getValidationUtil(Object object) throws ValidationException;

    boolean validate(Object object) throws ValidationException;
}
