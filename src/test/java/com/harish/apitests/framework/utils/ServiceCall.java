package com.harish.apitests.framework.utils;

import com.harish.apitests.enums.ServiceName;
import com.harish.apitests.framework.model.SoapResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Builder
@Data
@Accessors()
public class ServiceCall {
    ServiceName serviceName;
    SoapResponse soapResponse;
}
