package br.edu.poo.permissoes.domain.permission;

import br.edu.poo.permissoes.shared.exception.DominioException;

public record PermissaoId(String valor) {

    public PermissaoId {
        if (valor == null || valor.isBlank()) {
            throw new DominioException("Codigo de permissao invalido.");
        }
        valor = valor.trim();
    }

    public static PermissaoId de(String valor) {
        return new PermissaoId(valor);
    }
}
