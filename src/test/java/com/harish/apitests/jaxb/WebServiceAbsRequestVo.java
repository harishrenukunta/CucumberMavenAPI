package com.harish.apitests.jaxb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebServiceAbsRequestVo {
    protected Boolean testFlag;
    @XmlElement(required=true)
    protected String messageId;
    @XmlElement(required = true)
    protected UserInfo userInfo;
}
