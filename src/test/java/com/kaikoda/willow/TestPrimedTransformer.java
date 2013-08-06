package com.kaikoda.willow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.XpathEngine;
import org.custommonkey.xmlunit.exceptions.XpathException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class TestPrimedTransformer {
	
	public static PrimedTransformer transformer = new PrimedTransformer();
	public static File sampleFile;
	public static String sampleString;
	public static File xslReverse;
	
	@BeforeClass
	public static void setupOnce() throws IOException {
		
		sampleFile = new File(TestPrimedTransformer.class.getResource("/data/control/hello_world.xml").getFile());
		sampleString = FileUtils.readFileToString(sampleFile);
		xslReverse = new File(TestPrimedTransformer.class.getResource("/xsl/reverse.xsl").getFile());
		
	}
	
	@Before
	public void setup() {
		
		assertEquals(true, sampleFile.exists());
		assertEquals(true, sampleString != null);
		
	}
	
	@Test
	public void testPrimedTransformer_newDocumentBuilder_configuration() {

		try {
			
			DocumentBuilder result = PrimedTransformer.newDocumentBuilder();
			
			assertEquals(true, result.isNamespaceAware());
			assertEquals(false, result.isValidating());
			assertEquals(false, result.isXIncludeAware());
			
		} catch (ParserConfigurationException e) {					
			fail(e.getMessage());
		}
		
	}
	
	@Test
	public void testPrimedTransformer_parseToDocument_inputFile() {		
		
		try {
			
			Document result = PrimedTransformer.parseToDocument(sampleFile);
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
					
		try {		
			
			Document result = PrimedTransformer.parseToDocument(sampleString);
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
	public void testPrimedTransformer_transform_inputXSL_null() {
					
		try {					
			
			// Prepare an XML String for transformation
			Source xmlSource = new DOMSource(PrimedTransformer.parseToDocument(sampleString));
			
			// Prepare a container to hold the result of the transformation
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			
			// Execute the transformation
			PrimedTransformer.transform(xmlSource, null, result, null, null);		
			
			// Extract the result of the transformation from the container
			String resultString = writer.toString();

			XMLUnit.setIgnoreWhitespace(true);
			
			assertXMLEqual(sampleString, resultString);		
			
		} catch (ParserConfigurationException e) {					
			fail(e.getMessage());
		} catch (SAXException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		} catch (TransformerException e) {
			fail(e.getMessage());
		}
	
	}
	
	@Test
	public void testPrimedTransformer_transform_inputXSL_reverse() {
					
		try {					
			
			// Prepare an XML String for transformation
			Source xmlSource = new DOMSource(PrimedTransformer.parseToDocument(sampleString));
			
			// Prepare an XSLT Stylesheet to implement the transformation
			Source xslSource = new DOMSource(PrimedTransformer.parseToDocument(xslReverse));
			
			// Prepare a container to hold the result of the transformation
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			
			// Prepare the control content
			String xmlExpected = FileUtils.readFileToString(new File(TestPrimedTransformer.class.getResource("/data/control/hello_world_reversed.xml").getFile()));
			
			// Execute the transformation
			PrimedTransformer.transform(xmlSource, xslSource, result, null, null);		
			
			// Extract the result of the transformation from the container
			String resultString = writer.toString();

			XMLUnit.setIgnoreWhitespace(true);
			
			assertXMLEqual(xmlExpected, resultString);		
			
		} catch (ParserConfigurationException e) {					
			fail(e.getMessage());
		} catch (SAXException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		} catch (TransformerException e) {
			fail(e.getMessage());
		}
	
	}

	@Test
	public void testPrimedTransformer_parseToString_inputFile() {		
		
		try {		
			
			String result = PrimedTransformer.parseToString(sampleFile);
			assertTrue(result != null);
			
			assertXMLEqual(sampleString, result);
			
		} catch (ParserConfigurationException e) {					
			fail(e.getMessage());
		} catch (SAXException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		} catch (TransformerException e) {
			fail(e.getMessage());
		}
	
	}
	
	@Test
	public void testPrimedTransformer_parseToString_inputDocument() {		
		
		try {		
			
			Document sampleDocument = PrimedTransformer.parseToDocument(sampleFile);
			
			String result = PrimedTransformer.parseToString(sampleDocument);
			assertTrue(result != null);
			
			assertXMLEqual(sampleString, result);
			
		} catch (ParserConfigurationException e) {					
			fail(e.getMessage());
		} catch (SAXException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		} catch (TransformerException e) {
			fail(e.getMessage());
		}
	
	}
	
	@Test
	public void testPrimedTransformer_newTransformer_configurationSaxon() {
		
		try {
			
			Transformer result = PrimedTransformer.newTransformer(null);			
			assertEquals(true, result != null);
			
		} catch (TransformerConfigurationException e) {
			fail(e.getMessage());
		}
		
	}
	
}
