package br.edu.poo.permissoes.domain.access;

public record DecisaoAcesso(ResultadoAcesso resultado, String motivo) {

    public static DecisaoAcesso autorizado(String motivo) {
        return new DecisaoAcesso(ResultadoAcesso.AUTORIZADO, motivo);
    }

    public static DecisaoAcesso negado(String motivo) {
        return new DecisaoAcesso(ResultadoAcesso.NEGADO, motivo);
    }

    public boolean autorizado() {
        return resultado == ResultadoAcesso.AUTORIZADO;
    }
}
