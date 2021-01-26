package com.harish.apitests.framework.utils;

import com.harish.apitests.enums.ServiceName;
import com.harish.apitests.framework.model.SoapResponse;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Data
@NoArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
public class ScenarioContext {
    Map<ServiceName, String> serviceCallsRepo = new HashMap<>();
    private ServiceName currentService;

    public void storeSoapResponse(final SoapResponse response){
        serviceCallsRepo.put(currentService, response.getPayload());
    }

    public String getServiceResponseByName(final ServiceName sname){
        return serviceCallsRepo.get(sname);
    }
}
