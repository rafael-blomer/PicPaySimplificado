package br.com.rafaelblomer.infrastructure.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.rafaelblomer.infrastructure.clients.request.NotificacaoRequest;

@FeignClient(name = "notificacaoCliente", url = "https://util.devi.tools/api/v1")
public interface NotificacaoClient {

    @PostMapping("/notify")
    void enviarNotificacao(@RequestBody NotificacaoRequest notificacaoRequest);
}