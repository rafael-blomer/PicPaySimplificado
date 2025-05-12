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
import br.com.rafaelblomer.business.dtos.DepositoDTO;
import br.com.rafaelblomer.business.dtos.UsuarioRequestDTO;
import br.com.rafaelblomer.business.dtos.UsuarioResponseDTO;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private UsuariosService service;

	@GetMapping
	public ResponseEntity<List<UsuarioResponseDTO>> buscarTodosUsuarios() {
		return ResponseEntity.ok().body(service.buscarTodosUsuarios());
	}

	@GetMapping("/one")
	public ResponseEntity<UsuarioResponseDTO> buscarUmUsuario(@RequestParam Long id) {
		return ResponseEntity.ok().body(service.buscarUmUsuarioResponseDTO(id));
	}

	@PostMapping("/comum")
	public ResponseEntity<UsuarioResponseDTO> criarNovoUsuarioComum(@RequestBody UsuarioRequestDTO dtoComum) {
		UsuarioResponseDTO usuarioCriado = service.novoComum(dtoComum);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id})").buildAndExpand(usuarioCriado.id())
				.toUri();
		return ResponseEntity.created(uri).body(usuarioCriado);
	}
	
	@PostMapping("/lojista")
	public ResponseEntity<UsuarioResponseDTO> criarNovoUsuarioLojista(@RequestBody UsuarioRequestDTO dtoLojista) {
		UsuarioResponseDTO usuarioCriado = service.novoLojista(dtoLojista);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id})").buildAndExpand(usuarioCriado.id())
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

	@PatchMapping("/depositar")
	public ResponseEntity<String> realizarDeposito(@RequestBody DepositoDTO dto) {
		return ResponseEntity.ok().body(service.depositarDinheiro(dto));
	}

}
