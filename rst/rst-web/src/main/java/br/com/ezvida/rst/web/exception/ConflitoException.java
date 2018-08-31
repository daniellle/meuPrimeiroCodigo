package br.com.ezvida.rst.web.exception;

import fw.security.exception.SecurityException;

public class ConflitoException extends SecurityException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConflitoException(String message) {
		super(message);
	}

}
