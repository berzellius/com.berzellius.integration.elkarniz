package com.berzellius.integrations.elkarniz.controllers.web;

import com.berzellius.integrations.elkarniz.businesslogic.processes.crmevents.CrmEventsService;
import com.berzellius.integrations.elkarniz.businesslogic.processes.websiteevents.WebsiteEventsService;
import com.berzellius.integrations.elkarniz.dto.site.CallRequest;
import com.berzellius.integrations.elkarniz.dto.site.ContactsAddingRequest;
import com.berzellius.integrations.elkarniz.dto.site.LeadRequest;
import com.berzellius.integrations.elkarniz.dto.site.Result;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by berz on 15.06.2016.
 */
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/rest/")
public class RestController extends BaseController {

    @Autowired
    private CrmEventsService crmEventsService;

    @Autowired
    private WebsiteEventsService websiteEventsService;

    @RequestMapping(
            value = "crm_contacts_add",
            method = RequestMethod.POST,
            consumes="application/json",
            produces="application/json"
    )
    @ResponseBody
    public Result newContactAdd(
            @RequestBody
                    ContactsAddingRequest contactsAddingRequest
    )
    {
        return  crmEventsService.newContactsAddedInCRM(contactsAddingRequest);
    }

    @RequestMapping(
            value = "lead_from_site",
            method = RequestMethod.POST,
            consumes="application/json",
            produces="application/json"
    )
    @ResponseBody
    public Result newLeadFromSite(
            @RequestBody
                    LeadRequest leadRequest
    ){
        return websiteEventsService.newLeadsFromWebsite(leadRequest);
    }
}

