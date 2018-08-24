package com.berzellius.integrations.elkarniz.businesslogic.config;

import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public interface APIConfig {
    Integer[] getCallrackingProjectsList();

    String getCalltrackingLogin();

    String getCalltrackingPassword();

    String getCalltrackingWebLogin();

    String getCalltrackingWebPassword();

    String getCalltrackingLoginUrl();

    String getCalltrackingApiUrl();

    String getCalltrackingApiLoginUrl();

    String getAmocrmUser();

    String getAmocrmHash();

    String getAmocrmLoginUrl();

    String getAmocrmApiUrl();

    Integer getAmocrmMaxRelogins();

    ArrayList<Long> getAmocrmLeadsClosedStatuses();

    Long getAmocrmDefaultUser();

    Integer getAmocrmAddedContactProcessDelay();

    Long getAmocrmContactsPhoneNumberCustomField();

    Long getAmocrmContactsPhoneNumberStockField();

    String getAmocrmContactsPhoneNumberEnumWork();

    Long getAmocrmContactsMarketingChannelField();

    Long getAmocrmContactsSourceField();

    Long getAmocrmContactsEmailField();

    String getAmocrmContactsEnumEmail();

    Long getAmocrmLeadsPhoneNumberCustomField();

    Long getAmocrmLeadsMarketingChannelField();

    Long getAmocrmLeadsSourceField();

    Long getAmocrmLeadsCommentField();

    Integer getCalltrackingConditionsExpires();

    Long getAmocrmTagSpam();
}
