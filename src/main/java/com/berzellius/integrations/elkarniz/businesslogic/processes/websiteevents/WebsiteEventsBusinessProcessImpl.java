package com.berzellius.integrations.elkarniz.businesslogic.processes.websiteevents;

import com.berzellius.integrations.amocrmru.dto.api.amocrm.AmoCRMContact;
import com.berzellius.integrations.amocrmru.dto.api.amocrm.AmoCRMLead;
import com.berzellius.integrations.amocrmru.service.AmoCRMService;
import com.berzellius.integrations.basic.exception.APIAuthException;
import com.berzellius.integrations.calltrackingru.dto.api.calltracking.CallTrackingSourceCondition;
import com.berzellius.integrations.elkarniz.businesslogic.config.APIConfig;
import com.berzellius.integrations.elkarniz.businesslogic.rules.transformer.FieldsTransformer;
import com.berzellius.integrations.elkarniz.businesslogic.rules.validator.BusinessRulesValidator;
import com.berzellius.integrations.elkarniz.dmodel.LeadFromSite;
import com.berzellius.integrations.elkarniz.dto.site.Lead;
import com.berzellius.integrations.elkarniz.repository.LeadFromSiteRepository;
import com.berzellius.integrations.service.CallTrackingAPIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.function.Function;

@Service
public class WebsiteEventsBusinessProcessImpl implements WebsiteEventsBusinessProcess {
    private static final Logger log = LoggerFactory.getLogger(WebsiteEventsBusinessProcess.class);

    @Autowired
    private FieldsTransformer fieldsTransformer;

    @Autowired
    private BusinessRulesValidator businessRulesValidator;

    @Autowired
    private LeadFromSiteRepository leadFromSiteRepository;

    @Autowired
    private CallTrackingAPIService callTrackingAPIService;

    @Autowired
    private AmoCRMService amoCRMService;

    @Autowired
    private APIConfig apiConfig;

    @Override
    public void processLeadFromSite(LeadFromSite leadFromSite) throws APIAuthException {
        Assert.notNull(leadFromSite);
        Assert.notNull(leadFromSite.getSite());
        Assert.notNull(leadFromSite.getLead());

        log.info("Started processing lead from site " + leadFromSite.getSite().getUrl() +
                "; contacts: " + leadFromSite.getLead().getPhone() + " / " + leadFromSite.getLead().getEmail());

        if(businessRulesValidator.validate(leadFromSite)){
            log.info("Lead from site validated: " + leadFromSite.getSite().getUrl() +
                    "; contacts: " + leadFromSite.getLead().getPhone() + " / " + leadFromSite.getLead().getEmail());

            String phone = this.getPhoneNumberFromLead(leadFromSite.getLead());
            String email = this.getEmailFromLead(leadFromSite.getLead());
            String source = this.getSourceFromLeadFromSite(leadFromSite);

            AmoCRMContact crmContact = this.seekContact(leadFromSite.getSite().getUrl(), phone, email, source, leadFromSite.getSite().getCrmContactSourceId());

            // ищем связанные сделки
            List<AmoCRMLead> crmLeads = amoCRMService.getLeadsLinkedToContact(
                    crmContact,
                    new Function<AmoCRMLead, Boolean>() {
                        @Override
                        public Boolean apply(AmoCRMLead amoCRMLead) {
                            // не учитываем закрытые сделки
                            return !apiConfig.getAmocrmLeadsClosedStatuses().contains(amoCRMLead.getStatus_id());
                        }
                    }
            );

            if(crmLeads.size() == 0){
                this.createLead(crmContact, leadFromSite.getLead().getComment(), source, leadFromSite.getSite().getCrmLeadSourceId());
            }

            leadFromSite.setState(LeadFromSite.State.DONE);
            leadFromSiteRepository.save(leadFromSite);
        }
        else{
            log.info("Lead from site NOT validated: " + leadFromSite.getSite().getUrl() +
                    "; contacts: " + leadFromSite.getLead().getPhone() + " / " + leadFromSite.getLead().getEmail());
        }
    }

