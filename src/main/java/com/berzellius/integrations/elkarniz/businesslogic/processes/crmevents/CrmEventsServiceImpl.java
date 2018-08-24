package com.berzellius.integrations.elkarniz.businesslogic.processes.crmevents;


import com.berzellius.integrations.basic.exception.APIAuthException;
import com.berzellius.integrations.elkarniz.businesslogic.config.APIConfig;
import com.berzellius.integrations.elkarniz.dmodel.ContactAdded;
import com.berzellius.integrations.elkarniz.dto.site.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.berzellius.integrations.elkarniz.repository.ContactAddedRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.function.Consumer;

/**
 * Created by berz on 14.08.2018.
 */
@Service
public class CrmEventsServiceImpl implements CrmEventsService {
    private static final Logger log = LoggerFactory.getLogger(CrmEventsService.class);

    @Autowired
    private ContactAddedRepository contactAddedRepository;

    @Autowired
    private CrmEventsBusinessProcess crmEventsBusinessProcess;

    @Autowired
    private APIConfig apiConfig;


    @Override
    public Result newContactsAddedInCRM(ContactsAddingRequest contactsAddingRequest) {

        if(contactsAddingRequest.getAddedContacts().size() > 0){

            for(ContactAddDTO contactAddDTO : contactsAddingRequest.getAddedContacts()){

                ContactAdded contactAdded = new ContactAdded();
                contactAdded.setState(ContactAdded.State.NEW);
                contactAdded.setContactId(contactAddDTO.getContactId());
                contactAdded.setDtmCreate(new Date());

                contactAddedRepository.save(contactAdded);
            }

            this.runProcessingAddedContacts();
        }
        return new Result("success");
    }

    @Scheduled(fixedDelay = 60000)
    protected void runProcessingAddedContacts(){

        Consumer<ContactAdded> processContactAdd = new Consumer<ContactAdded>() {
            @Override
            public void accept(ContactAdded contactAdded) {

                try {

                    crmEventsBusinessProcess.processAddedContact(contactAdded);
                } catch (APIAuthException e) {

                    log.error(
                            "Authentification error while processing AddedContact#" + contactAdded.getContactId() +
                                    "/id=" + contactAdded.getId() + " :: " + e.getMessage());
                } catch (RuntimeException e) {

                    log.error("Runtime Exception while processing  AddedContact#" + contactAdded.getContactId() +
                            "id=" + contactAdded.getId() + " :: " + e.getMessage());
                } finally {

                    ContactAdded contactAdded1 = contactAddedRepository.findOne(contactAdded.getId());
                    contactAdded1.setLastProcessed(new Date());
                    contactAddedRepository.save(contactAdded1);
                }
            }
        };

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, (-1) * apiConfig.getAmocrmAddedContactProcessDelay());

        contactAddedRepository.findByStateAndDtmCreateLessThan(ContactAdded.State.NEW, calendar.getTime()).forEach(processContactAdd);
    }
}
