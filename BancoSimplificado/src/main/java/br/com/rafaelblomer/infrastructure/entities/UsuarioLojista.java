package br.com.rafaelblomer.infrastructure.entities;

import jakarta.persistence.Entity;

@Entity
public class UsuarioLojista extends Usuario {

	public UsuarioLojista() {
	}

	public UsuarioLojista(String nomeCompleto, String documento, String email, String senha) {
		super(nomeCompleto, documento, email, senha);
	}


}
