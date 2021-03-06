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
public class LeadFromSiteValidationUtilImpl implements LeadFromSiteValidationUtil {

    @Autowired
    private SimpleFieldsValidationUtil simpleFieldsValidationUtil;

    @Override
    public boolean validate(LeadFromSite leadFromSite) throws ValidationException {
        Assert.notNull(leadFromSite.getLead());

        if(
                leadFromSite.getLead().getPhone() == null &&
                        leadFromSite.getLead().getEmail() == null
                ){
            return false;
        }

        if(
                leadFromSite.getLead().getEmail() != null &&
                        !this.getSimpleFieldsValidationUtil().validate(leadFromSite.getLead().getEmail(), SimpleFieldsValidationUtil.ValidationType.EMAIL)
                ){
            return false;
        }

        return true;
    }

    @Override
    public boolean validate(Object object) throws ValidationException {
        if(object instanceof LeadFromSite){
            LeadFromSite leadFromSite = (LeadFromSite) object;
            return this.validate(leadFromSite);
        }
        else{
            throw new ValidationException("Cannot validate object of class " + object.getClass() + "; expected instance of the dmodel.LeadFromSite");
        }
    }

    public SimpleFieldsValidationUtil getSimpleFieldsValidationUtil() {
        return simpleFieldsValidationUtil;
    }

    @Override
    public void setSimpleFieldsValidationUtil(SimpleFieldsValidationUtil simpleFieldsValidationUtil) {
        this.simpleFieldsValidationUtil = simpleFieldsValidationUtil;
    }
}
