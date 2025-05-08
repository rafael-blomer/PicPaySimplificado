package br.com.rafaelblomer.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.rafaelblomer.business.converter.Converter;
import br.com.rafaelblomer.business.dtos.TransacaoDTO;
import br.com.rafaelblomer.infrastructure.entities.Transacao;
import br.com.rafaelblomer.infrastructure.entities.Usuario;
import br.com.rafaelblomer.infrastructure.entities.UsuarioLojista;
import br.com.rafaelblomer.infrastructure.repositories.TransacaoRepository;
import jakarta.transaction.Transactional;

@Service
public class TransacaoService {

	@Autowired
	private TransacaoRepository repository;
	
	@Autowired
	private UsuariosService usuariosService;
	
	@Autowired
	private Converter converter;
	
	@Transactional
	public TransacaoDTO novaTransacao(TransacaoDTO dto) {
		Usuario remetente = usuariosService.buscarUmUsuario(dto.remetente());
		Usuario destinatario = usuariosService.buscarUmUsuario(dto.destinatario());
		verificarTransferencia(remetente, dto.valor());
		verificarDisponibilidadeServico();
		usuariosService.atualizarSaldoConta(remetente, destinatario, dto.valor());
		Transacao transacao = new Transacao(remetente, destinatario, true, dto.valor());
		repository.save(transacao);
		return converter.paraTransacaoDTO(transacao);
	}


	//Métodos utilitários
	
	//TODO: criar exceções personalizadas
	private void verificarTransferencia(Usuario remetente, Double valor) {
		if (remetente instanceof UsuarioLojista)
			throw new RuntimeException();
		if (remetente.getSaldo() < valor)
			throw new RuntimeException();
	}
	
	//TODO: fazer a verificação da disponibilidade
	private void verificarDisponibilidadeServico() {
		
	}
}
