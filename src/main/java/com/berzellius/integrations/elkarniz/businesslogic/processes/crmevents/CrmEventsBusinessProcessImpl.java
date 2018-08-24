package com.berzellius.integrations.elkarniz.businesslogic.processes.crmevents;



import com.berzellius.integrations.amocrmru.dto.api.amocrm.AmoCRMContact;
import com.berzellius.integrations.amocrmru.dto.api.amocrm.AmoCRMLead;
import com.berzellius.integrations.amocrmru.dto.api.amocrm.AmoCRMTask;
import com.berzellius.integrations.amocrmru.dto.api.amocrm.response.AmoCRMCreatedEntityResponse;
import com.berzellius.integrations.amocrmru.dto.api.amocrm.response.AmoCRMCreatedLeadsResponse;
import com.berzellius.integrations.amocrmru.service.AmoCRMService;
import com.berzellius.integrations.basic.exception.APIAuthException;
import com.berzellius.integrations.calltrackingru.dto.api.calltracking.Call;
import com.berzellius.integrations.elkarniz.businesslogic.config.APIConfig;
import com.berzellius.integrations.elkarniz.dmodel.ContactAdded;
import com.berzellius.integrations.elkarniz.repository.ContactAddedRepository;
import com.berzellius.integrations.service.CallTrackingAPIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by berz on 10.10.2015.
 */
@Service
@Transactional
public class CrmEventsBusinessProcessImpl implements CrmEventsBusinessProcess {
    @Autowired
    private APIConfig apiConfig;

    @Autowired
    private CallTrackingAPIService callTrackingAPIService;

    @Autowired
    private AmoCRMService amoCRMService;

    @Autowired
    private ContactAddedRepository contactAddedRepository;

    private final static Logger log = LoggerFactory.getLogger(CrmEventsService.class);

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void processAddedContact(ContactAdded contactAdded) throws APIAuthException {

        Assert.notNull(contactAdded);
        Assert.notNull(contactAdded.getContactId());
        log.info("Start processing added contact #" + contactAdded.getId());

        AmoCRMContact crmContact = amoCRMService.getContactById(contactAdded.getContactId());
        // Ищем незакрытые сделки
        List<AmoCRMLead> crmLeads = amoCRMService.getLinkedLeadsByContactIdAndFilter(contactAdded.getContactId(), new Function<AmoCRMLead, Boolean>() {
            @Override
            public Boolean apply(AmoCRMLead amoCRMLead) {
                return !amoCRMService.getLeadClosedStatusesIDs().contains(amoCRMLead.getStatus_id());
            }
        });

        if(crmLeads.size() == 0 && !crmContact.hasTagById(apiConfig.getAmocrmTagSpam())){
            // нет сделок - создаем сделку и задачу, если только контакт не помечен как спам

            log.info("opened leads is absent, so creating one");
            AmoCRMLead crmLead = new AmoCRMLead();
            crmLead.setName("Автоматически для " + crmContact.getName());
            crmLead.setResponsible_user_id(apiConfig.getAmocrmDefaultUser());

            Long leadId = amoCRMService.createEntity(crmLead);
            log.info("lead#" + leadId + " created for contact#" + contactAdded.getId());
            crmLead = amoCRMService.getLeadById(leadId);
            crmContact.addLinkedLeadById(leadId);
            amoCRMService.saveByUpdate(crmContact);

            log.info("creating task from lead#" + leadId);
            AmoCRMTask task = new AmoCRMTask();
            task.setLead(crmLead);
            task.setResponsible_user_id(apiConfig.getAmocrmDefaultUser());
            task.setComplete_till(new Date());
            Long taskId = amoCRMService.createEntity(task);
            log.info("task#" + taskId + "created for lead#" + leadId);
        }

        if(crmContact.hasTagById(apiConfig.getAmocrmTagSpam())){
            log.info("Contact#" + contactAdded.getContactId() + " has been marked as SPAM!");
        }

        contactAdded.setState(ContactAdded.State.DONE);
        contactAdded.setDtmUpdate(new Date());
        contactAddedRepository.save(contactAdded);
    }
}
