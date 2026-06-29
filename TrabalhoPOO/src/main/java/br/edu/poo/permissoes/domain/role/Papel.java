package br.edu.poo.permissoes.domain.role;

import br.edu.poo.permissoes.domain.collection.ConjuntoPermissoes;
import br.edu.poo.permissoes.domain.permission.PermissaoId;

public final class Papel {

    private final PapelId id;
    private final String nome;
    private final ConjuntoPermissoes permissoes;

    public Papel(PapelId id, String nome) {
        this.id = id;
        this.nome = nome;
        this.permissoes = new ConjuntoPermissoes();
    }

    public PapelId id() {
        return id;
    }

    public String nome() {
        return nome;
    }

    public void associarPermissao(PermissaoId permissaoId) {
        permissoes.adicionar(permissaoId);
    }

    public void removerPermissao(PermissaoId permissaoId) {
        permissoes.remover(permissaoId);
    }

    public ConjuntoPermissoes permissoes() {
        return permissoes;
    }

    @Override
    public String toString() {
        return nome;
    }
}
