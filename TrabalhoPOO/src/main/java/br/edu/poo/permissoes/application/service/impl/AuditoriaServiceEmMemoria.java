package br.edu.poo.permissoes.application.service.impl;

import java.util.List;

import br.edu.poo.permissoes.application.service.AuditoriaService;
import br.edu.poo.permissoes.domain.access.ResultadoAcesso;
import br.edu.poo.permissoes.domain.audit.RegistroAcesso;
import br.edu.poo.permissoes.domain.audit.RegistroAlteracao;
import br.edu.poo.permissoes.infrastructure.repository.RegistroAcessoRepository;
import br.edu.poo.permissoes.infrastructure.repository.RegistroAlteracaoRepository;

public final class AuditoriaServiceEmMemoria implements AuditoriaService {

    private final RegistroAcessoRepository acessoRepository;
    private final RegistroAlteracaoRepository alteracaoRepository;

    public AuditoriaServiceEmMemoria(
        RegistroAcessoRepository acessoRepository,
        RegistroAlteracaoRepository alteracaoRepository
    ) {
        this.acessoRepository = acessoRepository;
        this.alteracaoRepository = alteracaoRepository;
    }

    @Override
    public void registrarAcesso(RegistroAcesso registroAcesso) {
        acessoRepository.salvar(registroAcesso);
    }

    @Override
    public void registrarAlteracao(RegistroAlteracao registroAlteracao) {
        alteracaoRepository.salvar(registroAlteracao);
    }

    @Override
    public List<RegistroAcesso> listarTentativasAutorizadas() {
        return acessoRepository.listarTodos().stream()
            .filter(registro -> registro.resultado() == ResultadoAcesso.AUTORIZADO)
            .toList();
    }

    @Override
    public List<RegistroAcesso> listarTentativasNegadas() {
        return acessoRepository.listarTodos().stream()
            .filter(registro -> registro.resultado() == ResultadoAcesso.NEGADO)
            .toList();
    }

    @Override
    public List<RegistroAlteracao> listarAlteracoes() {
        return alteracaoRepository.listarTodos();
    }
}
