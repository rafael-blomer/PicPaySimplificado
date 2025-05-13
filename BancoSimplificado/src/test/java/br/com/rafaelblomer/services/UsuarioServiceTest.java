package br.com.rafaelblomer.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.rafaelblomer.business.UsuariosService;
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

@SpringBootTest
public class UsuarioServiceTest {

    @InjectMocks
    private UsuariosService service;

    @Mock
    private UsuarioRepository repository;

    @Mock
    private Converter converter;

    private UsuarioLojista lojista;
    private UsuarioComum comum;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        lojista = new UsuarioLojista("Lojista", "111", "email1", "senha");
        comum = new UsuarioComum("Comum", "222", "email2", "senha");
    }

    @Test
    public void deveBuscarUmUsuarioPorId() {
        when(repository.findById(1L)).thenReturn(Optional.of(lojista));
        when(converter.paraUsuarioResponse(lojista)).thenReturn(new UsuarioResponseDTO(1L, "Lojista", "111", "email1", 0.0));

        UsuarioResponseDTO response = service.buscarUmUsuarioResponseDTO(1L);

        assertEquals("111", response.documento());
    }

    @Test
    public void deveLancarExcecao_UsuarioNaoEncontrado() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UsuarioNaoEncontradoException.class, () -> service.buscarUmUsuarioResponseDTO(1L));
    }

    @Test
    public void deveBuscarTodosUsuarios() {
        when(repository.findAll()).thenReturn(Arrays.asList(lojista, comum));
        when(converter.paraUsuarioResponse(any())).thenReturn(new UsuarioResponseDTO(1L, "User", "doc", "email", 0.0));

        List<UsuarioResponseDTO> usuarios = service.buscarTodosUsuarios();

        assertEquals(2, usuarios.size());
    }

    @Test
    public void deveExcluirUsuario_SemSaldo() {
        comum.setSaldo(0.0);
        when(repository.findById(1L)).thenReturn(Optional.of(comum));

        service.excluirUsuario(1L);

        verify(repository).delete(comum);
    }

    @Test
    public void deveLancarExcecao_ExcluirUsuarioComSaldo() {
        comum.setSaldo(10.0);
        when(repository.findById(1L)).thenReturn(Optional.of(comum));

        assertThrows(DinheiroEmContaException.class, () -> service.excluirUsuario(1L));
    }

    @Test
    public void deveDepositarDinheiro() {
        comum.setSaldo(100.0);
        when(repository.findById(1L)).thenReturn(Optional.of(comum));

        String resultado = service.depositarDinheiro(new DepositoDTO(1L, 50.0));

        assertEquals("Depósito realizado com sucesso", resultado);
        assertEquals(150.0, comum.getSaldo());
        verify(repository).save(comum);
    }

    @Test
    public void deveAlterarSenhaComSucesso() {
        when(repository.findById(1L)).thenReturn(Optional.of(comum));
        AlteracaoSenhaDTO dto = new AlteracaoSenhaDTO("senha", "novaSenha");

        service.alterarSenhaUsuario(1L, dto);

        assertEquals("novaSenha", comum.getSenha());
        verify(repository).save(comum);
    }

    @Test
    public void deveLancarExcecao_SenhaAntigaIncorreta() {
        when(repository.findById(1L)).thenReturn(Optional.of(comum));
        AlteracaoSenhaDTO dto = new AlteracaoSenhaDTO("errada", "novaSenha");

        assertThrows(AlteracaoSenhaException.class, () -> service.alterarSenhaUsuario(1L, dto));
    }

    @Test
    public void deveSalvarNovoLojista() {
        UsuarioRequestDTO dto = new UsuarioRequestDTO("Lojista", "111", "email1", "senha");
        when(repository.findByEmail("email1")).thenReturn(null);
        when(repository.findByDocumento("111")).thenReturn(null);

        when(converter.paraUsuarioEntity(dto)).thenReturn(lojista);
        when(converter.paraUsuarioResponse(any())).thenReturn(new UsuarioResponseDTO(1L, "Lojista", "111", "email1", 0.0));

        UsuarioResponseDTO response = service.novoLojista(dto);

        assertEquals("111", response.documento());
    }

    @Test
    public void deveSalvarNovoComum() {
        UsuarioRequestDTO dto = new UsuarioRequestDTO("Comum", "222", "email2", "senha");
        when(repository.findByEmail("email2")).thenReturn(null);
        when(repository.findByDocumento("222")).thenReturn(null);

        when(converter.paraUsuarioEntity(dto)).thenReturn(comum);
        when(converter.paraUsuarioResponse(any())).thenReturn(new UsuarioResponseDTO(1L, "Comum", "222", "email2", 0.0));

        UsuarioResponseDTO response = service.novoComum(dto);

        assertEquals("222", response.documento());
    }

    @Test
    public void deveAtualizarSaldoEntreUsuarios() {
        Usuario remetente = new UsuarioComum("A", "1", "a@mail", "senha");
        Usuario destinatario = new UsuarioComum("B", "2", "b@mail", "senha");
        remetente.setSaldo(100.0);
        destinatario.setSaldo(50.0);

        service.atualizarSaldoConta(remetente, destinatario, 25.0);

        assertEquals(75.0, remetente.getSaldo());
        assertEquals(75.0, destinatario.getSaldo());
        verify(repository).save(remetente);
        verify(repository).save(destinatario);
    }

    @Test
    public void deveLancarExcecao_EmailJaExistente() {
        when(repository.findByEmail("email1")).thenReturn(lojista);
        assertThrows(DadoUnicoException.class, () -> service.novoLojista(
                new UsuarioRequestDTO("Lojista", "111", "email1", "senha")));
    }

    @Test
    public void deveLancarExcecao_DocumentoJaExistente() {
        when(repository.findByEmail("email1")).thenReturn(null);
        when(repository.findByDocumento("111")).thenReturn(lojista);
        assertThrows(DadoUnicoException.class, () -> service.novoLojista(
                new UsuarioRequestDTO("Lojista", "111", "email1", "senha")));
    }
}