package com.kaikoda.willow;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.TreeMap;

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
import org.apache.xerces.util.XMLCatalogResolver;
import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.XpathEngine;
import org.custommonkey.xmlunit.exceptions.XpathException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * @author Sheila Ellen Thomson
 * 
 */
public class TestPrimedTransformer {

	/**
	 * The default instance of PrimedTransformer that will be used during these
	 * tests.
	 */
	public static PrimedTransformer transformer;

	/**
	 * A sample XML file for use during tests.
	 */
	public static File sampleFileHelloWorldSemantic;

	/**
	 * A sample XML string for use during tests.
	 */
	public static String sampleStringHelloWorldSemantic;

	/**
	 * A sample XML file for use during tests.
	 */
	public static File sampleFileHelloWorldSemanticReversed;

	/**
	 * A sample XML string for use during tests.
	 */
	public static String sampleStringHelloWorldSemanticReversed;

	/**
	 * A sample XML string for use during tests.
	 */
	public static File sampleFileCharacterEntityReferences;

	/**
	 * A sample XML string for use during tests.
	 */
	public static String sampleStringCharacterEntityReferences;

	/**
	 * A sample XML string for use during tests.
	 */
	public static File sampleFileEntityReferences;

	/**
	 * A sample XML string for use during tests.
	 */
	public static String sampleStringEntityReferences;

	/**
	 * A sample XML string for use during tests.
	 */
	public static File sampleFileHelloWorldPlain;

	/**
	 * A sample XML string for use during tests.
	 */
	public static String sampleStringHelloWorldPlain;

	/**
	 * An XSL stylesheet that reverses the order of elements.
	 */
	public static File xslReverse;

	/**
	 * An XSL stylesheet that wraps a message in an element. The message and
	 * wrapper element name can both be set via parameters.
	 */
	public static File xslWrapMessage;

	/**
	 * An XSL stylesheet designed to generate a fatal TransformerException.
	 */
	public static File xslError;

	/**
	 * A sample file containing a basic XML prolog.
	 */
	public static File prologFile;

	/**
	 * A basic XML prolog string.
	 */
	public static String prologString;

	/**
	 * The default entity resolver used during tests.
	 */
	public static XMLCatalogResolver resolver;

	/**
	 * The default OASIS catalog used during tests.
	 */
	public static File catalog;

