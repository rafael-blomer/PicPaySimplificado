package br.com.rafaelblomer.infrastructure.clients.response;

public class AutorizacaoData {
	private boolean authorization;

	public AutorizacaoData(boolean authorization) {
		super();
		this.authorization = authorization;
	}

	public boolean isAuthorization() {
		return authorization;
	}

	public void setAuthorization(boolean authorization) {
		this.authorization = authorization;
	}

}
