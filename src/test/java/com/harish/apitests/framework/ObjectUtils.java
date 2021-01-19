package com.harish.apitests.framework;

import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.assertj.core.util.Arrays;
import org.springframework.util.StringUtils;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.harish.apitests.enums.MethodType;
import com.harish.apitests.framework.utils.DiscNSPrefixMapper;

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
	
	public static String getXmlFromPOJO(final Object pojo, final boolean isXmlFragment) {
		Class<?> clazz = pojo.getClass();
		
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
			final Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new DiscNSPrefixMapper());
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT,  isXmlFragment);
			StringWriter xml = new StringWriter();
			marshaller.marshal(pojo, xml);
			return xml.toString();	
			
		}catch(JAXBException ex) {
			ex.printStackTrace();
		}
		
		return null;
	}
	
	public static XMLGregorianCalendar getXMLGregorianCalendarFromStr(final String dateToConvert) throws ParseException, DatatypeConfigurationException {
		
		final GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(new SimpleDateFormat("yyy-MM-dd").parse(dateToConvert));
		final XMLGregorianCalendar xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		xmlCal.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
		return xmlCal;
		
	}
	
	public static void setObject(Class<?> objectClazz, Object object, final String methodToInvoke, final String valueToAssign) {
		if(Constants.NULL.toString().equals(valueToAssign) || Constants.EMPTY.toString().equals(valueToAssign)) {
			return;
		}
		
		Optional<Object> targetMethod = Arrays.asList(objectClazz.getDeclaredMethods()).stream()
				.filter(m -> ((Method)m).getName().contains("set" + StringUtils.capitalize(methodToInvoke)))
				.findFirst();
		
		if(targetMethod.isPresent()) {
			try {
				final Method method = (Method)targetMethod.get();
				final String paramType = method.getParameterTypes()[0].getSimpleName();
				final Object convertedValue;
				if(method.getParameterTypes()[0].isEnum()) {
					convertedValue = getEnumForValue(method.getParameterTypes()[0], valueToAssign);
				}else if(!paramType.equals("String")) {
					convertedValue = convertParam(valueToAssign, paramType);
				}else {
					convertedValue = valueToAssign;
				}
				method.invoke(object, convertedValue);
			} catch(IllegalAccessException | InvocationTargetException ex) {
				ex.printStackTrace();
				throw new Error(String.format("Error while invoke a method '%s' on object. Please check logs for details", methodToInvoke));
			} 
			
		}else {
			if(objectClazz.getSuperclass() != null) {
				setObject(objectClazz.getSuperclass(), object, methodToInvoke, valueToAssign);
			}
		}
	}
	
	private static Object getEnumForValue(final Class<?> enumType, final String value) {
		final Object o = Arrays.asList(enumType.getEnumConstants()).stream()
				.filter(ev -> ev.toString().equals(value))
				.findFirst()
				.orElse(null);
		return o;
	}
	
	private static Object convertParam(final String paramValue, final String paramTypeToConvert) {
		Object resultValue = null;
		if(paramTypeToConvert.contains("Boolean") || paramTypeToConvert.contains("boolean")) {
			resultValue = Boolean.parseBoolean(paramValue);
		}else if(paramTypeToConvert.contains("BigDecimal")) {
			resultValue = new BigDecimal(paramValue);
		}else if(paramTypeToConvert.equals("byte[]")) {
			resultValue = paramValue.getBytes(Charset.defaultCharset());
		}else if(paramTypeToConvert.contains("XMLGregorianCalendar")) {
			try {
				resultValue = getXMLGregorianCalendarFromStr(paramValue);
			}catch(ParseException | DatatypeConfigurationException ex) {
				ex.printStackTrace();
			}
		}else if(paramTypeToConvert.contains("BigInteger")) {
			resultValue = new BigInteger(paramValue);
		}
		return resultValue;
	}
	
	public static void injectObject(final Object parentPOJO, final String methodToInvoke, final Object objectToAssign) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		final Object method = Arrays.asList(parentPOJO.getClass().getDeclaredMethods()).stream()
				.filter(m -> ((Method)m).getName().contains("set" + StringUtils.capitalize(methodToInvoke)))
				.findFirst()
				.orElseThrow(()->new AssertionError(String.format("Method %s is not found to invoke",  methodToInvoke)));
		
		((Method)method).invoke(parentPOJO, objectToAssign);
		
	}
	
	public static Object createObject(final String classLocation) {
		try {
			final Class<?> objectClazz = Class.forName(classLocation);
			return objectClazz.newInstance();
		
		}catch(InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
			ex.printStackTrace();
			log.error("Error while creating a object of " + classLocation);
		}
		return null;
	}
	
	public static Object getObject(final Object parentPOJO, final String methodToInvoke) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		final Object targetMethod = Arrays.asList(parentPOJO.getClass().getDeclaredMethods()).stream()
				.filter( m -> ((Method)m).getName().contains("get" + StringUtils.capitalize(methodToInvoke)))
				.findFirst()
				.orElseThrow(() -> new AssertionError(String.format("No method found with name '%s'", methodToInvoke)));
		return ((Method)targetMethod).invoke(parentPOJO, methodToInvoke);
		
	}
	
	/*
	public static Method getTargetMethod(final Object targetObject, final MethodType methodType, final String methodNameContains) {
		Object targetMethod = Arrays.asList(targetObject.getClass().getDeclaredMethods()).stream()
				.filter( m -> ((Method)m).getName().startsWith(methodType.getMType()) && ((Method)m).getName().contains(methodNameContains))))
				.findFirst()
				.orElse(null);
		
	   return((Method)targetMethod);
	
	
		return null;}
		*/

}