	/**
	 * Test environment configuration steps.
	 */
	@BeforeClass
	public static void setupOnce() {

		sampleFileHelloWorldSemantic = new File(TestPrimedTransformer.class.getResource("/data/control/hello_world_semantic.xml").getFile());
		sampleFileHelloWorldSemanticReversed = new File(TestPrimedTransformer.class.getResource("/data/control/hello_world_semantic_reversed.xml").getFile());
		sampleFileCharacterEntityReferences = new File(TestPrimedTransformer.class.getResource("/data/source/character_entity_references.xml").getFile());
		sampleFileEntityReferences = new File(TestPrimedTransformer.class.getResource("/data/source/entity_references.xml").getFile());
		sampleFileHelloWorldPlain = new File(TestPrimedTransformer.class.getResource("/data/control/hello_world_plain.xml").getFile());
		xslReverse = new File(TestPrimedTransformer.class.getResource("/xsl/reverse.xsl").getFile());
		xslError = new File(TestPrimedTransformer.class.getResource("/xsl/error.xsl").getFile());
		prologFile = new File(TestPrimedTransformer.class.getResource("/data/control/prolog.txt").getFile());
		xslWrapMessage = new File(TestPrimedTransformer.class.getResource("/xsl/wrap_message.xsl").getFile());
		catalog = new File(TestPrimedTransformer.class.getResource("/schema/catalog.xml").getFile());

		try {

			transformer = new PrimedTransformer();

			sampleStringHelloWorldSemantic = FileUtils.readFileToString(sampleFileHelloWorldSemantic);
			sampleStringHelloWorldSemanticReversed = FileUtils.readFileToString(sampleFileHelloWorldSemanticReversed);
			sampleStringCharacterEntityReferences = FileUtils.readFileToString(sampleFileCharacterEntityReferences);
			sampleStringEntityReferences = FileUtils.readFileToString(sampleFileEntityReferences);
			sampleStringHelloWorldPlain = FileUtils.readFileToString(sampleFileHelloWorldPlain);
			prologString = FileUtils.readFileToString(prologFile);

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		resolver = new XMLCatalogResolver();
		resolver.setCatalogList(new String[] { catalog.toURI().toString() });
		transformer.setCatalogResolver(resolver);

	}

	/**
	 * Before each test, check that all is as expected.
	 * 
	 * @throws ParserConfigurationException
	 */
	@Before
	public void setup() throws ParserConfigurationException {

		XMLUnit.setIgnoreWhitespace(true);
		XMLUnit.setControlEntityResolver(resolver);
		XMLUnit.setTestEntityResolver(resolver);

		assertEquals(true, transformer != null);
		assertEquals(true, resolver != null);
		assertEquals(true, catalog.exists());

		assertEquals(true, sampleFileHelloWorldSemantic.exists());
		assertEquals(true, sampleStringHelloWorldSemantic != null);

		assertEquals(true, sampleFileHelloWorldSemanticReversed.exists());
		assertEquals(true, sampleStringHelloWorldSemanticReversed != null);

		assertEquals(true, sampleFileCharacterEntityReferences.exists());
		assertEquals(true, sampleStringCharacterEntityReferences != null);

		assertEquals(true, sampleFileHelloWorldPlain.exists());
		assertEquals(true, sampleStringHelloWorldPlain != null);

		assertEquals(true, xslReverse.exists());
		assertEquals(true, xslError.exists());
		assertEquals(true, xslWrapMessage.exists());

		assertEquals(true, prologFile.exists());
		assertEquals(true, prologString != null);

	}

	/**
	 * Check that the default values stored in the PrimedTransformer class are
	 * those expected.
	 */
	@Test
	public void testPrimedTransformer_defaults() {

		assertEquals(true, PrimedTransformer.SET_EXPAND_ENTITY_REFERENCES);
		assertEquals(true, PrimedTransformer.SET_NAMESPACE_AWARE);
		assertEquals(false, PrimedTransformer.SET_VALIDATING);
		assertEquals(false, PrimedTransformer.SET_XINCLUDE_AWARE);
		assertEquals(true, PrimedTransformer.SET_IGNORING_ELEMENT_CONTENT_WHITESPACE);

	}

	/**
	 * Check that the default configuration for a DocumentBuilder created using
	 * a PrimedTransformer is as expected.
	 */
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

	/**
	 * Check that the PrimedTransformer correctly converts an XML File into a
	 * DOM Document.
	 */
	@Test
	public void testPrimedTransformer_parseToDocument_inputFile() {

		try {

			Document result = transformer.parseToDocument(sampleFileHelloWorldSemantic);
			assertTrue(result != null);

			assertEquals("document", result.getDocumentElement().getNodeName());

			XpathEngine engine = XMLUnit.newXpathEngine();
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

	/**
	 * Check that the PrimedTransformer correctly converts an XML String into a
	 * DOM Document.
	 */
	@Test
	public void testPrimedTransformer_parseToDocument_inputString() {

		try {

			Document result = transformer.parseToDocument(sampleStringHelloWorldSemantic);
			assertTrue(result != null);

			assertEquals("document", result.getDocumentElement().getNodeName());

			XpathEngine engine = XMLUnit.newXpathEngine();
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

	/**
	 * Check that the PrimedTransformer correctly executes a transformation
	 * using the XML supplied but no XSL Stylesheet.
	 */
	@Test
	public void testPrimedTransformer_transform_inputXSL_null() {

		try {

			// Prepare an XML String for transformation
			Source xmlSource = new DOMSource(transformer.parseToDocument(sampleStringHelloWorldSemantic));

			// Prepare a container to hold the result of the transformation
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);

			// Execute the transformation
			transformer.transform(xmlSource, null, result, null, null);

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

	/**
	 * Check that the PrimedTransformer correctly executes a transformation
	 * using the XML and XSL Stylesheets specified.
	 */
	@Test
	public void testPrimedTransformer_transform_inputXSL_reverse() {

		try {

			// Prepare an XML String for transformation
			Source xmlSource = new DOMSource(transformer.parseToDocument(sampleStringHelloWorldSemantic));

			// Prepare an XSLT Stylesheet to implement the transformation
			Source xslSource = new DOMSource(transformer.parseToDocument(xslReverse));

			// Prepare a container to hold the result of the transformation
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);

			// Prepare the control content
			String xmlExpected = FileUtils.readFileToString(new File(TestPrimedTransformer.class.getResource("/data/control/hello_world_semantic_reversed.xml").getFile()));

			// Execute the transformation
			transformer.transform(xmlSource, xslSource, result, null, null);

			// Extract the result of the transformation from the container
			String resultString = writer.toString();

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

	/**
	 * Check that the PrimedTransformer correctly converts an XML File into an
	 * XML String.
	 */
	@Test
	public void testPrimedTransformer_parseToString_inputFile() {

		try {

			String result = transformer.parseToString(sampleFileHelloWorldSemantic);
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

	/**
	 * Check that the PrimedTransformer correctly converts a DOM Document into
	 * an XML String.
	 */
	@Test
	public void testPrimedTransformer_parseToString_inputDocument() {

		try {

			Document sampleDocument = transformer.parseToDocument(sampleFileHelloWorldSemantic);

			String result = transformer.parseToString(sampleDocument);
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

	/**
	 * Check that the default configuration for a Transformer created using a
	 * PrimedTransformer without an XSL Stylesheet.
	 */
	@Test
	public void testPrimedTransformer_newTransformer_configurationDefault_stylesheetNone() {

		try {

			Transformer customTransformer = transformer.newTransformer();
			assertEquals(true, customTransformer != null);
			assertEquals("class net.sf.saxon.IdentityTransformer", customTransformer.getClass().toString());

			// Prepare an XML String for transformation
			Source xmlSource = transformer.parseToDOMSource(sampleFileHelloWorldSemantic);

			// Prepare a StringWriter to write out the result of the
			// transformation
			StringWriter writer = new StringWriter();

			// Prepare a container to hold the result of the transformation
			StreamResult streamResult = new StreamResult(writer);

			// Execute a transformation without an XSLT stylesheet
			customTransformer.transform(xmlSource, streamResult);

			// Extract the result of the transformation from the container
			String result = writer.toString();

			// Check that the XML hasn't changed as a result of the
			// transformation
			assertXMLEqual(sampleStringHelloWorldSemantic, result);

		} catch (TransformerConfigurationException e) {
			fail(e.getMessage());
		} catch (TransformerException e) {
			fail(e.getMessage());
		} catch (ParserConfigurationException e) {
			fail(e.getMessage());
		} catch (SAXException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		}

	}

	/**
	 * Check that the default configuration for a Transformer created using a
	 * PrimedTransformer with the XSL Stylesheet specified.
	 */
	@Test
	public void testPrimedTransformer_newTransformer_configurationDefault_stylesheetReversed() {

		try {

			Transformer customTransformer = transformer.newTransformer(TestPrimedTransformer.xslReverse);
			assertEquals(true, customTransformer != null);
			assertEquals("class net.sf.saxon.Controller", customTransformer.getClass().toString());

			// Prepare an XML String for transformation
			Source xmlSource = transformer.parseToDOMSource(sampleFileHelloWorldSemantic);

			// Prepare a StringWriter to write out the result of the
			// transformation
			StringWriter writer = new StringWriter();

			// Prepare a container to hold the result of the transformation
			StreamResult streamResult = new StreamResult(writer);

			// Execute a transformation without an XSLT stylesheet
			customTransformer.transform(xmlSource, streamResult);

			// Extract the result of the transformation from the container
			String result = writer.toString();

			// Check that the XML has changed as a result of the transformation
			assertXMLEqual(sampleStringHelloWorldSemanticReversed, result);

		} catch (TransformerConfigurationException e) {
			fail(e.getMessage());
		} catch (SAXException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		} catch (ParserConfigurationException e) {
			fail(e.getMessage());
		} catch (TransformerException e) {
			fail(e.getMessage());
		}

	}

	/**
	 * Check that the entity resolver is correctly set to the instance
	 * specified.
	 */
	@Test
	public void testPrimedTransformer_setCatalogResolver() {

		XMLCatalogResolver resolver = new XMLCatalogResolver(new String[] { catalog.toURI().toString() });
		transformer.setCatalogResolver(resolver);

		assertEquals(resolver, transformer.getCatalogResolver());

	}

	/**
	 * Check that the document builder is correctly set to the instance
	 * specified.
	 * 
	 * @throws ParserConfigurationException
	 * @throws TransformerConfigurationException
	 */
	@Test
	public void testPrimedTransformer_setDocumentBuilder() throws ParserConfigurationException, TransformerConfigurationException {

		PrimedTransformer customTransformer = new PrimedTransformer();

		// Retrieve the current document builder
		DocumentBuilder originalBuilder = customTransformer.getDocumentBuilder();
		assertEquals(originalBuilder, customTransformer.getDocumentBuilder());

		// Construct a new document builder
		DocumentBuilder builder = PrimedTransformer.newDocumentBuilder();

		// Double-check that the new builder is different to the current
		// document builder
		assertEquals(false, originalBuilder.equals(builder));

		// Replace the current document builder with the new document builder
		customTransformer.setDocumentBuilder(builder);

		// Check that the new document builder is now the current document
		// builder.
		assertEquals(builder, customTransformer.getDocumentBuilder());

	}

	/**
	 * Check that it's possible to configure the document builder to support
	 * xincludes in conjunction with static methods.
	 */
	@Test
	public void testPrimedTransformer_setDocumentBuilder_xincludingTrue() throws ParserConfigurationException, IOException, SAXException, TransformerException {

		PrimedTransformer customTransformer = new PrimedTransformer();

		File expectedXmlFile = new File(TestPrimedTransformer.class.getResource("/data/control/hello_world_expanded.xml").getFile());
		String expectedXmlString = FileUtils.readFileToString(expectedXmlFile, "UTF-8");

		File testXmlFile = new File(TestPrimedTransformer.class.getResource("/data/source/hello_world_xinclude.xml").getFile());

		DocumentBuilderFactory factory = customTransformer.getDocumentBuilderFactory();
		factory.setXIncludeAware(true);
		assertEquals(true, factory.isXIncludeAware());

		DocumentBuilder builder = factory.newDocumentBuilder();
		assertEquals(true, builder.isXIncludeAware());

		customTransformer.setDocumentBuilder(builder);
		assertEquals(true, customTransformer.getDocumentBuilder().isXIncludeAware());

		Document xmlDocument = customTransformer.parseToDocument(testXmlFile);
		String resultXmlString = customTransformer.parseToString(xmlDocument);

		XMLUnit.setIgnoreWhitespace(true);
		XMLUnit.setIgnoreAttributeOrder(true);

		assertXMLEqual(expectedXmlString, resultXmlString);

	}

	/**
	 * Check that the PrimedTransformation correctly transforms the XML
	 * specified, using the XSL Stylesheet and parameters specified.
	 */
	@Test
	public void testPrimedTransformer_transform_withStylesheet_withParams_withoutListener() {

		try {

			// Create a container to hold the parameters
			TreeMap<String, String> params = new TreeMap<String, String>();
			params.put("wrapper", "document");
			params.put("message", "Hello World!");
			params.put("test", null);

			// Prepare a StringWriter to write out the result of the
			// transformation
			StringWriter writer = new StringWriter();

			// Prepare a container to hold the result of the transformation
			StreamResult streamResult = new StreamResult(writer);

			// Execute a transformation
			transformer.transform(sampleFileHelloWorldSemantic, xslWrapMessage, streamResult, params, null);

			// Extract the result of the transformation from the container
			String result = writer.toString();

			// Check that the XML has changed as a result of the transformation
			assertXMLEqual(sampleStringHelloWorldPlain, result);

		} catch (TransformerException e) {
			fail(e.getMessage());
		} catch (SAXException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		} catch (ParserConfigurationException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Check that the PrimedTransformation correctly transforms the XML
	 * specified, using the XSL Stylesheet, parameters and listener specified.
	 */
	@Test
	public void testPrimedTransformer_transform_withStylesheet_withParams_withListener() {

		// Instantiate an ErrorListener
		CustomErrorListener listener = new CustomErrorListener();

		// Prepare a StringWriter to write out the result of the transformation
		StringWriter writer = new StringWriter();

		try {

			// Prepare a container to hold the result of the transformation
			StreamResult streamResult = new StreamResult(writer);

			// Execute a transformation
			transformer.transform(sampleFileHelloWorldPlain, xslError, streamResult, null, listener);

			fail("TransformerException not thrown.");

		} catch (TransformerException e) {

			assertEquals(1, listener.getTotalExceptions());
			assertEquals(0, listener.getTotalWarnings());
			assertEquals(0, listener.getTotalErrors());
			assertEquals(1, listener.getTotalFatalErrors());

			// Extract the result of the transformation from the container
			String result = writer.toString();

			// Check that the XML has changed as a result of the transformation
			assertEquals(prologString, result);

		} catch (SAXException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		} catch (ParserConfigurationException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Check that the default configuration of the PrimedTransformation is as
	 * expected.
	 */
	@Test
	public void testPrimedTransformer_configurationDefault() {

		/*
		 * Check the configuration of the DocumentBuilderFactory immediately
		 * after an instance of PrimedTransformer has been constructed.
		 */
		DocumentBuilderFactory initialDocumentBuilderFactory = transformer.getDocumentBuilderFactory();
		assertNotNull(initialDocumentBuilderFactory);
		assertEquals(PrimedTransformer.SET_EXPAND_ENTITY_REFERENCES, initialDocumentBuilderFactory.isExpandEntityReferences());
		assertEquals(PrimedTransformer.SET_NAMESPACE_AWARE, initialDocumentBuilderFactory.isNamespaceAware());
		assertEquals(PrimedTransformer.SET_VALIDATING, initialDocumentBuilderFactory.isValidating());
		assertEquals(PrimedTransformer.SET_XINCLUDE_AWARE, initialDocumentBuilderFactory.isXIncludeAware());
		assertEquals(PrimedTransformer.SET_IGNORING_ELEMENT_CONTENT_WHITESPACE, initialDocumentBuilderFactory.isIgnoringElementContentWhitespace());

		/*
		 * Check the configuration of the DocumentBuilder immediately after an
		 * instance of PrimedTransformer has been constructed.
		 */
		DocumentBuilder initialDocumentBuilder = transformer.getDocumentBuilder();
		assertNotNull(initialDocumentBuilder);
		assertEquals(PrimedTransformer.SET_NAMESPACE_AWARE, initialDocumentBuilder.isNamespaceAware());
		assertEquals(PrimedTransformer.SET_VALIDATING, initialDocumentBuilder.isValidating());
		assertEquals(PrimedTransformer.SET_XINCLUDE_AWARE, initialDocumentBuilder.isXIncludeAware());

		initialDocumentBuilder.setEntityResolver(resolver);

		// Check that the DocumentBuilder is expanding character entity
		// references.
		try {
			assertXMLEqual(initialDocumentBuilder.parse(sampleFileHelloWorldPlain), initialDocumentBuilder.parse(sampleFileCharacterEntityReferences));
		} catch (IOException e) {
			fail(e.getMessage());
		} catch (SAXException e) {
			fail(e.getMessage());
		}

		// Check that the DocumentBuilder isn't expanding entity references.
		try {
			assertXMLEqual(initialDocumentBuilder.parse(sampleFileHelloWorldPlain), initialDocumentBuilder.parse(sampleFileEntityReferences));
		} catch (IOException e) {
			fail(e.getMessage());
		} catch (SAXException e) {
			fail(e.getMessage());
		}

		/*
		 * Check the configuration of the TransformerFactory immediately after
		 * an instance of PrimedTransformer has been constructed.
		 */
		TransformerFactory initialTransformerFactory = transformer.getTransformerFactory();
		assertNotNull(initialTransformerFactory);
		assertEquals(PrimedTransformer.SET_XINCLUDE_AWARE, initialTransformerFactory.getAttribute(FeatureKeys.XINCLUDE));
		assertEquals(!PrimedTransformer.SET_VALIDATING, initialTransformerFactory.getAttribute(FeatureKeys.VALIDATION_WARNINGS));

		/*
		 * Check the configuration of the Transformer immediately after an
		 * instance of PrimedTransformer has been constructed.
		 */
		Transformer initialTransformer = transformer.getTransformer();
		assertNotNull(initialTransformer);

	}

}
