package br.edu.poo.permissoes.application.policy;

import java.util.Optional;
import java.util.Set;

import br.edu.poo.permissoes.domain.access.DecisaoAcesso;
import br.edu.poo.permissoes.domain.user.Usuario;

public final class RegraPermissaoEfetiva implements RegraAcesso {

    @Override
    public Optional<DecisaoAcesso> avaliar(Usuario usuario, String codigoPermissao, Set<String> permissoesEfetivas) {
        if (permissoesEfetivas.contains(codigoPermissao)) {
            return Optional.of(DecisaoAcesso.autorizado("Permissao presente nas atribuicoes do usuario."));
        }
        return Optional.empty();
    }
}
