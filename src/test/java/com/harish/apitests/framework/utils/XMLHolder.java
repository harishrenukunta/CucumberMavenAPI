package com.harish.apitests.framework.utils;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Node;
import javax.xml.xpath.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

@Slf4j
public class XMLHolder {
    private XPath xpath;
    private Document doc;
    private NamespaceResolver nsResolver;

    public XMLHolder(final String response) throws IOException, SAXException, ParserConfigurationException {
        parse(response);
        xpath = XPathFactory.newInstance().newXPath();
        nsResolver = new NamespaceResolver(doc);
        xpath.setNamespaceContext(nsResolver);
    }

    private void parse(final String response) throws ParserConfigurationException, IOException, SAXException {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();

        StringBuilder xmlStringBuilder = new StringBuilder(response);
        ByteArrayInputStream input = new ByteArrayInputStream(
               xmlStringBuilder.toString().getBytes("UTF-8")
        );
        doc = builder.parse(new InputSource(new StringReader(response)));
    }

    public NodeList getNodes(final String xpathExpression) throws XPathExpressionException {
        final NodeList nodelist = (NodeList) xpath.compile(xpathExpression).evaluate(doc, XPathConstants.NODESET);
        return nodelist;
    }

    public Node getNode(final String xpathExpression){
        try{
            XPathExpression expr = xpath.compile(xpathExpression);
            return (Node)expr.evaluate(doc, XPathConstants.NODE);

        } catch (XPathExpressionException e) {
            e.printStackTrace();
            throw new Error("Threw while evaluating xpath:", e);
        }
    }

    public void addNamespace(String ns, String nsUri) {nsResolver.addNamespace(ns, nsUri);}

    public String getNodeValue(final String xpathExpression){
        try{
            XPathExpression expr = xpath.compile(xpathExpression);
            return expr.evaluate(doc, XPathConstants.STRING).toString();
        } catch (XPathExpressionException ex) {
            throw new Error("Error while evaluating xpath : " + xpathExpression + ":" + ex.getMessage());
        }
    }
}
