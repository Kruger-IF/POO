package br.edu.poo.permissoes.domain.permission;

public final class Permissao {

    private final PermissaoId id;

    public Permissao(PermissaoId id) {
        this.id = id;
    }

    public PermissaoId id() {
        return id;
    }

    public String codigo() {
        return id.valor();
    }

    @Override
    public String toString() {
        return codigo();
    }
}
