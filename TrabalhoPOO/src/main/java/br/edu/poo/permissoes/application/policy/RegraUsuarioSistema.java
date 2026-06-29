package br.edu.poo.permissoes.application.policy;

import java.util.Optional;
import java.util.Set;

import br.edu.poo.permissoes.domain.access.DecisaoAcesso;
import br.edu.poo.permissoes.domain.user.TipoUsuario;
import br.edu.poo.permissoes.domain.user.Usuario;

// Regra para UsuarioSistema: processos automatizados nao sao bloqueados por operadores humanos.
// Correcao: a regra anterior so autorizava permissoes com prefixo "SYSTEM_", o que tornava
// a regra inutil para as permissoes normais (REPORT_VIEW, USER_CREATE, etc.).
// Agora: UsuarioSistema ignora o flag de bloqueio e acessa qualquer permissao
// que esteja presente nos seus papeis atribuidos — sem restricao de prefixo.
// Se nao tiver papeis, cai na regra padrao e e negado normalmente.
public final class RegraUsuarioSistema implements RegraAcesso {

    @Override
    public Optional<DecisaoAcesso> avaliar(Usuario usuario, String codigoPermissao, Set<String> permissoesEfetivas) {
        if (usuario.tipo() != TipoUsuario.SISTEMA) {
            return Optional.empty();
        }
        if (usuario.papeis().vazio()) {
            return Optional.of(DecisaoAcesso.negado("Usuario de sistema sem papeis atribuidos."));
        }
        if (permissoesEfetivas.contains(codigoPermissao)) {
            return Optional.of(DecisaoAcesso.autorizado(
                "Usuario de sistema autorizado pela permissao presente em seus papeis."));
        }
        return Optional.of(DecisaoAcesso.negado(
            "Usuario de sistema nao possui a permissao '" + codigoPermissao + "' em nenhum papel atribuido."));
    }
}
