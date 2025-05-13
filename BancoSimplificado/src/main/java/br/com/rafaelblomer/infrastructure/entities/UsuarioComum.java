package br.com.rafaelblomer.infrastructure.entities;

import jakarta.persistence.Entity;

@Entity
public class UsuarioComum extends Usuario {

	public UsuarioComum() {
	}

	public UsuarioComum(String nomeCompleto, String documento, String email, String senha) {
		super(nomeCompleto, documento, email, senha);
	}

}
