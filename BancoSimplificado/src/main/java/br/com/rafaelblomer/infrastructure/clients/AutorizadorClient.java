package br.com.rafaelblomer.infrastructure.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.rafaelblomer.infrastructure.clients.config.AutorizadorFeignConfig;
import br.com.rafaelblomer.infrastructure.clients.response.AutorizacaoResponse;

@FeignClient(name = "autorizadorCliente", url = "https://util.devi.tools/api/v2", configuration = AutorizadorFeignConfig.class)
public interface AutorizadorClient {

	@GetMapping("/authorize")
    AutorizacaoResponse autorizarTransferencia();
}
