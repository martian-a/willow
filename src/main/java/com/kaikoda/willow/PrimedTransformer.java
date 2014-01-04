/*
 * PrimedTransformer
 * Copyright (C) 2013  Sheila Thomson
 * 
 * This Source Code Form is subject to the terms of the 
 * Mozilla Public License, v.2.0. 
 *     
 * If a copy of the MPL was not distributed with this file, 
 * You can obtain one at <http://mozilla.org/MPL/2.0/ />.
 */
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

import net.sf.saxon.lib.FeatureKeys;

import org.apache.xerces.util.XMLCatalogResolver;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author Sheila Ellen Thomson
 * 
 */
public class PrimedTransformer {

	/**
	 * The string identifying the default TransformerFactoryImpl used by
	 * instances of PrimedTransformer.
	 */
	public static final String DEFAULT_TRANSFORMER_FACTORY = "net.sf.saxon.TransformerFactoryImpl";

	/**
	 * The default value used when setting whether references to external
	 * entities will be expanded.
	 */
	public static final boolean SET_EXPAND_ENTITY_REFERENCES = true;

	/**
	 * The default value used when setting whether whitespace in the content
	 * will be ignored.
	 */
	public static final boolean SET_IGNORING_ELEMENT_CONTENT_WHITESPACE = true;

	/**
	 * The default value used when setting whether XML namespaces will be
	 * respected.
	 */
	public static final boolean SET_NAMESPACE_AWARE = true;

	/**
	 * The default value used when setting whether XML documents will be
	 * validated.
	 */
	public static final boolean SET_VALIDATING = false;

	/**
	 * The default value used when setting whether XIncludes will be processed.
	 */
	public static final boolean SET_XINCLUDE_AWARE = false;

	/**
	 * The XMLCatalogResolver used when building an XML Document.
	 */
	private XMLCatalogResolver catalogResolver;

	/**
	 * The DocumentBuilder used when creating a DOM Document.
	 */
	private DocumentBuilder documentBuilder;

	/**
	 * The DocumentBuilderFactory used when instantiating a new DocumentBuilder
	 * for this instance of PrimedTransformer.
	 */
	private final DocumentBuilderFactory documentBuilderFactory;

	/**
	 * The current XSL Stylesheet used when executing a transformation with this
	 * instance of PrimedTransformer.
	 */
	private Source stylesheet;

	/**
	 * The parameter set currently assigned to the Stylesheet used by this
	 * instance of PrimedTransformer.
	 */
	private TreeMap<String, String> stylesheetParameters;

	/**
	 * The Transformer used when executing transformations.
	 */
	private Transformer transformer;

	/**
	 * The ErrorListener currently assigned to the Transformer used by this
	 * instance of PrimedTransformer.
	 */
	private ErrorListener transformerErrorListener;

	/**
	 * The TransformerFactory used when instantiating a new Transformer for this
	 * instance of PrimedTransformer.
	 */
	private final TransformerFactory transformerFactory;

	/**
	 * Default constructor.
	 * 
	 * @throws ParserConfigurationException
	 *             if there's a problem instantiating a DocumentBuilder.
	 * @throws TransformerConfigurationException
	 *             if there's a problem instantiating a Transformer.
	 */
	public PrimedTransformer() throws ParserConfigurationException, TransformerConfigurationException {

		// Configure and store a re-usable DocumentBuilderFactory
		this.documentBuilderFactory = PrimedTransformer.newDocumentBuilderFactory();

		// Instantiate and store a re-usable DocumentBuilder
		this.documentBuilder = this.documentBuilderFactory.newDocumentBuilder();

		// Instantiate and store a re-usable XMLCatalogResolver
		this.catalogResolver = new XMLCatalogResolver();

		// Set the default XSL stylesheet to null.
		this.stylesheet = null;

		// Set the parameters of the XSL stylesheet to an empty collection.
		this.stylesheetParameters = new TreeMap<String, String>();

		// Configure and store a re-usable TransformerFactory
		this.transformerFactory = PrimedTransformer.newTransformerFactory();

		// Instantiate and store a re-usable Transformer
		this.setTransformer();

	}

	/**
	 * @return the CatalogResolver used by this instance of PrimedTransformer.
	 */
	public XMLCatalogResolver getCatalogResolver() {
		return this.catalogResolver;
	}

	/**
	 * @return the DocumentBuilder used by this instance of PrimedTransformer.
	 */
	public DocumentBuilder getDocumentBuilder() {
		return this.documentBuilder;
	}

	/**
	 * @return the DocumentBuilderFactory used when instantiating a new
	 *         DocumentBuilder for this instance of PrimedTransformer.
	 */
	public DocumentBuilderFactory getDocumentBuilderFactory() {
		return this.documentBuilderFactory;
	}

	/**
	 * @return the Transformer used when executing transformations with this
	 *         instance of PrimedTransformer.
	 */
	public Transformer getTransformer() {
		return this.transformer;
	}

