package br.edu.poo.permissoes.domain.permission;

import br.edu.poo.permissoes.shared.exception.DominioException;

public final class Permissao {

    private final PermissaoId id;
    private final String descricao;

    public Permissao(PermissaoId id, String descricao) {
        if (descricao == null || descricao.isBlank()) {
            throw new DominioException("Descricao de permissao invalida.");
        }
        this.id = id;
        this.descricao = descricao.trim();
    }

    public PermissaoId id() {
        return id;
    }

    public String codigo() {
        return id.valor();
    }

    public String descricao() {
        return descricao;
    }

    public String descricaoCompleta() {
        return codigo() + " - " + descricao;
    }

    @Override
    public String toString() {
        return descricaoCompleta();
    }
}
