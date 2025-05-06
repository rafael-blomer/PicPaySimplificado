package br.com.rafaelblomer.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.rafaelblomer.infrastructure.entities.Transacao;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

}