    protected void createLead(AmoCRMContact crmContact, String comment, String source, String crmLeadSourceId) throws APIAuthException {
        AmoCRMLead amoCRMLead = new AmoCRMLead();
        amoCRMLead.setName("Автоматически -> " + crmContact.getName());
        amoCRMLead.setResponsible_user_id(apiConfig.getAmocrmDefaultUser());

        if(comment != null) {
            amoCRMLead.addStringValueToCustomField(
                    apiConfig.getAmocrmLeadsCommentField(), comment);
        }

        if(source != null) {
            amoCRMLead.addStringValueToCustomField(
                    apiConfig.getAmocrmLeadsMarketingChannelField(), source);
        }

        if(crmLeadSourceId != null) {
            amoCRMLead.addStringValueToCustomField(
                    apiConfig.getAmocrmLeadsSourceField(), crmLeadSourceId);
        }

        Long leadId = amoCRMService.createEntity(amoCRMLead);
        log.info("created AmoCRMLead#" + leadId);
        amoCRMService.addContactToLead(crmContact, amoCRMService.getLeadById(leadId));
    }

    protected AmoCRMContact seekContact(String url, String phone, String email, String source, String crmContactSourceId) throws APIAuthException {
        String query = (phone == null)? "" : phone;
        if(email != null) {
            query = query + (query.equals("") ? email : (" " + email));
        }

        List<AmoCRMContact> crmContacts = amoCRMService.getContactsByQuery(query);

        if(crmContacts == null || crmContacts.size() == 0){
            log.info("contacts not found by query:" + query + "; new contacts needs to be created");
            return this.createContact(url, phone, email, source, query, crmContactSourceId);
        }
        else return crmContacts.get(0);
    }

    protected AmoCRMContact createContact(String url, String phone, String email, String source, String query, String crmContactSourceId) throws APIAuthException {
        AmoCRMContact crmContact = new AmoCRMContact();
        crmContact.setName("заявка с сайта " + url + " от " + query);

        if(phone != null) {
            crmContact.addStringValueToCustomField(
                    apiConfig.getAmocrmContactsPhoneNumberCustomField(), phone);
            crmContact.addStringValueToCustomField(
                    apiConfig.getAmocrmContactsPhoneNumberStockField(), phone, apiConfig.getAmocrmContactsPhoneNumberEnumWork());
        }

        if(email != null) {
            crmContact.addStringValueToCustomField(
                    apiConfig.getAmocrmContactsEmailField(), phone);
        }

        if(source != null) {
            crmContact.addStringValueToCustomField(
                    apiConfig.getAmocrmContactsMarketingChannelField(), source);
        }

        if(crmContactSourceId != null){
            crmContact.addStringValueToCustomField(
                    apiConfig.getAmocrmContactsSourceField(), crmContactSourceId
            );
        }

        crmContact.setResponsible_user_id(apiConfig.getAmocrmDefaultUser());
        Long amoCRMContactId = amoCRMService.createEntity(crmContact);
        log.info("AmoCRMContact#" + amoCRMContactId + " created");

        return amoCRMService.getContactById(amoCRMContactId);
    }

    protected String getSourceFromLeadFromSite(LeadFromSite leadFromSite) throws APIAuthException {
        Assert.notNull(leadFromSite.getSite().getCallTrackingProjectId());

        String utmSource = (leadFromSite.getLead().getUtm_source() != null)? leadFromSite.getLead().getUtm_source() : "";
        String utmMedium = (leadFromSite.getLead().getUtm_medium() != null)? leadFromSite.getLead().getUtm_medium() : "";
        String utmCampaign = (leadFromSite.getLead().getUtm_campaign() != null)? leadFromSite.getLead().getUtm_campaign() : "";

        log.info("utm = {" + utmSource + ", " + utmMedium + ", " + utmCampaign + "}, project = " + leadFromSite.getSite().getCallTrackingProjectId());
        CallTrackingSourceCondition callTrackingSourceCondition = callTrackingAPIService.getCallTrackingSourceConditionByUtmAndProjectId(
                leadFromSite.getSite().getCallTrackingProjectId(),
                utmSource, utmMedium, utmCampaign
                );

        if(callTrackingSourceCondition == null)
            return null;

        return callTrackingSourceCondition.getSourceName();
    }

    protected String getEmailFromLead(Lead lead) {
        return lead.getEmail();
    }

    protected String getPhoneNumberFromLead(Lead lead) {
        if(lead.getPhone() == null)
            return null;

        String phone = lead.getPhone();
        phone = fieldsTransformer.transform(phone, FieldsTransformer.Transformation.CALL_NUMBER_COMMON);
        phone = fieldsTransformer.transform(phone, FieldsTransformer.Transformation.CALL_NUMBER_LEADING_7);
        return phone;
    }

    protected String getCommentFromLead(Lead lead){
        return lead.getComment();
    }
}
