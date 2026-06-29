package br.edu.poo.permissoes.domain.user;

public final class UsuarioSistema extends Usuario {

    public UsuarioSistema(String email, String nome) {
        super(email, nome);
    }

    @Override
    public TipoUsuario tipo() {
        return TipoUsuario.SISTEMA;
    }
}
