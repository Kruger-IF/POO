package br.edu.poo.permissoes.application.policy;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import br.edu.poo.permissoes.domain.access.DecisaoAcesso;
import br.edu.poo.permissoes.domain.user.Usuario;

public final class PoliticaAcessoPadrao implements PoliticaAcesso {

    private final List<RegraAcesso> regras;

    public PoliticaAcessoPadrao() {
        // Ordem das regras importa:
        // 1. RegraAdministrador — admin acessa mesmo bloqueado (recuperacao do sistema);
        //    se nao for admin, passa para a proxima regra.
        // 2. RegraUsuarioSistema — processo automatizado ignora bloqueio manual;
        //    se nao for sistema, passa para a proxima regra.
        // 3. RegraUsuarioBloqueado — usuarios comuns bloqueados sao negados aqui.
        // 4. RegraPermissaoEfetiva — verificacao final para usuarios ativos.
        this.regras = List.of(
            new RegraAdministrador(),
            new RegraUsuarioSistema(),
            new RegraUsuarioBloqueado(),
            new RegraPermissaoEfetiva()
        );
    }

    @Override
    public DecisaoAcesso avaliar(Usuario usuario, String codigoPermissao, Set<String> permissoesEfetivas) {
        if (codigoPermissao == null || codigoPermissao.isBlank()) {
            return DecisaoAcesso.negado("Codigo de permissao invalido.");
        }
        for (RegraAcesso regra : regras) {
            Optional<DecisaoAcesso> decisao = regra.avaliar(usuario, codigoPermissao, permissoesEfetivas);
            if (decisao.isPresent()) {
                return decisao.get();
            }
        }
        return DecisaoAcesso.negado("Permissao ausente para a acao solicitada.");
    }
}
