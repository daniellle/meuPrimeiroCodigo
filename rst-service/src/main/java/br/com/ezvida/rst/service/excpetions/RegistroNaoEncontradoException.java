package br.com.ezvida.rst.service.excpetions;

import fw.core.exception.BusinessException;

public class RegistroNaoEncontradoException extends BusinessException {

	private static final long serialVersionUID = 1L;
	
	public RegistroNaoEncontradoException(String mensagem){
		super(mensagem);
	}
	
	public RegistroNaoEncontradoException(String mensagem, Throwable causa){
		super(mensagem, causa);
	}
}
