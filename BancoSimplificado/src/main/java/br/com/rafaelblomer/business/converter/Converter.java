package br.com.rafaelblomer.business.converter;

import org.springframework.stereotype.Component;

import br.com.rafaelblomer.business.dtos.TransacaoDTO;
import br.com.rafaelblomer.business.dtos.UsuarioRequestDTO;
import br.com.rafaelblomer.business.dtos.UsuarioResponseDTO;
import br.com.rafaelblomer.infrastructure.entities.Transacao;
import br.com.rafaelblomer.infrastructure.entities.Usuario;

@Component
public class Converter {
	
	public TransacaoDTO paraTransacaoDTO(Transacao entity) {
		return new TransacaoDTO(
				entity.getValor(),
				entity.getRemetente().getId(), 
				entity.getDestinatario().getId()
				);
	}
	
	public UsuarioResponseDTO paraUsuarioResponse(Usuario usuario) {
		return new UsuarioResponseDTO(
				usuario.getId(),
				usuario.getNomeCompleto(), 
				usuario.getDocumento(), 
				usuario.getEmail(),
				usuario.getSaldo()
				);
	}

	public Usuario paraUsuarioEntity(UsuarioRequestDTO usuarioDTO) {
		return new Usuario(
				usuarioDTO.nome(),
				usuarioDTO.documento(),
				usuarioDTO.email(),
				usuarioDTO.senha()
				);
	}
}
