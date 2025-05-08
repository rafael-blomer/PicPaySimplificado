package br.com.rafaelblomer.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.rafaelblomer.business.UsuariosService;
import br.com.rafaelblomer.business.dtos.AlteracaoSenhaDTO;
import br.com.rafaelblomer.infrastructure.entities.Usuario;
import br.com.rafaelblomer.infrastructure.entities.UsuarioComum;
import br.com.rafaelblomer.infrastructure.entities.UsuarioLojista;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private UsuariosService service;

	@GetMapping
	public ResponseEntity<List<Usuario>> buscarTodosUsuarios() {
		return ResponseEntity.ok().body(service.buscarTodosUsuarios());
	}

	@GetMapping("/one")
	public ResponseEntity<Usuario> buscarUmUsuario(@RequestParam Long id) {
		return ResponseEntity.ok().body(service.buscarUmUsuario(id));
	}

	@PostMapping("/comum")
	public ResponseEntity<Usuario> criarNovoUsuario(@RequestBody UsuarioComum comum) {
		UsuarioComum usuarioCriado = service.novoComum(comum);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id})").buildAndExpand(usuarioCriado.getId())
				.toUri();
		return ResponseEntity.created(uri).body(usuarioCriado);
	}
	
	@PostMapping("/lojista")
	public ResponseEntity<Usuario> criarNovoUsuario(@RequestBody UsuarioLojista lojista) {
		UsuarioLojista usuarioCriado = service.novoLojista(lojista);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id})").buildAndExpand(usuarioCriado.getId())
				.toUri();
		return ResponseEntity.created(uri).body(usuarioCriado);
	}

	@DeleteMapping
	public ResponseEntity<Void> excluirUsuario(@RequestParam Long id) {
		service.excluirUsuario(id);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/senha")
	public ResponseEntity<Void> alterarSenha(@RequestParam Long id, @RequestBody AlteracaoSenhaDTO dto) {
		service.alterarSenhaUsuario(id, dto);
		return ResponseEntity.noContent().build();
	}

	//TODO: criar dto para não receber os parametors via utl
	@PatchMapping("/depositar")
	public ResponseEntity<Usuario> alterarSenha(@RequestParam Long id, @RequestParam Double quantia) {
		return ResponseEntity.ok().body(service.depositarDinheiro(id, quantia));
	}

}
