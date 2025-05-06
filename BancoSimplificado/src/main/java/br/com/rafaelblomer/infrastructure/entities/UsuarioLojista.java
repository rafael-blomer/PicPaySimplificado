package br.com.rafaelblomer.infrastructure.entities;

import jakarta.persistence.Entity;

@Entity
public class UsuarioLojista extends Usuario {

	public UsuarioLojista() {
	}

	public UsuarioLojista(String nomeCompleto, String cpf, String email, String senha, Double saldo) {
		super(nomeCompleto, cpf, email, senha, saldo);
	}

}
