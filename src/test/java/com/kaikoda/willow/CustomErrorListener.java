package com.kaikoda.willow;

import java.util.ArrayList;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;

/**
 * @author Sheila Ellen Thomson
 * 
 */
public class CustomErrorListener implements ErrorListener {

	/**
	 * A container for storing TransformerExceptions deemed to be an error when
	 * passed to this instance of CustomErrorListener.
	 */
	ArrayList<TransformerException> errors = new ArrayList<TransformerException>();

	/**
	 * A container for storing TransformerExceptions deemed to be a fatal error
	 * when passed to this instance of CustomErrorListener.
	 */
	ArrayList<TransformerException> fatalErrors = new ArrayList<TransformerException>();

	/**
	 * A container for storing TransformerExceptions deemed to be a warning when
	 * passed to this instance of CustomErrorListener.
	 */
	ArrayList<TransformerException> warnings = new ArrayList<TransformerException>();

	@Override
	public void warning(TransformerException e) throws TransformerException {
		this.warnings.add(e);
		throw (e);
	}
	
	@Override
	public void error(TransformerException e) throws TransformerException {
		this.errors.add(e);
		throw (e);
	}

	@Override
	public void fatalError(TransformerException e) throws TransformerException {
		this.fatalErrors.add(e);
		throw (e);
	}

	/**
	 * @return the collection of warnings currently stored by this instance of
	 *         CustomErrorListener.
	 */
	public ArrayList<TransformerException> getWarnings() {
		return this.warnings;
	}
	
	/**
	 * @return the collection of non-fatal errors currently stored by this
	 *         instance of CustomErrorListener.
	 */
	public ArrayList<TransformerException> getErrors() {
		return this.errors;
	}

	/**
	 * @return the collection of fatal errors currently stored by this instance
	 *         of CustomErrorListener.
	 */
	public ArrayList<TransformerException> getFatalErrors() {
		return this.fatalErrors;
	}
	
	/**
	 * @return the total number of warnings currently stored by this instance of
	 *         CustomErrorListener.
	 */
	public int getTotalWarnings() {
		return this.warnings.size();
	}

	/**
	 * @return the total number of non-fatal errors currently stored by this
	 *         instance of CustomErrorListener.
	 */
	public int getTotalErrors() {
		return this.errors.size();
	}

	/**
	 * @return the total number of fatal errors currently stored by this
	 *         instance of CustomErrorListener.
	 */
	public int getTotalFatalErrors() {
		return this.fatalErrors.size();
	}
	
	/**
	 * @return the total number of exceptions currently stored by this instance
	 *         of CustomErrorListener.
	 */
	public int getTotalExceptions() {
		return this.getTotalWarnings() + this.getTotalErrors() + this.getTotalFatalErrors();
	}

}
