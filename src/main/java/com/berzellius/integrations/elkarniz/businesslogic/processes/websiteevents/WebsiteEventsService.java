package com.berzellius.integrations.elkarniz.businesslogic.processes.websiteevents;

import com.berzellius.integrations.elkarniz.dto.site.LeadRequest;
import com.berzellius.integrations.elkarniz.dto.site.Result;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public interface WebsiteEventsService {
    Result newLeadsFromWebsite(LeadRequest leadRequest);
}
