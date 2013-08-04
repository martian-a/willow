package com.kaikoda.willow;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

class PrimedTransformer {
	
	/**
	 * Creates and configures a re-usable instance of DocumentBuilder.
	 *
	 * @throws ParserConfigurationException when it's not possible to configure
	 * the DocumentBuilder as required.
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
	 * Parses the XML file specified and returns its contents as a DOM Document.
	 * @param xml the file to be parsed.
	 * @return the contents of the file, as a DOM Document.
	 * @throws SAXException if there's an exception building the DOM Document.
	 * @throws IOException if there's an unresolvable problem finding or reading the file specified.
	 * @throws ParserConfigurationException if the DocumentBuilder is configured incorrectly.
	 */
	public static Document parse(File xml) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilder builder = PrimedTransformer.newDocumentBuilder();
		return builder.parse(xml);
	}
	
}