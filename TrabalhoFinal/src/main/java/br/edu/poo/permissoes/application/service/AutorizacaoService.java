package br.edu.poo.permissoes.application.service;

import br.edu.poo.permissoes.domain.access.DecisaoAcesso;
import br.edu.poo.permissoes.domain.user.UsuarioId;

public interface AutorizacaoService {

    DecisaoAcesso verificarAcesso(UsuarioId usuarioId, String codigoPermissao);
}
