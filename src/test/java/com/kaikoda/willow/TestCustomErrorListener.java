package com.kaikoda.willow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.xml.transform.TransformerException;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Sheila Ellen Thomson
 *
 */
public class TestCustomErrorListener {

	/**
	 * The default instance of CustomErrorListener that will be used during these tests.
	 */
	private CustomErrorListener listener;
	
	/**
	 * Before each test, check that all is as expected.
	 */
	@Before
	public void setup() {
		
		listener = new CustomErrorListener();
		
		assertEquals(0, listener.getTotalWarnings());
		assertEquals(0, listener.getTotalErrors());
		assertEquals(0, listener.getTotalFatalErrors());
		assertEquals(0, listener.getTotalExceptions());
		
	}
	
	
	/**
	 * Check that warnings are stored and counted correctly.
	 */
	@Test
	public void testCustomErrorListener_getTotalWarnings() {
		
		TransformerException exception = new TransformerException("Testing Warnings");
		
		try {
			
			listener.warning(exception);

			fail("TransformerException not thrown.");
			
		} catch (TransformerException result) {			
			
			assertEquals(exception, result);			
			assertEquals(1, listener.getTotalWarnings());
			assertEquals(exception, listener.getWarnings().get(0));
			
			assertEquals(0, listener.getTotalErrors());
			assertEquals(0, listener.getTotalFatalErrors());
			assertEquals(1, listener.getTotalExceptions());
			
		}	
		
	}
	
	/**
	 * Check that non-fatal errors are stored and counted correctly.
	 */
	@Test
	public void testCustomErrorListener_getTotalErrors() {
		
		TransformerException exception = new TransformerException("Testing Errors");
		
		try {
			
			listener.error(exception);

			fail("TransformerException not thrown.");
			
		} catch (TransformerException result) {			
			
			assertEquals(exception, result);
			assertEquals(1, listener.getTotalErrors());
			assertEquals(exception, listener.getErrors().get(0));
			
			assertEquals(0, listener.getTotalWarnings());
			assertEquals(0, listener.getTotalFatalErrors());
			assertEquals(1, listener.getTotalExceptions());
			
		}	
		
	}

	/**
	 * Check that fatal errors are stored and counted correctly.
	 */
	@Test
	public void testCustomErrorListener_getTotalFatalErrors() {
		
		TransformerException exception = new TransformerException("Testing Warnings");
		
		try {
			
			listener.fatalError(exception);

			fail("TransformerException not thrown.");
			
		} catch (TransformerException result) {			
			
			assertEquals(exception, result);
			assertEquals(1, listener.getTotalFatalErrors());
			assertEquals(exception, listener.getFatalErrors().get(0));
			
			assertEquals(0, listener.getTotalWarnings());
			assertEquals(0, listener.getTotalErrors());
			assertEquals(1, listener.getTotalExceptions());
			
		}	
		
	}
	
	/**
	 * Check that all exceptions are stored and counted correctly.
	 */
	@Test
	public void testCustomErrorListener_getTotalExceptions() {
		
		TransformerException exception = new TransformerException("Testing Exceptions");
		
		try {
			
			listener.warning(exception);

			fail("TransformerException not thrown.");
			
		} catch (TransformerException result) {			
			
			assertEquals(exception, result);
			assertEquals(1, listener.getTotalWarnings());
			assertEquals(0, listener.getTotalErrors());
			assertEquals(0, listener.getTotalFatalErrors());
			assertEquals(1, listener.getTotalExceptions());
			
		}	
		
		try {
			
			listener.fatalError(exception);

			fail("TransformerException not thrown.");
			
		} catch (TransformerException result) {			
			
			assertEquals(exception, result);
			assertEquals(1, listener.getTotalWarnings());
			assertEquals(0, listener.getTotalErrors());
			assertEquals(1, listener.getTotalFatalErrors());
			assertEquals(2, listener.getTotalExceptions());
			
		}
		
		
		try {
			
			listener.error(exception);

			fail("TransformerException not thrown.");
			
		} catch (TransformerException result) {			
			
			assertEquals(exception, result);
			assertEquals(1, listener.getTotalWarnings());
			assertEquals(1, listener.getTotalErrors());
			assertEquals(1, listener.getTotalFatalErrors());
			assertEquals(3, listener.getTotalExceptions());
			
		}
		
	}
	
}
