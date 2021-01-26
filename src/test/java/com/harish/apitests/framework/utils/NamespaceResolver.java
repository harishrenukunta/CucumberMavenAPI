package com.harish.apitests.framework.utils;

import org.w3c.dom.Document;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NamespaceResolver implements NamespaceContext {
    private Document sourceDocument;
    private Map<String, String> nameSpaces = new HashMap<>();

    public NamespaceResolver(Document document){ sourceDocument = document;}

    public String getNamespaceURI(String prefix){
        if(prefix.equals(XMLConstants.DEFAULT_NS_PREFIX)){
            return sourceDocument.lookupNamespaceURI(null);
        }else{
            return nameSpaces.get(prefix);
        }
    }

    public String getPrefix(String namespaceURI) { return null;}

    public Iterator getPrefixes(String namespaceURI){ return null;}

    public void addNamespace(String ns, String nsUri){ nameSpaces.put(ns, nsUri);}

}
