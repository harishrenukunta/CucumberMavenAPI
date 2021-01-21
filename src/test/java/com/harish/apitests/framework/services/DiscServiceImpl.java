package com.harish.apitests.framework.services;


import com.harish.apitests.enums.ServiceName;
import io.cucumber.java.Scenario;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Data
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DiscServiceImpl implements DiscService {

    @Setter
    Scenario scenario;
    final Environment env;

    @Value("${test.maxRetry.attempt}")
    private int maxRetryAttempt;

    final static String BUILDER_CLASS_PACKAGE = "com.disc.sit.webservices.model.builders.";

    String pwd = null;


    @Override
    public void callServiceWithRequestPOJO(Object requestPOJO, Map<String, String> headerData) {

    }

    @Override
    public Object createRequestPOJO(ServiceName serviceName, Map<String, String> requestData) {
        return null;
    }

    @Override
    public Map<String, String> resolveRequestParamsFromPreviousCalls(Map<String, String> requestData) {
        return null;
    }
}
