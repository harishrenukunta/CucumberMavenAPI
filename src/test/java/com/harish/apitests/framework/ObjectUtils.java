package com.harish.apitests.framework;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import ch.qos.logback.core.helpers.Transform;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ObjectUtils {
	public static <T> T getObjectFromXml(final String xmlFragment, Class<T> clazz) {
		try{
			final JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
			return (T)jaxbContext.createUnmarshaller().unmarshal(new InputSource(new StringReader(xmlFragment)));
		} catch(JAXBException ex) {
			ex.printStackTrace();
		}
		return null;		
	}
	
	public static <T> T getObjectFromNode(final Node node, Class<T> clazz) {
		try {
			final StringWriter writer = new StringWriter();
			final Transformer trans = TransformerFactory.newInstance().newTransformer();
			trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			trans.setOutputProperty(OutputKeys.INDENT, "yes");
			final String xmlFragment = writer.toString();
			trans.transform(new DOMSource(node), new StreamResult(writer));
			final JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
			T targetObject = (T)jaxbContext.createUnmarshaller().unmarshal(new InputSource(new StringReader(xmlFragment)));
			return targetObject;
		} catch(TransformerException | JAXBException te) {
			te.printStackTrace();
		}
		return null;
		
	}

}
