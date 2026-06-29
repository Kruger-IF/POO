package br.edu.poo.permissoes.domain.collection;

import java.util.LinkedHashSet;
import java.util.Set;

import br.edu.poo.permissoes.domain.permission.PermissaoId;

public final class ConjuntoPermissoes {

    private final Set<PermissaoId> permissoes;

    public ConjuntoPermissoes() {
        this.permissoes = new LinkedHashSet<>();
    }

    public void adicionar(PermissaoId permissaoId) {
        permissoes.add(permissaoId);
    }

    public void remover(PermissaoId permissaoId) {
        permissoes.remove(permissaoId);
    }

    public Set<PermissaoId> itens() {
        return Set.copyOf(permissoes);
    }

    public boolean vazio() {
        return permissoes.isEmpty();
    }
}