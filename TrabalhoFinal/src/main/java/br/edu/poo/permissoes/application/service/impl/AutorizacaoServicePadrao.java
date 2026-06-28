package br.edu.poo.permissoes.application.service.impl;

import java.time.LocalDateTime;
import java.util.Set;

import br.edu.poo.permissoes.application.policy.PoliticaAcesso;
import br.edu.poo.permissoes.application.service.AuditoriaService;
import br.edu.poo.permissoes.application.service.AutorizacaoService;
import br.edu.poo.permissoes.domain.access.DecisaoAcesso;
import br.edu.poo.permissoes.domain.audit.RegistroAcesso;
import br.edu.poo.permissoes.domain.user.Usuario;
import br.edu.poo.permissoes.domain.user.UsuarioId;
import br.edu.poo.permissoes.infrastructure.repository.PapelRepository;
import br.edu.poo.permissoes.infrastructure.repository.PermissaoRepository;
import br.edu.poo.permissoes.infrastructure.repository.UsuarioRepository;
import br.edu.poo.permissoes.shared.exception.DominioException;

public final class AutorizacaoServicePadrao implements AutorizacaoService {

    private final UsuarioRepository usuarioRepository;
    private final PapelRepository papelRepository;
    private final PermissaoRepository permissaoRepository;
    private final PoliticaAcesso politicaAcesso;
    private final AuditoriaService auditoriaService;

    public AutorizacaoServicePadrao(
        UsuarioRepository usuarioRepository,
        PapelRepository papelRepository,
        PermissaoRepository permissaoRepository,
        PoliticaAcesso politicaAcesso,
        AuditoriaService auditoriaService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.papelRepository = papelRepository;
        this.permissaoRepository = permissaoRepository;
        this.politicaAcesso = politicaAcesso;
        this.auditoriaService = auditoriaService;
    }

    @Override
    public DecisaoAcesso verificarAcesso(UsuarioId usuarioId, String codigoPermissao) {
        Usuario usuario = consultarUsuario(usuarioId);
        Set<String> permissoesEfetivas = calcularPermissoesEfetivas(usuario);
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

    private Set<String> calcularPermissoesEfetivas(Usuario usuario) {
        var permissoes = new java.util.LinkedHashSet<String>();
        for (var papelId : usuario.papeis()) {
            var papel = papelRepository.buscarPorId(papelId)
                .orElseThrow(() -> new DominioException("Papel inexistente: " + papelId.valor()));
            for (var permissaoId : papel.permissoes()) {
                permissaoRepository.buscarPorId(permissaoId)
                    .ifPresent(permissao -> permissoes.add(permissao.codigo()));
            }
        }
        return Set.copyOf(permissoes);
    }
}
