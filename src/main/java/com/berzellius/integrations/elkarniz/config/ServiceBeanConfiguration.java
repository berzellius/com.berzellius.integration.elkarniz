package com.berzellius.integrations.elkarniz.config;


import com.berzellius.integrations.amocrmru.dto.ErrorHandlers.AmoCRMAPIRequestErrorHandler;
import com.berzellius.integrations.amocrmru.service.AmoCRMService;
import com.berzellius.integrations.amocrmru.service.AmoCRMServiceImpl;
import com.berzellius.integrations.calltrackingru.dto.api.errorhandlers.CalltrackingAPIRequestErrorHandler;
import com.berzellius.integrations.elkarniz.businesslogic.config.APIConfig;
import com.berzellius.integrations.elkarniz.businesslogic.config.APIConfigImpl;
import com.berzellius.integrations.elkarniz.businesslogic.processes.crmevents.CrmEventsBusinessProcess;
import com.berzellius.integrations.elkarniz.businesslogic.processes.crmevents.CrmEventsBusinessProcessImpl;
import com.berzellius.integrations.elkarniz.businesslogic.processes.crmevents.CrmEventsService;
import com.berzellius.integrations.elkarniz.businesslogic.processes.crmevents.CrmEventsServiceImpl;
import com.berzellius.integrations.elkarniz.businesslogic.processes.websiteevents.WebsiteEventsBusinessProcess;
import com.berzellius.integrations.elkarniz.businesslogic.processes.websiteevents.WebsiteEventsBusinessProcessImpl;
import com.berzellius.integrations.elkarniz.businesslogic.processes.websiteevents.WebsiteEventsService;
import com.berzellius.integrations.elkarniz.businesslogic.processes.websiteevents.WebsiteEventsServiceImpl;
import com.berzellius.integrations.elkarniz.businesslogic.rules.transformer.FieldsTransformer;
import com.berzellius.integrations.elkarniz.businesslogic.rules.transformer.FieldsTransformerImpl;
import com.berzellius.integrations.elkarniz.businesslogic.rules.validator.*;
import com.berzellius.integrations.service.CallTrackingAPIService;
import com.berzellius.integrations.service.CallTrackingAPIServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by berz on 14.08.18.
 */
@Configuration
public class ServiceBeanConfiguration {

    @Bean
    BusinessRulesValidator businessRulesValidator(){
        return new BusinessRulesValidatorImpl();
    }

    @Bean
    FieldsTransformer fieldsTransformer(){
        return new FieldsTransformerImpl();
    }

    @Bean
    CrmEventsBusinessProcess crmEventsBusinessProcess() {
        return new CrmEventsBusinessProcessImpl();
    }

    @Bean
    CrmEventsService crmEventsService() {
        return new CrmEventsServiceImpl();
    }

    @Bean
    WebsiteEventsService websiteEventsService() { return new WebsiteEventsServiceImpl(); }

    @Bean
    WebsiteEventsBusinessProcess websiteEventsBusinessProcess() { return new WebsiteEventsBusinessProcessImpl(); }

    @Bean
    LeadFromSiteValidationUtil leadFromSiteValidationUtil() { return new LeadFromSiteValidationUtilImpl(); }

    @Bean
    SimpleFieldsValidationUtil simpleFieldsValidationUtil() { return new SimpleFieldValidationUtilImpl(); }

    @Bean
    APIConfig apiConfig(){
        return new APIConfigImpl();
    }

    /**
     * нужно внедрить ConversionService, чтобы из .properties читались массивы (через запятую)
     * @return ConversionService
     */
    @Bean public ConversionService conversionService() {
        return new DefaultConversionService();
    }

    @Bean
    public CallTrackingAPIService callTrackingAPIService(){
        CallTrackingAPIService callTrackingAPIService = new CallTrackingAPIServiceImpl();

        callTrackingAPIService.setApiMethod(HttpMethod.POST);
        callTrackingAPIService.setApiURL(apiConfig().getCalltrackingApiUrl());

        callTrackingAPIService.setLoginURL(apiConfig().getCalltrackingApiLoginUrl());
        callTrackingAPIService.setLoginMethod(HttpMethod.POST);

        callTrackingAPIService.setLogin(apiConfig().getCalltrackingLogin());
        callTrackingAPIService.setPassword(apiConfig().getCalltrackingPassword());
        callTrackingAPIService.setWebSiteLogin(apiConfig().getCalltrackingWebLogin());
        callTrackingAPIService.setWebSitePassword(apiConfig().getCalltrackingWebPassword());
        callTrackingAPIService.setWebSiteLoginUrl(apiConfig().getCalltrackingLoginUrl());
        callTrackingAPIService.setProjects(apiConfig().getCallrackingProjectsList());
        callTrackingAPIService.setMarketingChannelsConditionsStoreExpiration(apiConfig().getCalltrackingConditionsExpires());

        CalltrackingAPIRequestErrorHandler errorHandler = new CalltrackingAPIRequestErrorHandler();
        callTrackingAPIService.setErrorHandler(errorHandler);

        return callTrackingAPIService;
    }

    @Bean
    AmoCRMService amoCRMService(){
        AmoCRMService amoCRMService = new AmoCRMServiceImpl();
        amoCRMService.setApiBaseUrl(apiConfig().getAmocrmApiUrl());
        amoCRMService.setLoginUrl(apiConfig().getAmocrmLoginUrl());
        amoCRMService.setUserHash(apiConfig().getAmocrmHash());
        amoCRMService.setUserLogin(apiConfig().getAmocrmUser());
        amoCRMService.setLeadClosedStatusesIDs(apiConfig().getAmocrmLeadsClosedStatuses());
        amoCRMService.setMaxRelogins(apiConfig().getAmocrmMaxRelogins());

        AmoCRMAPIRequestErrorHandler errorHandler = new AmoCRMAPIRequestErrorHandler();
        amoCRMService.setErrorHandler(errorHandler);

        return amoCRMService;
    }
}
