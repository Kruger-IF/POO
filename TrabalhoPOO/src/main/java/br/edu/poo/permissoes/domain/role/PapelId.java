package br.edu.poo.permissoes.domain.role;

import br.edu.poo.permissoes.shared.exception.DominioException;

public record PapelId(String valor) {

    public PapelId {
        if (valor == null || valor.isBlank()) {
            throw new DominioException("Identificador de papel invalido.");
        }
        valor = valor.trim();
    }

    public static PapelId de(String valor) {
        return new PapelId(valor);
    }
}
