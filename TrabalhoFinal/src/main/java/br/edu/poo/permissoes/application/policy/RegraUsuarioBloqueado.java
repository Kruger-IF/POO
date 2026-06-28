package br.edu.poo.permissoes.application.policy;

import java.util.Optional;
import java.util.Set;

import br.edu.poo.permissoes.domain.access.DecisaoAcesso;
import br.edu.poo.permissoes.domain.user.Usuario;

public final class RegraUsuarioBloqueado implements RegraAcesso {

    @Override
    public Optional<DecisaoAcesso> avaliar(Usuario usuario, String codigoPermissao, Set<String> permissoesEfetivas) {
        if (usuario.estaBloqueado()) {
            return Optional.of(DecisaoAcesso.negado("Usuario bloqueado."));
        }
        return Optional.empty();
    }
}
