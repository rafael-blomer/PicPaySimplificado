package br.com.rafaelblomer.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.rafaelblomer.business.NotificacaoService;
import br.com.rafaelblomer.business.TransacaoService;
import br.com.rafaelblomer.business.UsuariosService;
import br.com.rafaelblomer.business.converter.Converter;
import br.com.rafaelblomer.business.dtos.TransacaoDTO;
import br.com.rafaelblomer.business.exceptions.SaldoInsuficienteException;
import br.com.rafaelblomer.business.exceptions.TransferenciaDeLojistaException;
import br.com.rafaelblomer.business.exceptions.TransferenciaNaoAutorizadaException;
import br.com.rafaelblomer.business.exceptions.UsuarioNaoEncontradoException;
import br.com.rafaelblomer.infrastructure.clients.AutorizadorClient;
import br.com.rafaelblomer.infrastructure.clients.response.AutorizacaoData;
import br.com.rafaelblomer.infrastructure.clients.response.AutorizacaoResponse;
import br.com.rafaelblomer.infrastructure.entities.Transacao;
import br.com.rafaelblomer.infrastructure.entities.Usuario;
import br.com.rafaelblomer.infrastructure.entities.UsuarioLojista;
import br.com.rafaelblomer.infrastructure.repositories.TransacaoRepository;

@SpringBootTest
public class TransacaoServiceTest {

    @InjectMocks
    private TransacaoService service;

    @Mock
    private UsuariosService usuariosService;

    @Mock
    private AutorizadorClient autorizadorClient;

    @Mock
    private TransacaoRepository repository;

    @Mock
    private Converter converter;

    @Mock
    private NotificacaoService notificacaoService;

    private Usuario remetente;
    private Usuario destinatario;
    private TransacaoDTO dto;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        remetente = new Usuario();
        remetente.setId(1L);
        remetente.setSaldo(100.0);

        destinatario = new Usuario();
        destinatario.setId(2L);
        destinatario.setSaldo(50.0);

        dto = new TransacaoDTO(30.0, remetente.getId(), destinatario.getId());
    }

    @Test
    public void deveRealizarTransacaoComSucesso() {
        when(usuariosService.buscaUsuarioEntity(1L)).thenReturn(remetente);
        when(usuariosService.buscaUsuarioEntity(2L)).thenReturn(destinatario);
        when(autorizadorClient.autorizarTransferencia()).thenReturn(new AutorizacaoResponse("success", new AutorizacaoData(true)));
        when(converter.paraTransacaoDTO(any(Transacao.class))).thenReturn(dto);

        TransacaoDTO resultado = service.novaTransacao(dto);

        assertNotNull(resultado);
        verify(repository).save(any(Transacao.class));
        verify(notificacaoService).notificarPagamentoRecebido(destinatario, 30.0);
    }

    @Test
    public void deveLancarErro_UsuarioRemetenteNaoEncontrado() {
        when(usuariosService.buscaUsuarioEntity(1L)).thenReturn(null);
        when(usuariosService.buscaUsuarioEntity(2L)).thenReturn(destinatario);

        assertThrows(UsuarioNaoEncontradoException.class, () -> service.novaTransacao(dto));
    }

    @Test
    public void deveLancarErro_UsuarioDestinatarioNaoEncontrado() {
        when(usuariosService.buscaUsuarioEntity(1L)).thenReturn(remetente);
        when(usuariosService.buscaUsuarioEntity(2L)).thenReturn(null);

        assertThrows(UsuarioNaoEncontradoException.class, () -> service.novaTransacao(dto));
    }

    @Test
    public void deveLancarErro_TransferenciaDeLojista() {
        Usuario lojista = new UsuarioLojista();
        lojista.setSaldo(100.0);

        when(usuariosService.buscaUsuarioEntity(1L)).thenReturn(lojista);
        when(usuariosService.buscaUsuarioEntity(2L)).thenReturn(destinatario);

        assertThrows(TransferenciaDeLojistaException.class, () -> service.novaTransacao(dto));
    }

    @Test
    public void deveLancarErro_SaldoInsuficiente() {
        remetente.setSaldo(10.0);

        when(usuariosService.buscaUsuarioEntity(1L)).thenReturn(remetente);
        when(usuariosService.buscaUsuarioEntity(2L)).thenReturn(destinatario);

        assertThrows(SaldoInsuficienteException.class, () -> service.novaTransacao(dto));
    }

    @Test
    public void deveLancarErro_TransferenciaNaoAutorizada() {
        when(usuariosService.buscaUsuarioEntity(1L)).thenReturn(remetente);
        when(usuariosService.buscaUsuarioEntity(2L)).thenReturn(destinatario);
        when(autorizadorClient.autorizarTransferencia()).thenReturn(new AutorizacaoResponse("fail", new AutorizacaoData(false)));

        assertThrows(TransferenciaNaoAutorizadaException.class, () -> service.novaTransacao(dto));
    }
}
