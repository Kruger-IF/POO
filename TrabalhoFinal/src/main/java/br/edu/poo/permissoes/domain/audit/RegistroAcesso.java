package br.edu.poo.permissoes.domain.audit;

import java.time.LocalDateTime;

import br.edu.poo.permissoes.domain.access.ResultadoAcesso;

public record RegistroAcesso(
    String usuario,
    String acao,
    ResultadoAcesso resultado,
    String motivo,
    LocalDateTime dataHora
) {

    public String resumo() {
        return dataHora + " | " + usuario + " | " + acao + " | " + resultado + " | " + motivo;
    }
}
