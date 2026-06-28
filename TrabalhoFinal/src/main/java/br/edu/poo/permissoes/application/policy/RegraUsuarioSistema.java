package br.edu.poo.permissoes.application.policy;

import java.util.Optional;
import java.util.Set;

import br.edu.poo.permissoes.domain.access.DecisaoAcesso;
import br.edu.poo.permissoes.domain.user.TipoUsuario;
import br.edu.poo.permissoes.domain.user.Usuario;

public final class RegraUsuarioSistema implements RegraAcesso {

    @Override
    public Optional<DecisaoAcesso> avaliar(Usuario usuario, String codigoPermissao, Set<String> permissoesEfetivas) {
        if (usuario.tipo() != TipoUsuario.SISTEMA) {
            return Optional.empty();
        }
        if (codigoPermissao.startsWith("SYSTEM_")) {
            return Optional.of(DecisaoAcesso.autorizado("Usuario de sistema autorizado para acao de sistema."));
        }
        return Optional.empty();
    }
}
