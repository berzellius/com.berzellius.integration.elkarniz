package com.berzellius.integrations.elkarniz.businesslogic.rules.validator;

import com.berzellius.integrations.elkarniz.businesslogic.rules.exceptions.ValidationException;
import com.berzellius.integrations.elkarniz.dmodel.LeadFromSite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Created by berz on 11.01.2017.
 */
@Service
public class BusinessRulesValidatorImpl implements BusinessRulesValidator {

    @Autowired
    private LeadFromSiteValidationUtil leadFromSiteValidationUtil;

    @Override
    public ValidationUtil getValidationUtil(Object object) throws ValidationException {

        Assert.notNull(object);

        if(object instanceof LeadFromSite){
            return this.getLeadFromSiteValidationUtil();
        }

        throw new ValidationException("cannot get implementation of the ValidationUtil for object of class " + object.getClass().getName());
    }


    @Override
    public boolean validate(Object object) throws ValidationException {
        return this.getValidationUtil(object).validate(object);
    }

    public LeadFromSiteValidationUtil getLeadFromSiteValidationUtil() {
        return leadFromSiteValidationUtil;
    }
}
