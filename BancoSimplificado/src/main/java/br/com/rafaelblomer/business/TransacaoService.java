package br.com.rafaelblomer.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.rafaelblomer.business.converter.Converter;
import br.com.rafaelblomer.business.dtos.TransacaoDTO;
import br.com.rafaelblomer.business.exceptions.SaldoInsuficienteException;
import br.com.rafaelblomer.business.exceptions.TransferenciaDeLojistaException;
import br.com.rafaelblomer.business.exceptions.TransferenciaNaoAutorizadaException;
import br.com.rafaelblomer.business.exceptions.UsuarioNaoEncontradoException;
import br.com.rafaelblomer.infrastructure.clients.AutorizadorClient;
import br.com.rafaelblomer.infrastructure.clients.response.AutorizacaoResponse;
import br.com.rafaelblomer.infrastructure.entities.Transacao;
import br.com.rafaelblomer.infrastructure.entities.Usuario;
import br.com.rafaelblomer.infrastructure.entities.UsuarioLojista;
import br.com.rafaelblomer.infrastructure.repositories.TransacaoRepository;
import jakarta.transaction.Transactional;

@Service
public class TransacaoService {
	
    @Autowired
    private AutorizadorClient autorizadorClient;

	@Autowired
	private TransacaoRepository repository;
	
	@Autowired
	private UsuariosService usuariosService;
	
	@Autowired
	private Converter converter;
	
	@Autowired
	private NotificacaoService notificacaoService;
	
	@Transactional
	public TransacaoDTO novaTransacao(TransacaoDTO dto) {
		Usuario remetente = usuariosService.buscaUsuarioEntity(dto.remetente());
		Usuario destinatario = usuariosService.buscaUsuarioEntity(dto.destinatario());
		verificarTransferencia(remetente, destinatario, dto.valor());
		verificarAutorizacaoDeServico();
		usuariosService.atualizarSaldoConta(remetente, destinatario, dto.valor());
		Transacao transacao = new Transacao(remetente, destinatario, true, dto.valor());
		repository.save(transacao);
        TransacaoDTO transacaoDTO = converter.paraTransacaoDTO(transacao);

        notificacaoService.notificarPagamentoRecebido(destinatario, dto.valor());

        return transacaoDTO;
	}


	//Métodos utilitários
	
	private void verificarTransferencia(Usuario remetente, Usuario destinatario, Double valor) {
		if (remetente == null)
			throw new UsuarioNaoEncontradoException("O usuário rementente não foi encontrado.");
		if(destinatario ==  null)
			throw new UsuarioNaoEncontradoException("O usuário destinatário não foi encontrado.");
		if (remetente instanceof UsuarioLojista)
			throw new TransferenciaDeLojistaException("Usuários de tipo 'Lojista' não podem realizar transações.");
		if (remetente.getSaldo() < valor)
			throw new SaldoInsuficienteException("O usuário remetente não possui saldo suficiente para realizar a transação.");
	}
	
	private void verificarAutorizacaoDeServico() {
		try {
	        AutorizacaoResponse response = autorizadorClient.autorizarTransferencia();
	        if (!"success".equalsIgnoreCase(response.status())) {
	            throw new TransferenciaNaoAutorizadaException("Transferência não autorizada pelo serviço externo.");
	        }
	    } catch (feign.FeignException e) {
	        if (e.status() == HttpStatus.FORBIDDEN.value()) {
	            throw new TransferenciaNaoAutorizadaException("Transferência não autorizada pelo serviço externo.");
	        }
	        throw e; 
	    }
	}
	
}
