package com.kaikoda.willow;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

class PrimedTransformer {

	public static final String DEFAULT_TRANSFORMER_FACTORY = "net.sf.saxon.TransformerFactoryImpl";
	public static final boolean SET_EXPAND_ENTITY_REFERENCES = true;
	public static final boolean SET_IGNORING_ELEMENT_CONTENT_WHITESPACE = true;
	public static final boolean SET_NAMESPACE_AWARE = true;
	public static final boolean SET_VALIDATING = false;
	public static final boolean SET_XINCLUDE_AWARE = false;

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
		documentBuilderFactory.setExpandEntityReferences(PrimedTransformer.SET_EXPAND_ENTITY_REFERENCES);
		documentBuilderFactory.setNamespaceAware(PrimedTransformer.SET_NAMESPACE_AWARE);
		documentBuilderFactory.setValidating(PrimedTransformer.SET_VALIDATING);
		documentBuilderFactory.setXIncludeAware(PrimedTransformer.SET_XINCLUDE_AWARE);
		documentBuilderFactory.setIgnoringElementContentWhitespace(PrimedTransformer.SET_IGNORING_ELEMENT_CONTENT_WHITESPACE);
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
	public static Transformer newTransformer(Source xsl) throws TransformerConfigurationException {
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
	public static Transformer newTransformer(Source xsl, String factoryImplId) throws TransformerConfigurationException {

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

	/**
	 * Parses the DOM Document supplied and returns it as an XML String.
	 * 
	 * @param xml
	 *            the DOM Document to be parsed
	 * @return the contents of the DOM Document as an XML String.
	 * @throws TransformerException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public static String parseToString(Document xml) throws TransformerException, SAXException, IOException, ParserConfigurationException {

		// Prepare an XML String for transformation
		Source xmlSource = new DOMSource(xml);

		// Prepare a container to hold the result of the transformation
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);

		// Execute a transformation without an XSLT stylesheet
		PrimedTransformer.transform(xmlSource, null, result, null, null);

		// Extract the result of the transformation from the container
		return writer.toString();

	}

	/**
	 * Parses the XML File supplied and returns it as an XML String.
	 * 
	 * @param xml
	 *            the file to be parsed
	 * @return the contents of the file as an XML String.
	 * @throws TransformerException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public static String parseToString(File xml) throws TransformerException, SAXException, IOException, ParserConfigurationException {
		return PrimedTransformer.parseToString(PrimedTransformer.parseToDocument(xml));
	}

	/**
	 * Transforms XML using the XSLT stylesheet and parameters specified.
	 * 
	 * @param xml
	 *            the XML to be transformed.
	 * @param xsl
	 *            the XSLT stylesheet to use for the transformation.
	 * @param result
	 *            a container to hold the result of the transformation.
	 * @param params
	 *            a list of parameters for configuring the XSLT stylesheet prior
	 *            to the transformation.
	 * @param listener
	 * @throws TransformerException
	 *             when it's not possible to complete the transformation.
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	public static void transform(Source xml, Source xsl, Result result, TreeMap<String, String> params, ErrorListener listener) throws TransformerException, SAXException, IOException, ParserConfigurationException {

		// Use the transformer factory to create a new transformer
		Transformer transformer = PrimedTransformer.newTransformer(xsl);
		if (listener != null) {
			transformer.setErrorListener(listener);
		}

		// Pass parameters through to the XSLT
		if (params != null) {

			// Loop through all the parameters by name
			for (String name : params.keySet()) {

				// Use the name to retrieve the value
				String value = params.get(name);

				// If the parameter value isn't null, pass it through
				if (value != null) {
					transformer.setParameter(name, value);
				}
			}
		}

		transformer.transform(xml, result);

	}

}