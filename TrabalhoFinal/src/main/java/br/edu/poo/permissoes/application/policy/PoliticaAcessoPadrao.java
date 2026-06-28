package br.edu.poo.permissoes.application.policy;

import java.util.List;
import java.util.Set;

import br.edu.poo.permissoes.domain.access.DecisaoAcesso;
import br.edu.poo.permissoes.domain.user.Usuario;

public final class PoliticaAcessoPadrao implements PoliticaAcesso {

    private final List<RegraAcesso> regras;

    public PoliticaAcessoPadrao() {
        this.regras = List.of(
            new RegraUsuarioBloqueado(),
            new RegraAdministrador(),
            new RegraUsuarioSistema(),
            new RegraPermissaoEfetiva()
        );
    }

    @Override
    public DecisaoAcesso avaliar(Usuario usuario, String codigoPermissao, Set<String> permissoesEfetivas) {
        for (RegraAcesso regra : regras) {
            var decisao = regra.avaliar(usuario, codigoPermissao, permissoesEfetivas);
            if (decisao.isPresent()) {
                return decisao.get();
            }
        }
        return DecisaoAcesso.negado("Permissao ausente para a acao solicitada.");
    }
}
