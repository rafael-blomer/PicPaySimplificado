package br.com.rafaelblomer.business;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.rafaelblomer.business.converter.Converter;
import br.com.rafaelblomer.business.dtos.AlteracaoSenhaDTO;
import br.com.rafaelblomer.business.dtos.DepositoDTO;
import br.com.rafaelblomer.business.dtos.UsuarioRequestDTO;
import br.com.rafaelblomer.business.dtos.UsuarioResponseDTO;
import br.com.rafaelblomer.business.exceptions.AlteracaoSenhaException;
import br.com.rafaelblomer.business.exceptions.DadoUnicoException;
import br.com.rafaelblomer.business.exceptions.DinheiroEmContaException;
import br.com.rafaelblomer.business.exceptions.UsuarioNaoEncontradoException;
import br.com.rafaelblomer.infrastructure.entities.Usuario;
import br.com.rafaelblomer.infrastructure.entities.UsuarioComum;
import br.com.rafaelblomer.infrastructure.entities.UsuarioLojista;
import br.com.rafaelblomer.infrastructure.repositories.UsuarioRepository;

@Service
public class UsuariosService {

	@Autowired
	private UsuarioRepository repository;

	@Autowired
	private Converter converter;

	// Métodos default para os dois tipos de usuário

	public UsuarioResponseDTO buscarUmUsuarioResponseDTO(Long id) {
		Usuario entity = buscaUsuarioEntity(id);
		return converter.paraUsuarioResponse(entity);
	}

	public List<UsuarioResponseDTO> buscarTodosUsuarios() {
		List<Usuario> list = repository.findAll();
		return list.stream().map(converter::paraUsuarioResponse).collect(Collectors.toList());
	}

	public void excluirUsuario(Long id) {
		Usuario usuario = buscaUsuarioEntity(id);
		if (usuario.getSaldo() > 0)
			throw new DinheiroEmContaException("Transfira ou retire o resto do dinheiro em conta para poder excluir sua conta.");
		repository.delete(usuario);
	}

	public String depositarDinheiro(DepositoDTO dto) {
		Usuario usuario = buscaUsuarioEntity(dto.id());
		usuario.setSaldo(usuario.getSaldo() + dto.valor());
		repository.save(usuario);
		return "Depósito realizado com sucesso";
	}

	public void alterarSenhaUsuario(Long id, AlteracaoSenhaDTO senhaDTO) {
		Usuario usuario = buscaUsuarioEntity(id);
		if (!Objects.equals(senhaDTO.senhaAntiga(), usuario.getSenha()))
			throw new AlteracaoSenhaException("Senha antiga está incorreta.");
		usuario.setSenha(senhaDTO.senhaNova());
		repository.save(usuario);
	}

	// Métodos para usuário lojista

	public UsuarioResponseDTO novoLojista(UsuarioRequestDTO usuarioDTO) {
		verificarDadosUnicos(usuarioDTO.email(), usuarioDTO.documento());
		Usuario usuario = converter.paraUsuarioEntity(usuarioDTO);
		UsuarioLojista lojista = new UsuarioLojista(usuario.getNomeCompleto(), usuario.getDocumento(), usuario.getEmail(),
				usuario.getSenha());
		repository.save(lojista);
		return converter.paraUsuarioResponse(lojista);
	}

	// Métodos para usuário comum

	public UsuarioResponseDTO novoComum(UsuarioRequestDTO usuarioDTO) {
		verificarDadosUnicos(usuarioDTO.email(), usuarioDTO.documento());
		Usuario usuario = converter.paraUsuarioEntity(usuarioDTO);
		UsuarioComum comum = new UsuarioComum(usuario.getNomeCompleto(), usuario.getDocumento(), usuario.getEmail(),
				usuario.getSenha());
		repository.save(comum);
		return converter.paraUsuarioResponse(comum);
	}

	// Métodos utilitários

	public Usuario buscaUsuarioEntity(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new UsuarioNaoEncontradoException("O usuário não foi encontrado."));
	}

	public void atualizarSaldoConta(Usuario rementente, Usuario destinatario, Double valor) {
		rementente.setSaldo(rementente.getSaldo() - valor);
		destinatario.setSaldo(destinatario.getSaldo() + valor);
		repository.save(rementente);
		repository.save(destinatario);
	}
	
	private void verificarDadosUnicos(String email, String documento) {
		if(repository.findByEmail(email) != null)
			throw new DadoUnicoException("Esse email já foi cadastrado para outro usuário.");
		if(repository.findByDocumento(documento) != null)
			throw new DadoUnicoException("Esse documento já foi cadastrado para outro usuário.");
	}
}
