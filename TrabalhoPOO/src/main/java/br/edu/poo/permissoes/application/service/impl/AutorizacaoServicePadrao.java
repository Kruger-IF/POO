package br.edu.poo.permissoes.application.service.impl;

import java.time.LocalDateTime;
import java.util.Set;

import br.edu.poo.permissoes.application.policy.PoliticaAcesso;
import br.edu.poo.permissoes.application.service.AuditoriaService;
import br.edu.poo.permissoes.application.service.AutorizacaoService;
import br.edu.poo.permissoes.application.service.PermissoesEfetivasCalculator;
import br.edu.poo.permissoes.domain.access.DecisaoAcesso;
import br.edu.poo.permissoes.domain.audit.RegistroAcesso;
import br.edu.poo.permissoes.domain.user.Usuario;
import br.edu.poo.permissoes.domain.user.UsuarioId;
import br.edu.poo.permissoes.infrastructure.repository.UsuarioRepository;
import br.edu.poo.permissoes.shared.exception.DominioException;

public final class AutorizacaoServicePadrao implements AutorizacaoService {

    private final UsuarioRepository usuarioRepository;
    private final PermissoesEfetivasCalculator permissoesEfetivasCalculator;
    private final PoliticaAcesso politicaAcesso;
    private final AuditoriaService auditoriaService;

    public AutorizacaoServicePadrao(
        UsuarioRepository usuarioRepository,
        PermissoesEfetivasCalculator permissoesEfetivasCalculator,
        PoliticaAcesso politicaAcesso,
        AuditoriaService auditoriaService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.permissoesEfetivasCalculator = permissoesEfetivasCalculator;
        this.politicaAcesso = politicaAcesso;
        this.auditoriaService = auditoriaService;
    }

    @Override
    public DecisaoAcesso verificarAcesso(UsuarioId usuarioId, String codigoPermissao) {
        Usuario usuario = consultarUsuario(usuarioId);
        Set<String> permissoesEfetivas = permissoesEfetivasCalculator.calcular(usuario);
        DecisaoAcesso decisao = politicaAcesso.avaliar(usuario, codigoPermissao, permissoesEfetivas);
        auditoriaService.registrarAcesso(
            new RegistroAcesso(
                usuario.identificador(),
                codigoPermissao,
                decisao.resultado(),
                decisao.motivo(),
                LocalDateTime.now()
            )
        );
        return decisao;
    }

    private Usuario consultarUsuario(UsuarioId usuarioId) {
        return usuarioRepository.buscarPorId(usuarioId)
            .orElseThrow(() -> new DominioException("Usuario inexistente: " + usuarioId.valor()));
    }
}
