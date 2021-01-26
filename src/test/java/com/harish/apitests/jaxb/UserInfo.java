package com.harish.apitests.jaxb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserInfo {

    @XmlElement(required=true)
    protected String userId;
    @XmlElement(required = true)
    protected String userName;
    @XmlSchemaType(name="integer")
    protected int memberId;
    @XmlElement(required = true)
    protected String roleClass;
    protected String userDepartmentName;
    protected String userEmployerName;
    protected String userLocation;
    protected String userCenterCode;
}
