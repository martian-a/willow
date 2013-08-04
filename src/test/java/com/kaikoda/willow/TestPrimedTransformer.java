package com.kaikoda.willow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.XpathEngine;
import org.custommonkey.xmlunit.exceptions.XpathException;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class TestPrimedTransformer {
	
	public static PrimedTransformer transformer = new PrimedTransformer();
	
	@Test
	public void testPrimedTransformer_newDocumentBuilder_configuration() {

		try {
			
			DocumentBuilder result = PrimedTransformer.newDocumentBuilder();
			
			assertEquals(true, result.isNamespaceAware());
			assertEquals(false, result.isValidating());
			assertEquals(true, result.isXIncludeAware());
			
		} catch (ParserConfigurationException e) {					
			fail(e.getMessage());
		}
		
	}
	
	@Test
	public void testPrimedTransformer_parseToDocument_inputFile() {
		
		File xmlFile = new File(TestPrimedTransformer.class.getResource("/data/control/hello_world.xml").getFile());
		assertEquals(true, xmlFile.exists());
		
		try {
			
			Document result = PrimedTransformer.parseToDocument(xmlFile);
			assertTrue(result != null);
			
			assertEquals("document", result.getDocumentElement().getNodeName());
		
			XpathEngine	engine = XMLUnit.newXpathEngine();
			assertEquals("Hello", engine.evaluate("/document/p/span[1]", result));
			assertEquals("!", engine.evaluate("/document/p/text()[position() = last()]", result));
			assertEquals("Hello World!", engine.evaluate("/document/p", result));
			assertEquals(" Hello World! ", engine.evaluate("/document/comment()[1]", result));
			
		} catch (ParserConfigurationException e) {					
			fail(e.getMessage());
		} catch (XpathException e) {	
			fail(e.getMessage() + ": " + e.getCause());
		} catch (SAXException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		}
	
	}
	
	@Test
	public void testPrimedTransformer_parseToDocument_inputString() {
		
		File xmlFile = new File(TestPrimedTransformer.class.getResource("/data/control/hello_world.xml").getFile());
		assertEquals(true, xmlFile.exists());	
		
		try {
			
			String xmlString = FileUtils.readFileToString(xmlFile);
			
			Document result = PrimedTransformer.parseToDocument(xmlString);
			assertTrue(result != null);
			
			assertEquals("document", result.getDocumentElement().getNodeName());
		
			XpathEngine	engine = XMLUnit.newXpathEngine();
			assertEquals("Hello", engine.evaluate("/document/p/span[1]", result));
			assertEquals("!", engine.evaluate("/document/p/text()[position() = last()]", result));
			assertEquals("Hello World!", engine.evaluate("/document/p", result));
			assertEquals(" Hello World! ", engine.evaluate("/document/comment()[1]", result));
			
		} catch (ParserConfigurationException e) {					
			fail(e.getMessage());
		} catch (XpathException e) {	
			fail(e.getMessage() + ": " + e.getCause());
		} catch (SAXException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		}
	
	}	
	
}
