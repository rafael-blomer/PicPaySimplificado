package br.com.rafaelblomer.business.exceptions;

public class SaldoInsuficienteException extends RuntimeException {
	private static final long serialVersionUID = 1805034905006226976L;

	public SaldoInsuficienteException(String msg) {
		super(msg);
	}
}
