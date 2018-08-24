package com.berzellius.integrations.elkarniz.businesslogic.processes.crmevents;


import com.berzellius.integrations.elkarniz.dto.site.ContactsAddingRequest;
import com.berzellius.integrations.elkarniz.dto.site.Result;
import org.springframework.stereotype.Service;

/**
 * Created by berz on 14.03.2017.
 */
@Service
public interface CrmEventsService {
    Result newContactsAddedInCRM(ContactsAddingRequest contactsAddingRequest);
}
