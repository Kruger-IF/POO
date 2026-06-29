package br.edu.poo.permissoes.domain.user;

public final class Administrador extends Usuario {

    public Administrador(String email, String nome) {
        super(email, nome);
    }

    @Override
    public TipoUsuario tipo() {
        return TipoUsuario.ADMINISTRADOR;
    }
}
