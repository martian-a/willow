package com.kaikoda.willow;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.sf.saxon.lib.FeatureKeys;

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
	
	public static PrimedTransformer transformer;
	
	public static File sampleFileHelloWorldSemantic;
	public static String sampleStringHelloWorldSemantic;
	public static File sampleFileCharacterEntityReferences;
	public static String sampleStringCharacterEntityReferences;
	public static File sampleFileEntityReferences;
	public static String sampleStringEntityReferences;
	public static File sampleFileHelloWorldPlain;
	public static String sampleStringHelloWorldPlain;
	public static File xslReverse;
	
	@BeforeClass
	public static void setupOnce() throws IOException, ParserConfigurationException {
		
		transformer = new PrimedTransformer();
		
		sampleFileHelloWorldSemantic = new File(TestPrimedTransformer.class.getResource("/data/control/hello_world_semantic.xml").getFile());
		sampleStringHelloWorldSemantic = FileUtils.readFileToString(sampleFileHelloWorldSemantic);	
		
		sampleFileCharacterEntityReferences = new File(TestPrimedTransformer.class.getResource("/data/source/character_entity_references.xml").getFile());
		sampleStringCharacterEntityReferences = FileUtils.readFileToString(sampleFileCharacterEntityReferences);
		
		sampleFileEntityReferences = new File(TestPrimedTransformer.class.getResource("/data/source/entity_references.xml").getFile());
		sampleStringEntityReferences = FileUtils.readFileToString(sampleFileEntityReferences);	
		
		sampleFileHelloWorldPlain = new File(TestPrimedTransformer.class.getResource("/data/control/hello_world_plain.xml").getFile());
		sampleStringHelloWorldPlain = FileUtils.readFileToString(sampleFileHelloWorldPlain);	
		
		xslReverse = new File(TestPrimedTransformer.class.getResource("/xsl/reverse.xsl").getFile());
		
	}
	
	@Before
	public void setup() {	
		
		assertEquals(true, transformer != null);
		assertEquals(true, sampleFileHelloWorldSemantic.exists());
		assertEquals(true, sampleStringHelloWorldSemantic != null);
		
		assertEquals(true, sampleFileCharacterEntityReferences.exists());
		assertEquals(true, sampleStringCharacterEntityReferences != null);
		
		assertEquals(true, sampleFileHelloWorldPlain.exists());
		assertEquals(true, sampleStringHelloWorldPlain != null);
		
		assertEquals(true, xslReverse.exists());		
		
	}
	
	@Test
	public void testPrimedTransformer_defaults() {
		
		assertEquals(true, PrimedTransformer.SET_EXPAND_ENTITY_REFERENCES);
		assertEquals(true, PrimedTransformer.SET_NAMESPACE_AWARE);
		assertEquals(false, PrimedTransformer.SET_VALIDATING);
		assertEquals(false, PrimedTransformer.SET_XINCLUDE_AWARE);
		assertEquals(true, PrimedTransformer.SET_IGNORING_ELEMENT_CONTENT_WHITESPACE);
		
	}
	
	@Test
	public void testPrimedTransformer_newDocumentBuilder_configuration() {

		try {
			
			DocumentBuilder result = PrimedTransformer.newDocumentBuilder();
			
			assertEquals(PrimedTransformer.SET_NAMESPACE_AWARE, result.isNamespaceAware());
			assertEquals(PrimedTransformer.SET_VALIDATING, result.isValidating());
			assertEquals(PrimedTransformer.SET_XINCLUDE_AWARE, result.isXIncludeAware());			
			
		} catch (ParserConfigurationException e) {					
			fail(e.getMessage());
		}
		
	}
	
	@Test
	public void testPrimedTransformer_parseToDocument_inputFile() {		
		
		try {
			
			Document result = PrimedTransformer.parseToDocument(sampleFileHelloWorldSemantic);
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
			
			Document result = PrimedTransformer.parseToDocument(sampleStringHelloWorldSemantic);
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
			Source xmlSource = new DOMSource(PrimedTransformer.parseToDocument(sampleStringHelloWorldSemantic));
			
			// Prepare a container to hold the result of the transformation
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			
			// Execute the transformation
			PrimedTransformer.transform(xmlSource, null, result, null, null);		
			
			// Extract the result of the transformation from the container
			String resultString = writer.toString();

			XMLUnit.setIgnoreWhitespace(true);
			
			assertXMLEqual(sampleStringHelloWorldSemantic, resultString);		
			
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
			Source xmlSource = new DOMSource(PrimedTransformer.parseToDocument(sampleStringHelloWorldSemantic));
			
			// Prepare an XSLT Stylesheet to implement the transformation
			Source xslSource = new DOMSource(PrimedTransformer.parseToDocument(xslReverse));
			
			// Prepare a container to hold the result of the transformation
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			
			// Prepare the control content
			String xmlExpected = FileUtils.readFileToString(new File(TestPrimedTransformer.class.getResource("/data/control/hello_world_semantic_reversed.xml").getFile()));
			
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
			
			String result = PrimedTransformer.parseToString(sampleFileHelloWorldSemantic);
			assertTrue(result != null);
			
			assertXMLEqual(sampleStringHelloWorldSemantic, result);
			
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
			
			Document sampleDocument = PrimedTransformer.parseToDocument(sampleFileHelloWorldSemantic);
			
			String result = PrimedTransformer.parseToString(sampleDocument);
			assertTrue(result != null);
			
			assertXMLEqual(sampleStringHelloWorldSemantic, result);
			
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
	
	@Test
	public void testPrimedTransformer_configurationDefault() {
		
		/*
		 *  Check the configuration of the DocumentBuilderFactory
		 *  immediately after an instance of PrimedTransformer has been constructed. 
		 */		
		DocumentBuilderFactory initialDocumentBuilderFactory = transformer.getDocumentBuilderFactory();		
		assertNotNull(initialDocumentBuilderFactory);
		assertEquals(PrimedTransformer.SET_EXPAND_ENTITY_REFERENCES, initialDocumentBuilderFactory.isExpandEntityReferences());
		assertEquals(PrimedTransformer.SET_NAMESPACE_AWARE, initialDocumentBuilderFactory.isNamespaceAware());
		assertEquals(PrimedTransformer.SET_VALIDATING, initialDocumentBuilderFactory.isValidating());
		assertEquals(PrimedTransformer.SET_XINCLUDE_AWARE, initialDocumentBuilderFactory.isXIncludeAware());
		assertEquals(PrimedTransformer.SET_IGNORING_ELEMENT_CONTENT_WHITESPACE, initialDocumentBuilderFactory.isIgnoringElementContentWhitespace());
		
		/*
		 *  Check the configuration of the DocumentBuilder
		 *  immediately after an instance of PrimedTransformer has been constructed. 
		 */		
		DocumentBuilder initialDocumentBuilder = transformer.getDocumentBuilder();		
		assertNotNull(initialDocumentBuilder);
		assertEquals(PrimedTransformer.SET_NAMESPACE_AWARE, initialDocumentBuilder.isNamespaceAware());
		assertEquals(PrimedTransformer.SET_VALIDATING, initialDocumentBuilder.isValidating());
		assertEquals(PrimedTransformer.SET_XINCLUDE_AWARE, initialDocumentBuilder.isXIncludeAware());	
		
		// Check that the DocumentBuilder is expanding character entity references.
		try {
			assertXMLEqual(initialDocumentBuilder.parse(sampleFileHelloWorldPlain), initialDocumentBuilder.parse(sampleFileCharacterEntityReferences));
		} catch(IOException e) {
			fail(e.getMessage());
		} catch (SAXException e) {
			fail(e.getMessage());
		}
		
		// Check that the DocumentBuilder isn't expanding entity references.		
		try {
			assertXMLEqual(initialDocumentBuilder.parse(sampleFileHelloWorldPlain), initialDocumentBuilder.parse(sampleFileEntityReferences));
		} catch(IOException e) {
			fail(e.getMessage());
		} catch (SAXException e) {
			fail(e.getMessage());
		}
		
		/*
		 *  Check the configuration of the TransformerFactory
		 *  immediately after an instance of PrimedTransformer has been constructed. 
		 */		
		TransformerFactory initialTransformerFactory = transformer.getTransformerFactory();		
		assertNotNull(initialTransformerFactory);
		assertEquals(PrimedTransformer.SET_XINCLUDE_AWARE, initialTransformerFactory.getAttribute(FeatureKeys.XINCLUDE));
		assertEquals(!PrimedTransformer.SET_VALIDATING, initialTransformerFactory.getAttribute(FeatureKeys.VALIDATION_WARNINGS));
				
	}
	
}
