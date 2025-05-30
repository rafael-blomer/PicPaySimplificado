package br.com.rafaelblomer.infrastructure.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Transacao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(nullable = true, name = "usuario_remetente", foreignKey = @ForeignKey(name = "fk_usuario_remetente"))
	private Usuario remetente;
	@ManyToOne
	@JoinColumn(nullable = true, name = "usuario_destinatario", foreignKey = @ForeignKey(name = "fk_usuario_destinatario"))
	private Usuario destinatario;
	private Double valor;
	private LocalDateTime dataHora;
	private Boolean sucesso;

	/*
	 * ALTER TABLE transacao DROP CONSTRAINT fk_usuario_remetente;
	 * 
	 * ALTER TABLE transacao ADD CONSTRAINT fk_usuario_remetente FOREIGN KEY
	 * (usuario_remetente) REFERENCES usuario(id) ON DELETE SET NULL;
	 * 
	 * ALTER TABLE transacao DROP CONSTRAINT fk_usuario_destinatario;
	 * 
	 * ALTER TABLE transacao ADD CONSTRAINT fk_usuario_destinatario FOREIGN KEY
	 * (usuario_destinatario) REFERENCES usuario(id) ON DELETE SET NULL;
	 */
	 
	
	public Transacao() {
	}

	public Transacao(Usuario remetente, Usuario destinatario, Boolean sucesso, Double valor) {
		this.remetente = remetente;
		this.destinatario = destinatario;
		this.dataHora = LocalDateTime.now();
		this.sucesso = sucesso;
		this.valor = valor;
	}

	public Usuario getRemetente() {
		return remetente;
	}

	public void setRemetente(Usuario remetente) {
		this.remetente = remetente;
	}

	public Usuario getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(Usuario destinatario) {
		this.destinatario = destinatario;
	}

	public LocalDateTime getDataHora() {
		return dataHora;
	}

	public void setDataHora(LocalDateTime dataHora) {
		this.dataHora = dataHora;
	}

	public Boolean getSucesso() {
		return sucesso;
	}

	public void setSucesso(Boolean sucesso) {
		this.sucesso = sucesso;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

}
