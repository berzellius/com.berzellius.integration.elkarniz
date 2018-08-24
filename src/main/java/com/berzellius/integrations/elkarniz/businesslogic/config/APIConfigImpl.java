package com.berzellius.integrations.elkarniz.businesslogic.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@PropertySource("classpath:api.properties")
public class APIConfigImpl implements APIConfig {

    @Value("${api.calltracking.projects}")
    private Integer[] callrackingProjectsList;

    @Value("${api.calltracking.login}")
    private String calltrackingLogin;

    @Value("${api.calltracking.password}")
    private String calltrackingPassword;

    @Value("${api.calltracking.weblogin}")
    private String calltrackingWebLogin;

    @Value("${api.calltracking.webpassword}")
    private String calltrackingWebPassword;

    @Value("${api.calltracking.login_url}")
    private String calltrackingLoginUrl;

    @Value("${api.calltracking.api_url}")
    private String calltrackingApiUrl;

    @Value("${api.calltracking.api_login_url}")
    private String calltrackingApiLoginUrl;

    @Value("${api.calltracking.conditions_expires}")
    private Integer calltrackingConditionsExpires;

    @Value("${api.amocrm.user}")
    private String amocrmUser;

    @Value("${api.amocrm.hash}")
    private String amocrmHash;

    @Value("${api.amocrm.login_url}")
    private String amocrmLoginUrl;

    @Value("${api.amocrm.api_url}")
    private String amocrmApiUrl;

    @Value("${api.amocrm.max_relogins}")
    private Integer amocrmMaxRelogins;

    @Value("${api.amocrm.lead_closed_statuses}")
    private ArrayList<Long> amocrmLeadsClosedStatuses;

    @Value("${api.amocrm.default_user}")
    private Long amocrmDefaultUser;

    @Value("${api.amocrm.tags.spam}")
    private Long amocrmTagSpam;

    @Value("${api.amocrm.businesslogic.added_contact_process_delay}")
    private Integer amocrmAddedContactProcessDelay;

    @Value("${api.amocrm.fields.contacts.phone_number_custom}")
    private Long amocrmContactsPhoneNumberCustomField;

    @Value("${api.amocrm.fields.contacts.phone_number_stock}")
    private Long amocrmContactsPhoneNumberStockField;

    @Value("${api.amocrm.fields.contacts.phone_enum_work}")
    private String amocrmContactsPhoneNumberEnumWork;

    @Value("${api.amocrm.fields.contacts.marketing_channel}")
    private Long amocrmContactsMarketingChannelField;

    @Value("${api.amocrm.fields.contacts.source}")
    private Long amocrmContactsSourceField;

    @Value("${api.amocrm.fields.contacts.email}")
    private Long amocrmContactsEmailField;

    @Value("${api.amocrm.fields.contacts.enum_email}")
    private String amocrmContactsEnumEmail;

    @Value("${api.amocrm.fields.leads.phone_number_custom}")
    private Long amocrmLeadsPhoneNumberCustomField;

    @Value("${api.amocrm.fields.leads.marketing_channel}")
    private Long amocrmLeadsMarketingChannelField;

    @Value("${api.amocrm.fields.leads.source}")
    private Long amocrmLeadsSourceField;

    @Value("${api.amocrm.fields.leads.comment}")
    private Long amocrmLeadsCommentField;

    @Override
    public Integer[] getCallrackingProjectsList() {
        return callrackingProjectsList;
    }

    @Override
    public String getCalltrackingLogin() {
        return calltrackingLogin;
    }

    @Override
    public String getCalltrackingPassword() {
        return calltrackingPassword;
    }

    @Override
    public String getCalltrackingWebLogin() {
        return calltrackingWebLogin;
    }

    @Override
    public String getCalltrackingWebPassword() {
        return calltrackingWebPassword;
    }

    @Override
    public String getCalltrackingLoginUrl() {
        return calltrackingLoginUrl;
    }

    @Override
    public String getCalltrackingApiUrl() {
        return calltrackingApiUrl;
    }

    @Override
    public String getCalltrackingApiLoginUrl() {
        return calltrackingApiLoginUrl;
    }

    @Override
    public String getAmocrmUser() {
        return amocrmUser;
    }

    @Override
    public String getAmocrmHash() {
        return amocrmHash;
    }

    @Override
    public String getAmocrmLoginUrl() { return amocrmLoginUrl; }

    @Override
    public String getAmocrmApiUrl() {
        return amocrmApiUrl;
    }

    @Override
    public Integer getAmocrmMaxRelogins() {
        return amocrmMaxRelogins;
    }

    @Override
    public ArrayList<Long> getAmocrmLeadsClosedStatuses() {
        return amocrmLeadsClosedStatuses;
    }

    @Override
    public Long getAmocrmDefaultUser() {
        return amocrmDefaultUser;
    }

    @Override
    public Integer getAmocrmAddedContactProcessDelay() {
        return amocrmAddedContactProcessDelay;
    }

    @Override
    public Long getAmocrmContactsPhoneNumberCustomField() { return amocrmContactsPhoneNumberCustomField; }

    @Override
    public Long getAmocrmContactsPhoneNumberStockField() { return amocrmContactsPhoneNumberStockField; }

    @Override
    public String getAmocrmContactsPhoneNumberEnumWork() { return amocrmContactsPhoneNumberEnumWork; }

    @Override
    public Long getAmocrmContactsMarketingChannelField() { return amocrmContactsMarketingChannelField; }

    @Override
    public Long getAmocrmContactsSourceField() { return amocrmContactsSourceField; }

    @Override
    public Long getAmocrmContactsEmailField() { return amocrmContactsEmailField; }

    @Override
    public String getAmocrmContactsEnumEmail() { return amocrmContactsEnumEmail; }

    @Override
    public Long getAmocrmLeadsPhoneNumberCustomField() { return amocrmLeadsPhoneNumberCustomField; }

    @Override
    public Long getAmocrmLeadsMarketingChannelField() { return amocrmLeadsMarketingChannelField; }

    @Override
    public Long getAmocrmLeadsSourceField() { return amocrmLeadsSourceField; }

    @Override
    public Long getAmocrmLeadsCommentField() { return amocrmLeadsCommentField; }

    @Override
    public Integer getCalltrackingConditionsExpires() { return calltrackingConditionsExpires; }

    @Override
    public Long getAmocrmTagSpam() { return amocrmTagSpam; }
}
