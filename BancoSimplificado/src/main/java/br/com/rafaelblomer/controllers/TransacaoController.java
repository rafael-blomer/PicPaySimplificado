package br.com.rafaelblomer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.rafaelblomer.business.TransacaoService;
import br.com.rafaelblomer.business.dtos.TransacaoDTO;

@RestController
@RequestMapping("/transacao")
public class TransacaoController {
	
	@Autowired
	private TransacaoService service;

	@PostMapping
	public ResponseEntity<TransacaoDTO> novaTransacao(@RequestBody TransacaoDTO dto) {
		return ResponseEntity.ok().body(service.novaTransacao(dto));
	}
}
