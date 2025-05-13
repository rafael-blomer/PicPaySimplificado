package br.com.rafaelblomer.infrastructure.clients.config;

import br.com.rafaelblomer.infrastructure.clients.decoder.AutorizadorErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AutorizadorFeignConfig {

    @Bean
    public AutorizadorErrorDecoder autorizadorErrorDecoder() {
        return new AutorizadorErrorDecoder();
    }
}