package br.com.rafaelblomer.business.exceptions;

public class TransferenciaNaoAutorizadaException extends RuntimeException {
	private static final long serialVersionUID = 4425460580616045203L;

	public TransferenciaNaoAutorizadaException(String msg) {
        super(msg);
    }
}
