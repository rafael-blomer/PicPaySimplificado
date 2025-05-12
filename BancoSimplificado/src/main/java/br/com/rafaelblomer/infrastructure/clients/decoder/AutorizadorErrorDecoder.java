package br.com.rafaelblomer.infrastructure.clients.decoder;

import br.com.rafaelblomer.business.exceptions.TransferenciaNaoAutorizadaException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
public class AutorizadorErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == HttpStatus.FORBIDDEN.value() && methodKey.contains("autorizarTransferencia")) {
            try (InputStream bodyIs = response.body().asInputStream()) {
                String body = new String(bodyIs.readAllBytes(), StandardCharsets.UTF_8);
                return new TransferenciaNaoAutorizadaException("Transferência não autorizada pelo serviço externo: " + body);
            } catch (IOException e) {
                return new TransferenciaNaoAutorizadaException("Erro ao ler a resposta do serviço de autorização.");
            }
        }
        return feign.FeignException.errorStatus(methodKey, response);
    }
}