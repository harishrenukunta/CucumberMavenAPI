package com.harish.apitests.framework.utils;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class FileUtilities {
	
	private static final Logger LOG = LoggerFactory.getLogger(FileUtilities.class);
	
	public static String loadRequestFromFile(String fileName) {
		Resource resource = new ClassPathResource("requestFiles" + File.separator + fileName);
		
		try {
			return readFromInputStream(resource.getInputStream());
		}catch(IOException ex) {
			LOG.error("Error reading file:" + fileName, ex);
		}
		return null;
	}
	
	public static String loadResponseFromFile(String fileName) {
		Resource resource = new ClassPathResource("responseFiles" + File.separator + fileName);
		
		try {
			return readFromInputStream(resource.getInputStream());
		}catch(IOException ex) {
			LOG.error("Error reading file:" + fileName, ex);
		}
		return null;
	}
	
	private static String readFromInputStream(InputStream inputStream) throws IOException{
		StringBuilder resultStringBuilder = new StringBuilder();
		try(BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))){
			String line;
			while((line=br.readLine()) != null) {
				resultStringBuilder.append(line).append("\n");
			}
		}
		return resultStringBuilder.toString();
	}
	
	public static byte[] convertFileToByte(String fileName) {
		Resource resource = new ClassPathResource("inputFiles" + File.separator + fileName);
		try {
			return readByteFromInputStream(resource.getInputStream());
		}
		catch(IOException ex) {
			LOG.error("Error reading file:" + fileName, ex);
		}
		return null;
	}
	
	private static byte[] readByteFromInputStream(InputStream inputStream) throws IOException{
		StringBuilder resultStringBuilder = new StringBuilder();
		try(BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))){
			String line;
			while((line = br.readLine()) != null) {
				resultStringBuilder.append(line).append("\n");
			}
			return resultStringBuilder.toString().getBytes();
		}
	}
	
	
	

}
