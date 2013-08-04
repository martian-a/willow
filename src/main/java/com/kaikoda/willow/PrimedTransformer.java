package com.kaikoda.willow;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

class PrimedTransformer {

	public static final String DEFAULT_TRANSFORMER_FACTORY = "net.sf.saxon.TransformerFactoryImpl";

	/**
	 * Creates and configures a re-usable instance of DocumentBuilder.
	 * 
	 * @throws ParserConfigurationException
	 *             when it's not possible to configure the DocumentBuilder as
	 *             required.
	 */
	public static DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {

		// Prepare for DOM Document building
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setExpandEntityReferences(true);
		documentBuilderFactory.setNamespaceAware(true);
		documentBuilderFactory.setValidating(false);
		documentBuilderFactory.setXIncludeAware(true);
		documentBuilderFactory.setIgnoringElementContentWhitespace(true);
		return documentBuilderFactory.newDocumentBuilder();

	}

	/**
	 * Creates and configures a Transformer generated using the Saxon
	 * TransformerFactory implementation.
	 * 
	 * @throws TransformerConfigurationException
	 *             when it's not possible to configure the Transformer as
	 *             required.
	 */
	protected static Transformer newTransformer(Source xsl) throws TransformerConfigurationException {
		return PrimedTransformer.newTransformer(xsl, PrimedTransformer.DEFAULT_TRANSFORMER_FACTORY);
	}

	/**
	 * Creates and configures a Transformer generated using the
	 * TransformerFactory implementation specified.
	 * 
	 * @throws TransformerConfigurationException
	 *             when it's not possible to configure the Transformer as
	 *             required.
	 */
	protected static Transformer newTransformer(Source xsl, String factoryImplId) throws TransformerConfigurationException {

		/*
		 * Specify that Saxon should be used as the transformer instead of the
		 * system default
		 */
		System.setProperty("javax.xml.transform.TransformerFactory", factoryImplId);
		TransformerFactory factory = TransformerFactory.newInstance();

		if (xsl != null) {
			return factory.newTransformer(xsl);
		}

		return factory.newTransformer();

	}

	/**
	 * Parses the XML file specified and returns its contents as a DOM Document.
	 * 
	 * @param xml
	 *            the file to be parsed.
	 * @return the contents of the file, as a DOM Document.
	 * @throws SAXException
	 *             if there's an exception building the DOM Document.
	 * @throws IOException
	 *             if there's an unresolvable problem finding or reading the
	 *             file specified.
	 * @throws ParserConfigurationException
	 *             if the DocumentBuilder is configured incorrectly.
	 */
	public static Document parseToDocument(File xml) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilder builder = PrimedTransformer.newDocumentBuilder();
		return builder.parse(xml);
	}

	/**
	 * Parses the XML string supplied and returns it as a DOM Document
	 * 
	 * @param xml
	 *            the string to be parsed
	 * @return the string as a DOM Document
	 * @throws SAXException
	 *             if there's an exception building the DOM Document
	 * @throws IOException
	 *             if there's an unresolvable problem reading the string
	 * @throws ParserConfigurationException
	 *             if the DocumentBuilder is configured incorrectly.
	 */
	public static Document parseToDocument(String xml) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilder builder = PrimedTransformer.newDocumentBuilder();
		return builder.parse(new InputSource(new StringReader(xml)));
	}

}