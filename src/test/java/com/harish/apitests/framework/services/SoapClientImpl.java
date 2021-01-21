package com.harish.apitests.framework.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.time.LocalDateTime;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.github.dockerjava.zerodep.shaded.org.apache.commons.codec.CharEncoding;
import com.harish.apitests.framework.model.SoapRequest;
import com.harish.apitests.framework.model.SoapResponse;

import io.cucumber.java.Scenario;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
@Data
public class SoapClientImpl implements SoapClient {

	private final ServiceUrl serviceUrl;
	private final Environment env;
	private Scenario scenario;

	@Override
	public SoapResponse send(SoapRequest request) throws MalformedURLException, SOAPException {
		// TODO Auto-generated method stub
		return sendToServer(request);
	}

	private SoapResponse sendToServer(SoapRequest request) throws MalformedURLException, SOAPException {
		String soapServiceUrl = serviceUrl.soapServerAddress();
		log.debug("Sending envelope to url: {} \n{}, ", soapServiceUrl, request.getEnvelope());
		SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
		SOAPConnection soapConnection = soapConnectionFactory.createConnection();

		String PROXY_ADDRESS = "proxy.discoverfinancial.com";
		int PROXY_PORT = 8080;
		Socket socket = new Socket();
		SocketAddress sockaddr = new InetSocketAddress(PROXY_ADDRESS, PROXY_PORT);

		try {
			int socketTimeout = Integer.parseInt(env.getProperty("connection.time.out")) * 1000;
			socket.connect(sockaddr, socketTimeout);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(socket.getInetAddress(), PROXY_PORT));

		URL endpoint = new URL(null, soapServiceUrl, new URLStreamHandler() {

			protected URLConnection openConnection(URL url) throws IOException {
				// This url is the parent of this stream handler, so must
				// create clone
				URL clone = new URL(url.toString());
				if (soapServiceUrl.equals(
						"http://localhost:8080/DisputesWebServiceWeb/services/DNDisputesIssuerServicesSOAPExt_162")) {
					URLConnection connection = null;
					connection = clone.openConnection();
					return connection;
				} else {
					URLConnection connection = null;
					connection = clone.openConnection(proxy);
					connection.setConnectTimeout(Integer.parseInt(env.getProperty("connection.time.out")) * 1000);
					connection.setReadTimeout(Integer.parseInt(env.getProperty("connection.time.out")) * 1000);
					return connection;
				}
			}

		});

		try {
			log.info("Before soap call:" + LocalDateTime.now());
			this.scenario.log(String.format("Request sent to %s at '%s' :- \n SOAP Request: \n%s",
					serviceUrl.soapServerAddress(), LocalDateTime.now(), request.getEnvelope()));

			SOAPMessage response = soapConnection.call(soapMessage(request), endpoint);

			soapConnection.close();
			String payload = messageToString(response);
			String httpStatusCode = httpStatusCode(response);

			log.debug("Soap httpstatusCode: {}, response \n{}", httpStatusCode, payload);
			scenario.log(String.format("SOAP Response at '%s' : \nHttpStatusCode: %s \nEnvelope:%s", httpStatusCode,
					LocalDateTime.now()));

			return new SoapResponse(httpStatusCode, payload);
		} catch (Exception ex) {
			log.error("Printing stack trace");
			soapConnection.close();
			throw new Error("Error when call soap service :" + ex.getMessage());
			// e.printStackTrace();
			// return null;
		} finally {
			log.info("After soap call:" + LocalDateTime.now());
			try {
				Float sleepTime = Float.parseFloat(env.getProperty("soap.sleep.time")) * 1000;
				Thread.sleep(sleepTime.longValue());
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}

	private SOAPMessage soapMessage(final SoapRequest request) throws SOAPException, IOException {
		String envelope = request.getEnvelope();
		InputStream inputStream = new ByteArrayInputStream(envelope.getBytes());
		return javax.xml.soap.MessageFactory.newInstance().createMessage(null, inputStream);

	}

	private String messageToString(SOAPMessage message)
			throws TransformerFactoryConfigurationError, TransformerException {
		final StringWriter sw = new StringWriter();
		final Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, CharEncoding.UTF_8);
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.transform(new DOMSource(message.getSOAPPart()), new StreamResult(sw));
		return sw.toString();

	}

	private String httpStatusCode(SOAPMessage message) throws SOAPException {
		SOAPBody body = message.getSOAPBody();
		if (body.hasFault())
			return body.getFault().getFaultCode();
		else
			return "200";
	}

}
