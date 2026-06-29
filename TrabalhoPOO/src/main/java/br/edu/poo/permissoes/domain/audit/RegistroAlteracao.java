package br.edu.poo.permissoes.domain.audit;

import java.time.LocalDateTime;

public record RegistroAlteracao(
    String entidade,
    String operacao,
    String detalhe,
    LocalDateTime dataHora
) {

    public String resumo() {
        return dataHora + " | " + entidade + " | " + operacao + " | " + detalhe;
    }
}
