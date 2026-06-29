package br.edu.poo.permissoes.domain.user;

public final class UsuarioComum extends Usuario {

    public UsuarioComum(String email, String nome) {
        super(email, nome);
    }

    @Override
    public TipoUsuario tipo() {
        return TipoUsuario.COMUM;
    }
}
