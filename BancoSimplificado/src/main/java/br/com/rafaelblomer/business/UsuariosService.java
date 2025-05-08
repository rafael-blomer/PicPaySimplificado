package br.com.rafaelblomer.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.rafaelblomer.business.dtos.AlteracaoSenhaDTO;
import br.com.rafaelblomer.infrastructure.entities.Usuario;
import br.com.rafaelblomer.infrastructure.entities.UsuarioComum;
import br.com.rafaelblomer.infrastructure.entities.UsuarioLojista;
import br.com.rafaelblomer.infrastructure.repositories.UsuarioRepository;

@Service
public class UsuariosService {

	@Autowired
	private UsuarioRepository repository;
	
	// Métodos default para os dois tipos de usuário

	public Usuario buscarUmUsuario(Long id) {
		return verificarUsuario(id);
	}

	public List<Usuario> buscarTodosUsuarios() {
		return repository.findAll();
	}

	// TODO: exceção caso haja dinheiro em conta
	public void excluirUsuario(Long id) {
		Usuario usuario = verificarUsuario(id);
		repository.delete(usuario);
	}
	
	//TODO
	public Usuario depositarDinheiro(Long id, Double quantia) {
		Usuario usuario = verificarUsuario(id);
		usuario.setSaldo(usuario.getSaldo() + quantia);
		repository.save(usuario);
		return usuario;
	}

	//TODO: tratamento de exceções
	public void alterarSenhaUsuario(Long id, AlteracaoSenhaDTO senhaDTO) {
		Usuario usuario = verificarUsuario(id);
		if (usuario.getSenha().equals(senhaDTO.senhaAntiga())) {
			usuario.setSenha(senhaDTO.senhaNova());
			repository.save(usuario);
		} else {
			throw new RuntimeException();
		}
	}


	// Métodos para usuário lojista

	public UsuarioLojista novoLojista(Usuario usuario) {
		UsuarioLojista lojista = new UsuarioLojista(usuario.getNomeCompleto(), usuario.getCpf(), usuario.getEmail(), usuario.getSenha(), usuario.getSaldo());
		return repository.save(lojista);
	}

	
	// Métodos para usuário comum

	public UsuarioComum novoComum(Usuario usuario) {
		UsuarioComum comum = new UsuarioComum(usuario.getNomeCompleto(), usuario.getCpf(), usuario.getEmail(), usuario.getSenha(), usuario.getSaldo());
		return repository.save(comum);
	}

	
	// Métodos utilitários

	private Usuario verificarUsuario(Long id) {
		return repository.findById(id).orElseThrow(() -> new RuntimeException());
	}
	
	public void atualizarSaldoConta(Usuario rementente, Usuario destinatario, Double valor) {
		rementente.setSaldo(rementente.getSaldo() - valor);
		destinatario.setSaldo(destinatario.getSaldo() + valor);
		repository.save(rementente);
		repository.save(destinatario);
	}
}
