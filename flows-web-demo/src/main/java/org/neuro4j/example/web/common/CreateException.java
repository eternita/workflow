package org.neuro4j.example.web.common;

public class CreateException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CreateException() {
	}

	public CreateException(String message) {
		super(message);
	}

	public CreateException(Exception ex) {
		super(ex);
	}

	public CreateException(String message, Exception ex) {
		super(message, ex);
	}
}
