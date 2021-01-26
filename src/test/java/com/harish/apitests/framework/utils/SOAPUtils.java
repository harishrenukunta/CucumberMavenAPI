package com.harish.apitests.framework.utils;

import com.harish.apitests.framework.ObjectUtils;
import org.apache.commons.codec.CharEncoding;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Node;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;

public class SOAPUtils {

    private static void addNamespaces(final XMLHolder xmlHolder){
        xmlHolder.addNamespace("ns1", "http://www.dfsdisputes.com_iss162/services/DNDisputesIssuer_162");
        xmlHolder.addNamespace("soap", "http://schemas.xmlsoap.org/soap/envelope/");
    }
    public static <T> T getResponsePOJOFromSOAPResponse(final String response, final Class<T> responseClazz){
        try{
            final XMLHolder xmlHolder = new XMLHolder(response);
            addNamespaces(xmlHolder);
            final Node serviceResponseNode = xmlHolder.getNode("//soap:Body/*[1]");
            return (T) ObjectUtils.getObjectFromNode(serviceResponseNode, responseClazz);

        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T getRequestPOJOFromSOAPRequest(final String request, Class<T> requestClazz){
        try{
            final XMLHolder xmlHolder = new XMLHolder(request);
            addNamespaces(xmlHolder);
            final Node serviceRequestNode = xmlHolder.getNode("//soap:Body/*[1]");
            return (T) ObjectUtils.getObjectFromNode(serviceRequestNode, requestClazz);
        } catch (SAXException | ParserConfigurationException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String messageToString(SOAPMessage message) throws TransformerException {
        final StringWriter sw = new StringWriter();
        final Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, CharEncoding.UTF_8);
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.transform(new DOMSource(message.getSOAPPart()), new StreamResult(sw));
        return sw.toString();

    }


}
