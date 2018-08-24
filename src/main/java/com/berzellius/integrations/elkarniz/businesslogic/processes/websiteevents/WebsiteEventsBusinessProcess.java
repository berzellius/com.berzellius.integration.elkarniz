package com.berzellius.integrations.elkarniz.businesslogic.processes.websiteevents;

import com.berzellius.integrations.basic.exception.APIAuthException;
import com.berzellius.integrations.elkarniz.dmodel.LeadFromSite;
import org.springframework.stereotype.Service;

@Service
public interface WebsiteEventsBusinessProcess {
    void processLeadFromSite(LeadFromSite leadFromSite) throws APIAuthException;
}
