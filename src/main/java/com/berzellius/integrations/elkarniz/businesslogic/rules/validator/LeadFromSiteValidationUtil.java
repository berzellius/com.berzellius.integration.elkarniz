package com.berzellius.integrations.elkarniz.businesslogic.rules.validator;

import com.berzellius.integrations.elkarniz.businesslogic.rules.exceptions.ValidationException;
import com.berzellius.integrations.elkarniz.dmodel.LeadFromSite;
import org.springframework.stereotype.Service;

/**
 * Created by berz on 11.01.2017.
 */
@Service
public interface LeadFromSiteValidationUtil extends ValidationUtil {
    public boolean validate(LeadFromSite leadFromSite) throws ValidationException;

    void setSimpleFieldsValidationUtil(SimpleFieldsValidationUtil simpleFieldsValidationUtil);
}
