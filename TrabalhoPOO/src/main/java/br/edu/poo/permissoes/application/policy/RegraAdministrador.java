package br.edu.poo.permissoes.application.policy;

import java.util.Optional;
import java.util.Set;

import br.edu.poo.permissoes.domain.access.DecisaoAcesso;
import br.edu.poo.permissoes.domain.user.TipoUsuario;
import br.edu.poo.permissoes.domain.user.Usuario;

// Regra para Administrador: acesso baseado nas permissoes efetivas dos papeis atribuidos.
// Correcao: administrador NAO e onipotente por tipo — precisa ter o papel com a permissao.
// Diferencial: administrador acessa mesmo estando bloqueado (recuperacao do sistema),
// desde que possua a permissao nos seus papeis. Usuarios comuns bloqueados sao barrados
// antes desta regra pela RegraUsuarioBloqueado.
public final class RegraAdministrador implements RegraAcesso {

    @Override
    public Optional<DecisaoAcesso> avaliar(Usuario usuario, String codigoPermissao, Set<String> permissoesEfetivas) {
        if (usuario.tipo() != TipoUsuario.ADMINISTRADOR) {
            return Optional.empty();
        }
        if (usuario.papeis().vazio()) {
            return Optional.of(DecisaoAcesso.negado("Administrador sem papeis atribuidos."));
        }
        if (permissoesEfetivas.contains(codigoPermissao)) {
            return Optional.of(DecisaoAcesso.autorizado(
                "Administrador autorizado pela permissao presente em seus papeis."));
        }
        return Optional.of(DecisaoAcesso.negado(
            "Administrador nao possui a permissao '" + codigoPermissao + "' em nenhum papel atribuido."));
    }
}
