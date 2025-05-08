package br.com.rafaelblomer.business.converter;

import org.springframework.stereotype.Component;

import br.com.rafaelblomer.business.dtos.TransacaoDTO;
import br.com.rafaelblomer.infrastructure.entities.Transacao;

@Component
public class Converter {
	
	public TransacaoDTO paraTransacaoDTO(Transacao entity) {
		return new TransacaoDTO(
				entity.getValor(),
				entity.getRemetente().getId(), 
				entity.getDestinatario().getId()
				);
	}

}
