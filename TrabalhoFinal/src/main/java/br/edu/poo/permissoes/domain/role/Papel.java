package br.edu.poo.permissoes.domain.role;

import java.util.LinkedHashSet;
import java.util.Set;

import br.edu.poo.permissoes.domain.permission.PermissaoId;

public final class Papel {

    private final PapelId id;
    private final String nome;
    private final Set<PermissaoId> permissoes;

    public Papel(PapelId id, String nome) {
        this.id = id;
        this.nome = nome;
        this.permissoes = new LinkedHashSet<>();
    }

    public PapelId id() {
        return id;
    }

    public String nome() {
        return nome;
    }

    public void associarPermissao(PermissaoId permissaoId) {
        permissoes.add(permissaoId);
    }

    public void removerPermissao(PermissaoId permissaoId) {
        permissoes.remove(permissaoId);
    }

    public Set<PermissaoId> permissoes() {
        return Set.copyOf(permissoes);
    }

    @Override
    public String toString() {
        return nome;
    }
}
