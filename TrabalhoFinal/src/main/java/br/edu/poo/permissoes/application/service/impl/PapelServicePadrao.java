package br.edu.poo.permissoes.application.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import br.edu.poo.permissoes.application.service.AuditoriaService;
import br.edu.poo.permissoes.application.service.PapelService;
import br.edu.poo.permissoes.domain.audit.RegistroAlteracao;
import br.edu.poo.permissoes.domain.permission.Permissao;
import br.edu.poo.permissoes.domain.permission.PermissaoId;
import br.edu.poo.permissoes.domain.role.Papel;
import br.edu.poo.permissoes.domain.role.PapelId;
import br.edu.poo.permissoes.infrastructure.repository.PapelRepository;
import br.edu.poo.permissoes.infrastructure.repository.PermissaoRepository;
import br.edu.poo.permissoes.shared.exception.DominioException;

public final class PapelServicePadrao implements PapelService {

    private final PapelRepository papelRepository;
    private final PermissaoRepository permissaoRepository;
    private final AuditoriaService auditoriaService;

    public PapelServicePadrao(
        PapelRepository papelRepository,
        PermissaoRepository permissaoRepository,
        AuditoriaService auditoriaService
    ) {
        this.papelRepository = papelRepository;
        this.permissaoRepository = permissaoRepository;
        this.auditoriaService = auditoriaService;
    }

    @Override
    public Papel cadastrar(String nome) {
        if (papelRepository.buscarPorNome(nome).isPresent()) {
            throw new DominioException("Papel ja cadastrado: " + nome);
        }
        Papel papel = new Papel(PapelId.de(nome), nome);
        papelRepository.salvar(papel);
        auditoriaService.registrarAlteracao(
            new RegistroAlteracao("Papel", "CADASTRO", nome, LocalDateTime.now())
        );
        return papel;
    }

    @Override
    public void associarPermissao(PapelId papelId, PermissaoId permissaoId) {
        Papel papel = consultar(papelId);
        Permissao permissao = permissaoRepository.buscarPorId(permissaoId)
            .orElseThrow(() -> new DominioException("Permissao inexistente: " + permissaoId.valor()));
        papel.associarPermissao(permissao.id());
        auditoriaService.registrarAlteracao(
            new RegistroAlteracao("Papel", "ASSOCIAR_PERMISSAO", papel.nome() + " -> " + permissao.codigo(), LocalDateTime.now())
        );
    }

    @Override
    public void removerPermissao(PapelId papelId, PermissaoId permissaoId) {
        Papel papel = consultar(papelId);
        papel.removerPermissao(permissaoId);
        auditoriaService.registrarAlteracao(
            new RegistroAlteracao("Papel", "REMOVER_PERMISSAO", papel.nome() + " -> " + permissaoId.valor(), LocalDateTime.now())
        );
    }

    @Override
    public Papel consultar(PapelId papelId) {
        return papelRepository.buscarPorId(papelId)
            .orElseThrow(() -> new DominioException("Papel inexistente: " + papelId.valor()));
    }

    @Override
    public List<Papel> listarTodos() {
        return papelRepository.listarTodos();
    }
}
