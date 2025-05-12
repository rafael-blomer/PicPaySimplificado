package br.com.rafaelblomer.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.rafaelblomer.business.exceptions.AlteracaoSenhaException;
import br.com.rafaelblomer.business.exceptions.DadoUnicoException;
import br.com.rafaelblomer.business.exceptions.DinheiroEmContaException;
import br.com.rafaelblomer.business.exceptions.SaldoInsuficienteException;
import br.com.rafaelblomer.business.exceptions.TransferenciaDeLojistaException;
import br.com.rafaelblomer.business.exceptions.UsuarioNaoEncontradoException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionHandler {
	
	@ExceptionHandler(UsuarioNaoEncontradoException.class)
	public ResponseEntity<StandardError> usuarioNãoEncontrado(UsuarioNaoEncontradoException e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		StandardError err = new StandardError(System.currentTimeMillis(), status.value(), "Usuário não encontrado.", e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(SaldoInsuficienteException.class)
	public ResponseEntity<StandardError> saldoInsuficiente(SaldoInsuficienteException e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		StandardError err = new StandardError(System.currentTimeMillis(), status.value(), "Saldo insuficiente.", e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(TransferenciaDeLojistaException.class)
	public ResponseEntity<StandardError> transferenciaLojista(TransferenciaDeLojistaException e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		StandardError err = new StandardError(System.currentTimeMillis(), status.value(), "Lojistas não podem realizar transferências.", e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(AlteracaoSenhaException.class)
	public ResponseEntity<StandardError> alteracaoSenha(AlteracaoSenhaException e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		StandardError err = new StandardError(System.currentTimeMillis(), status.value(), "Alteração de senha incorreta.", e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(DinheiroEmContaException.class)
	public ResponseEntity<StandardError> dinheiroEmContaExclusao(DinheiroEmContaException e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		StandardError err = new StandardError(System.currentTimeMillis(), status.value(), "Ainda existe dinheiro em sua conta.", e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(DadoUnicoException.class)
	public ResponseEntity<StandardError> dadoUnico(DadoUnicoException e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.CONFLICT;
		StandardError err = new StandardError(System.currentTimeMillis(), status.value(), "Esse dado já existe em nosso sitema.", e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
}
