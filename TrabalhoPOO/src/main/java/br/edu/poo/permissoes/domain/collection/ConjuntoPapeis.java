package br.edu.poo.permissoes.domain.collection;

import java.util.LinkedHashSet;
import java.util.Set;

import br.edu.poo.permissoes.domain.role.PapelId;

public final class ConjuntoPapeis {

    private final Set<PapelId> papeis;

    public ConjuntoPapeis() {
        this.papeis = new LinkedHashSet<>();
    }

    public void adicionar(PapelId papelId) {
        papeis.add(papelId);
    }

    public void remover(PapelId papelId) {
        papeis.remove(papelId);
    }

    public Set<PapelId> itens() {
        return Set.copyOf(papeis);
    }

    public boolean vazio() {
        return papeis.isEmpty();
    }
}