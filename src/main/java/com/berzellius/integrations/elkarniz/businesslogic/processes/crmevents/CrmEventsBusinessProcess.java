package com.berzellius.integrations.elkarniz.businesslogic.processes.crmevents;

import com.berzellius.integrations.basic.exception.APIAuthException;
import com.berzellius.integrations.elkarniz.dmodel.ContactAdded;
import org.springframework.stereotype.Service;

/**
 * Created by berz on 14.08.2018.
 */
@Service
public interface CrmEventsBusinessProcess {
    void processAddedContact(ContactAdded contactAdded) throws APIAuthException;
}