	/**
	 * @return the TransformerFactory used when instantiating a new Transformer
	 *         for this instance of PrimedTransformer.
	 */
	public TransformerFactory getTransformerFactory() {
		return this.transformerFactory;
	}

	/**
	 * Creates and configures a Transformer generated using the instance of
	 * TransformerFactory implementation currently stored in this instance of
	 * PrimedTransformer.
	 * 
	 * @throws TransformerConfigurationException
	 *             when it's not possible to configure the Transformer as
	 *             required.
	 */
	public Transformer newTransformer() throws TransformerConfigurationException {
		return this.newTransformer((Source) null);
	}

	/**
	 * Creates and configures a Transformer generated using the instance of
	 * TransformerFactory implementation currently stored in this instance of
	 * PrimedTransformer.
	 * 
	 * @throws TransformerConfigurationException
	 *             when it's not possible to configure the Transformer as
	 *             required.
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	public Transformer newTransformer(File xsl) throws TransformerConfigurationException, SAXException, IOException, ParserConfigurationException {
		return this.newTransformer(this.parseToDOMSource(xsl));
	}

	/**
	 * Creates and configures a Transformer generated using the
	 * TransformerFactory implementation specified.
	 * 
	 * @throws TransformerConfigurationException
	 *             when it's not possible to configure the Transformer as
	 *             required.
	 */
	public Transformer newTransformer(Source xsl) throws TransformerConfigurationException {

		if (xsl != null) {
			return this.transformerFactory.newTransformer(xsl);
		}

		return this.transformerFactory.newTransformer();

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
	public Document parseToDocument(File xml) throws ParserConfigurationException, SAXException, IOException {
		return this.documentBuilder.parse(xml);
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
	public Document parseToDocument(String xml) throws ParserConfigurationException, SAXException, IOException {
		return this.documentBuilder.parse(new InputSource(new StringReader(xml)));
	}

	/**
	 * Parses the XML Document specified and returns its contents as a DOM
	 * Source.
	 * 
	 * @param xml
	 *            the document to be parsed.
	 * @return the contents of the document, as a DOM Source.
	 * @throws SAXException
	 *             if there's an exception building the DOM Source.
	 * @throws IOException
	 * @throws ParserConfigurationException
	 *             if the DocumentBuilder is configured incorrectly.
	 */
	public DOMSource parseToDOMSource(Document xml) throws ParserConfigurationException, SAXException, IOException {
		return new DOMSource(xml);
	}

	/**
	 * Parses the XML file specified and returns its contents as a DOM Source.
	 * 
	 * @param xml
	 *            the file to be parsed.
	 * @return the contents of the file, as a DOM Source.
	 * @throws SAXException
	 *             if there's an exception building the DOM Source.
	 * @throws IOException
	 *             if there's a problem reading the file.
	 * @throws ParserConfigurationException
	 *             if the DocumentBuilder is configured incorrectly.
	 */
	public DOMSource parseToDOMSource(File xml) throws ParserConfigurationException, SAXException, IOException {
		DOMSource source = new DOMSource(this.parseToDocument(xml));
		source.setSystemId(xml.toURI().toString());
		return source;
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
	public String parseToString(Document xml) throws TransformerException, SAXException, IOException, ParserConfigurationException {

		// Prepare an XML String for transformation
		Source xmlSource = this.parseToDOMSource(xml);

		// Prepare a StringWriter to write out the result of the transformation
		StringWriter writer = new StringWriter();

		// Prepare a container to hold the result of the transformation
		StreamResult result = new StreamResult(writer);

		// Execute a transformation without an XSLT stylesheet
		this.transform(xmlSource, null, result, null, null);

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
	public String parseToString(File xml) throws TransformerException, SAXException, IOException, ParserConfigurationException {
		return this.parseToString(this.parseToDocument(xml));
	}

	/**
	 * Changes the instance of CatalogResolver used by this instance of
	 * PrimedTransformer.
	 * 
	 * @param resolver
	 *            the CatalogResolver to use when building DOM Documents.
	 */
	public void setCatalogResolver(XMLCatalogResolver resolver) {
		this.catalogResolver = resolver;
		this.documentBuilder.setEntityResolver(this.catalogResolver);
	}

	/**
	 * Changes the instance of DocumentBuilder used by this instance of
	 * PrimedTransformer.
	 * 
	 * @param builder
	 *            the DocumentBuilder to use when building DOM Documents.
	 */
	public void setDocumentBuilder(DocumentBuilder builder) {
		this.documentBuilder = builder;
	}

	/**
	 * Changes the XSL Stylesheet used by this instance of PrimedTransformer.
	 * 
	 * @param xsl
	 *            the XSL Stylesheet to use for transformations.
	 * @throws TransformerConfigurationException
	 */
	public void setStylesheet(Source xsl) throws TransformerConfigurationException {

		// Update the stored XSL Stylesheet
		this.stylesheet = xsl;

		// Update the stored Transformer
		this.setTransformer();

	}

	/**
	 * Changes the parameters passed through to the XSL Stylesheet used by this
	 * instance of PrimedTransformer.
	 * 
	 * @param params
	 *            the parameters to pass through to the XSL Stylesheet
	 */
	public void setStylesheetParameters(TreeMap<String, String> params) {
		this.stylesheetParameters = params;
	}

	/**
	 * Creates a new instance of Transformer that uses the XSL Stylesheet,
	 * parameters and ErrorListener currently stored in this instance of
	 * PrimedTransformer.
	 * 
	 * @throws TransformerConfigurationException
	 */
	private void setTransformer() throws TransformerConfigurationException {

		if (this.stylesheet != null) {
			this.transformer = this.transformerFactory.newTransformer(this.stylesheet);
		} else {
			this.transformer = this.transformerFactory.newTransformer();
		}

		if (this.transformerErrorListener != null) {
			this.transformer.setErrorListener(this.transformerErrorListener);
		}

		// Pass parameters through to the XSLT
		if (this.stylesheetParameters != null) {

			// Loop through all the parameters by name
			for (String name : this.stylesheetParameters.keySet()) {

				// Use the name to retrieve the value
				String value = this.stylesheetParameters.get(name);

				// If the parameter value isn't null, pass it through
				if (value != null) {
					this.transformer.setParameter(name, value);
				}
			}
		}

	}

	/**
	 * Changes the ErrorListener used with this instance of PrimedTransformer.
	 * 
	 * @param listener
	 *            the ErrorListener to use for handling TransformationExceptions
	 *            thrown during transformations executed by this instance of
	 *            PrimedTransformation.
	 */
	public void setTransformerErrorListener(ErrorListener listener) {

		this.transformerErrorListener = listener;

		if (this.transformerErrorListener != null) {
			this.transformer.setErrorListener(this.transformerErrorListener);
		}

	}

	/**
	 * Transforms XML using the XSLT stylesheet and parameters specified.
	 * 
	 * @param xml
	 *            the XML to be transformed.
	 * @param stylesheet
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
	public void transform(File xml, File xsl, Result result, TreeMap<String, String> params, ErrorListener listener) throws TransformerException, SAXException, IOException, ParserConfigurationException {
		this.transform(this.parseToDOMSource(xml), this.parseToDOMSource(xsl), result, params, listener);
	}

	/**
	 * Transforms XML using the XSLT stylesheet and parameters specified.
	 * 
	 * @param xml
	 *            the XML to be transformed.
	 * @param stylesheet
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
	public void transform(Source xml, Source xsl, Result result, TreeMap<String, String> params, ErrorListener listener) throws TransformerException, SAXException, IOException, ParserConfigurationException {

		// Update the stored ErrorListener
		this.setTransformerErrorListener(listener);

		// Update the stored parameters for use with the XSL Stylesheet.
		this.setStylesheetParameters(params);

		// Update the stored XSL Stylesheet (and Transformer).
		this.setStylesheet(xsl);

		// Execute the transformation.
		this.transformer.transform(xml, result);

	}

	/**
	 * Creates and configures a re-usable instance of DocumentBuilder.
	 * 
	 * @throws ParserConfigurationException
	 *             when it's not possible to configure the DocumentBuilder as
	 *             required.
	 */
	public static DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {

		// Prepare for DOM Document building
		DocumentBuilderFactory documentBuilderFactory = PrimedTransformer.newDocumentBuilderFactory();
		return documentBuilderFactory.newDocumentBuilder();

	}

	/**
	 * Creates and configures a re-usable instance of DocumentBuilderFactory.
	 * 
	 * @return a DocumentBuilderFactory configured using the defaults specified
	 *         in this class.
	 */
	public static DocumentBuilderFactory newDocumentBuilderFactory() {

		// Prepare for DOM Document building
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setExpandEntityReferences(PrimedTransformer.SET_EXPAND_ENTITY_REFERENCES);
		documentBuilderFactory.setNamespaceAware(PrimedTransformer.SET_NAMESPACE_AWARE);
		documentBuilderFactory.setValidating(PrimedTransformer.SET_VALIDATING);
		documentBuilderFactory.setXIncludeAware(PrimedTransformer.SET_XINCLUDE_AWARE);
		documentBuilderFactory.setIgnoringElementContentWhitespace(PrimedTransformer.SET_IGNORING_ELEMENT_CONTENT_WHITESPACE);
		return documentBuilderFactory;

	}

	/**
	 * Creates and configures an instance of the default TransformerFactory
	 * implementation.
	 */
	public static TransformerFactory newTransformerFactory() {

		TransformerFactory factory = new net.sf.saxon.TransformerFactoryImpl();

		factory.setAttribute(FeatureKeys.XINCLUDE, PrimedTransformer.SET_XINCLUDE_AWARE);
		factory.setAttribute(FeatureKeys.VALIDATION_WARNINGS, !PrimedTransformer.SET_VALIDATING);

		return factory;

	}

}