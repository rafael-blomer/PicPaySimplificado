package br.com.rafaelblomer.infrastructure.entities;

import jakarta.persistence.Entity;

@Entity
public class UsuarioComum extends Usuario {

	public UsuarioComum() {
	}

	public UsuarioComum(String nomeCompleto, String cpf, String email, String senha, Double saldo) {
		super(nomeCompleto, cpf, email, senha, saldo);
	}

}
