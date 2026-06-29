package br.edu.poo.permissoes.application.service.impl;

import java.util.List;

import br.edu.poo.permissoes.application.service.AuditoriaService;
import br.edu.poo.permissoes.application.service.PermissaoService;
import br.edu.poo.permissoes.domain.audit.RegistroAlteracao;
import br.edu.poo.permissoes.domain.permission.Permissao;
import br.edu.poo.permissoes.domain.permission.PermissaoId;
import br.edu.poo.permissoes.infrastructure.repository.PermissaoRepository;
import br.edu.poo.permissoes.shared.exception.DominioException;

import java.time.LocalDateTime;

public final class PermissaoServicePadrao implements PermissaoService {

    private final PermissaoRepository permissaoRepository;
    private final AuditoriaService auditoriaService;

    public PermissaoServicePadrao(PermissaoRepository permissaoRepository, AuditoriaService auditoriaService) {
        this.permissaoRepository = permissaoRepository;
        this.auditoriaService = auditoriaService;
    }

    @Override
    public Permissao cadastrar(String codigo) {
        if (permissaoRepository.buscarPorCodigo(codigo).isPresent()) {
            throw new DominioException("Permissao ja cadastrada: " + codigo);
        }
        Permissao permissao = new Permissao(PermissaoId.de(codigo), codigo);
        permissaoRepository.salvar(permissao);
        auditoriaService.registrarAlteracao(
            new RegistroAlteracao("Permissao", "CADASTRO", codigo, LocalDateTime.now())
        );
        return permissao;
    }

    @Override
    public Permissao cadastrar(String codigo, String descricao) {
        if (permissaoRepository.buscarPorCodigo(codigo).isPresent()) {
            throw new DominioException("Permissao ja cadastrada: " + codigo);
        }
        Permissao permissao = new Permissao(PermissaoId.de(codigo), descricao);
        permissaoRepository.salvar(permissao);
        auditoriaService.registrarAlteracao(
            new RegistroAlteracao("Permissao", "CADASTRO", codigo, LocalDateTime.now())
        );
        return permissao;
    }

    @Override
    public List<Permissao> listarTodas() {
        return permissaoRepository.listarTodas();
    }
}
