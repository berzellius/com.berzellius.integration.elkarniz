package com.berzellius.integrations.elkarniz.businesslogic.processes.websiteevents;

import com.berzellius.integrations.basic.exception.APIAuthException;
import com.berzellius.integrations.elkarniz.dmodel.LeadFromSite;
import com.berzellius.integrations.elkarniz.dmodel.Site;
import com.berzellius.integrations.elkarniz.dto.site.Lead;
import com.berzellius.integrations.elkarniz.dto.site.LeadRequest;
import com.berzellius.integrations.elkarniz.dto.site.Result;
import com.berzellius.integrations.elkarniz.repository.LeadFromSiteRepository;
import com.berzellius.integrations.elkarniz.repository.SiteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

@Service
public class WebsiteEventsServiceImpl implements WebsiteEventsService {
    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private LeadFromSiteRepository leadFromSiteRepository;

    @Autowired
    private WebsiteEventsBusinessProcess websiteEventsBusinessProcess;

    private static final Logger log = LoggerFactory.getLogger(WebsiteEventsService.class);

    @Override
    public Result newLeadsFromWebsite(LeadRequest leadRequest) {
        Assert.notNull(leadRequest);
        Assert.notNull(leadRequest.getOrigin());
        Assert.notNull(leadRequest.getPassword());

        String url = leadRequest.getOrigin();
        String password = leadRequest.getPassword();

        List<Site> sites = siteRepository.findByUrlAndPassword(url, password);
        if(sites.size() == 0){
            return new Result("error");
        }

        Site site = sites.get(0);

        List<LeadFromSite> leadFromSiteList = new ArrayList<>();
        for(Lead lead : leadRequest.getLeads()){
            LeadFromSite leadFromSite = new LeadFromSite();
            leadFromSite.setDtmCreate(new Date());
            leadFromSite.setSite(site);
            leadFromSite.setLead(lead);
            leadFromSite.setState(LeadFromSite.State.NEW);

            leadFromSiteList.add(leadFromSite);
        }

        leadFromSiteRepository.save(leadFromSiteList);

        this.runProcessingNewLeadsFromSite();
        return new Result("success");
    }

    @Scheduled(fixedDelay = 60000)
    protected void runProcessingNewLeadsFromSite(){
        leadFromSiteRepository.findByState(LeadFromSite.State.NEW).forEach(
                new Consumer<LeadFromSite>() {
                    @Override
                    public void accept(LeadFromSite leadFromSite) {
                        try {
                            websiteEventsBusinessProcess.processLeadFromSite(leadFromSite);
                        } catch (APIAuthException e) {

                            log.error(
                                    "Authentification error while processing LeadFromSite:" + leadFromSite.getLead().toString() +
                                            "/id=" + leadFromSite.getId() + " :: " + e.getMessage());
                        } catch (RuntimeException e) {

                            log.error("Runtime Exception while processing  LeadFromSite:" + leadFromSite.getLead().toString() +
                                    "id=" + leadFromSite.getId() + " :: " + e.getMessage());
                        } finally {
                            LeadFromSite leadFromSite1 = leadFromSiteRepository.findOne(leadFromSite.getId());
                            leadFromSite1.setLastProcessed(new Date());
                            leadFromSiteRepository.save(leadFromSite1);
                        }
                    }
                }
        );
    }
}
