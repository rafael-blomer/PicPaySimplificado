package br.com.rafaelblomer.business.exceptions;

public class UsuarioNaoEncontradoException extends RuntimeException {
	private static final long serialVersionUID = -2384591321496091688L;
	
	public UsuarioNaoEncontradoException(String msg) {
		super(msg);
	}

}
