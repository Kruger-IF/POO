package br.edu.poo.permissoes.domain.user;

import br.edu.poo.permissoes.domain.collection.ConjuntoPapeis;
import br.edu.poo.permissoes.domain.role.PapelId;
import br.edu.poo.permissoes.shared.exception.DominioException;

public abstract class Usuario {

    private final UsuarioId id;
    private final String nome;
    private final String email;
    private final ConjuntoPapeis papeis;
    private boolean bloqueado;

    protected Usuario(String email, String nome) {
        if (email == null || email.isBlank()) {
            throw new DominioException("Email de usuario invalido.");
        }
        if (nome == null || nome.isBlank()) {
            throw new DominioException("Nome de usuario invalido.");
        }
        this.id = UsuarioId.de(email);
        this.email = email.trim();
        this.nome = nome.trim();
        this.papeis = new ConjuntoPapeis();
    }

    public final UsuarioId id() {
        return id;
    }

    public final String email() {
        return email;
    }

    public final String nome() {
        return nome;
    }

    public final boolean estaBloqueado() {
        return bloqueado;
    }

    public final void bloquear() {
        bloqueado = true;
    }

    public final void desbloquear() {
        bloqueado = false;
    }

    public final void associarPapel(PapelId papelId) {
        papeis.adicionar(papelId);
    }

    public final void removerPapel(PapelId papelId) {
        papeis.remover(papelId);
    }

    public final ConjuntoPapeis papeis() {
        return papeis;
    }

    public final String identificador() {
        return nome + " <" + email + ">";
    }

    public abstract TipoUsuario tipo();
}
