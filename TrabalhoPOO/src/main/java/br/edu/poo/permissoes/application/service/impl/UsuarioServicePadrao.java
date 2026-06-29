package br.edu.poo.permissoes.application.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import br.edu.poo.permissoes.application.service.AuditoriaService;
import br.edu.poo.permissoes.application.service.PermissoesEfetivasCalculator;
import br.edu.poo.permissoes.application.service.UsuarioService;
import br.edu.poo.permissoes.domain.audit.RegistroAlteracao;
import br.edu.poo.permissoes.domain.role.Papel;
import br.edu.poo.permissoes.domain.role.PapelId;
import br.edu.poo.permissoes.domain.user.Usuario;
import br.edu.poo.permissoes.domain.user.UsuarioId;
import br.edu.poo.permissoes.infrastructure.repository.PapelRepository;
import br.edu.poo.permissoes.infrastructure.repository.UsuarioRepository;
import br.edu.poo.permissoes.shared.exception.DominioException;

public final class UsuarioServicePadrao implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PapelRepository papelRepository;
    private final PermissoesEfetivasCalculator permissoesEfetivasCalculator;
    private final AuditoriaService auditoriaService;

    public UsuarioServicePadrao(
        UsuarioRepository usuarioRepository,
        PapelRepository papelRepository,
        PermissoesEfetivasCalculator permissoesEfetivasCalculator,
        AuditoriaService auditoriaService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.papelRepository = papelRepository;
        this.permissoesEfetivasCalculator = permissoesEfetivasCalculator;
        this.auditoriaService = auditoriaService;
    }

    @Override
    public Usuario cadastrar(Usuario usuario) {
        usuarioRepository.salvar(usuario);
        auditoriaService.registrarAlteracao(
            new RegistroAlteracao("Usuario", "CADASTRO", usuario.identificador(), LocalDateTime.now())
        );
        return usuario;
    }

    @Override
    public void associarPapel(UsuarioId usuarioId, PapelId papelId) {
        Usuario usuario = consultar(usuarioId);
        Papel papel = papelRepository.buscarPorId(papelId)
            .orElseThrow(() -> new DominioException("Papel inexistente: " + papelId.valor()));
        usuario.associarPapel(papel.id());
        auditoriaService.registrarAlteracao(
            new RegistroAlteracao("Usuario", "ASSOCIAR_PAPEL", usuario.identificador() + " -> " + papel.nome(), LocalDateTime.now())
        );
    }

    @Override
    public void removerPapel(UsuarioId usuarioId, PapelId papelId) {
        Usuario usuario = consultar(usuarioId);
        usuario.removerPapel(papelId);
        auditoriaService.registrarAlteracao(
            new RegistroAlteracao("Usuario", "REMOVER_PAPEL", usuario.identificador() + " -> " + papelId.valor(), LocalDateTime.now())
        );
    }

    @Override
    public void bloquear(UsuarioId usuarioId, String motivo) {
        Usuario usuario = consultar(usuarioId);
        usuario.bloquear();
        auditoriaService.registrarAlteracao(
            new RegistroAlteracao("Usuario", "BLOQUEAR", usuario.identificador() + " | " + motivo, LocalDateTime.now())
        );
    }

    @Override
    public void desbloquear(UsuarioId usuarioId, String motivo) {
        Usuario usuario = consultar(usuarioId);
        usuario.desbloquear();
        auditoriaService.registrarAlteracao(
            new RegistroAlteracao("Usuario", "DESBLOQUEAR", usuario.identificador() + " | " + motivo, LocalDateTime.now())
        );
    }

    @Override
    public Set<String> listarPermissoesEfetivas(UsuarioId usuarioId) {
        Usuario usuario = consultar(usuarioId);
        return permissoesEfetivasCalculator.calcular(usuario);
    }

    @Override
    public Usuario consultar(UsuarioId usuarioId) {
        return usuarioRepository.buscarPorId(usuarioId)
            .orElseThrow(() -> new DominioException("Usuario inexistente: " + usuarioId.valor()));
    }

    @Override
    public List<Usuario> listarTodos() {
        return usuarioRepository.listarTodos();
    }
}
