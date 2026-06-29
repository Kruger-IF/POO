package br.edu.poo.permissoes.domain.user;

import br.edu.poo.permissoes.shared.exception.DominioException;

public record UsuarioId(String valor) {

    public UsuarioId {
        if (valor == null || valor.isBlank()) {
            throw new DominioException("Identificador de usuario invalido.");
        }
        valor = valor.trim();
    }

    public static UsuarioId de(String valor) {
        return new UsuarioId(valor);
    }
}
