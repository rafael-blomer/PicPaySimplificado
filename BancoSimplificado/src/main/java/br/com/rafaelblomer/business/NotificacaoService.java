package br.com.rafaelblomer.business;

import org.springframework.stereotype.Service;

import br.com.rafaelblomer.infrastructure.clients.NotificacaoClient;
import br.com.rafaelblomer.infrastructure.clients.request.NotificacaoRequest;
import br.com.rafaelblomer.infrastructure.entities.Usuario;

@Service
public class NotificacaoService {

    private final NotificacaoClient notificacaoClient;

    public NotificacaoService(NotificacaoClient notificacaoClient) {
        this.notificacaoClient = notificacaoClient;
    }

    public void notificarPagamentoRecebido(Usuario destinatario, Double valor) {
        String mensagem = String.format("Você recebeu um pagamento de R$ %.2f.", valor);
        NotificacaoRequest request = new NotificacaoRequest(destinatario.getEmail(), mensagem);
        enviarNotificacao(request);
    }

    public void notificarPagamentoRecebidoLojista(Usuario lojista, Double valor) {
        String mensagem = String.format("Você recebeu um pagamento de R$ %.2f em sua loja.", valor);
        NotificacaoRequest request = new NotificacaoRequest(lojista.getEmail(), mensagem); 
        enviarNotificacao(request);
    }

    private void enviarNotificacao(NotificacaoRequest request) {
        int maxRetries = 3;
        long delayMillis = 1000;

        for (int i = 0; i < maxRetries; i++) {
            try {
                notificacaoClient.enviarNotificacao(request);
                System.out.println("Notificação enviada para: " + request.para() + " (Tentativa " + (i + 1) + ")");
                return; 
            } catch (Exception e) {
                System.err.println("Erro ao enviar notificação para: " + request.para() + " (Tentativa " + (i + 1) + "). Erro: " + e.getMessage());
                if (i < maxRetries - 1) {
                    try {
                        Thread.sleep(delayMillis);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }
        System.err.println("Falha ao enviar notificação para: " + request.para() + " após " + maxRetries + " tentativas.");
    }
}