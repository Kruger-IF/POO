package br.edu.poo.permissoes.application.service;

import java.util.List;

import br.edu.poo.permissoes.domain.audit.RegistroAcesso;
import br.edu.poo.permissoes.domain.audit.RegistroAlteracao;

public interface AuditoriaService {

    void registrarAcesso(RegistroAcesso registroAcesso);

    void registrarAlteracao(RegistroAlteracao registroAlteracao);

    List<RegistroAcesso> listarTentativasAutorizadas();

    List<RegistroAcesso> listarTentativasNegadas();

    List<RegistroAlteracao> listarAlteracoes();
}
