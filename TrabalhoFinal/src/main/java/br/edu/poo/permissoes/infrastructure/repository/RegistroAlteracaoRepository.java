package br.edu.poo.permissoes.infrastructure.repository;

import java.util.List;

import br.edu.poo.permissoes.domain.audit.RegistroAlteracao;

public interface RegistroAlteracaoRepository {

    void salvar(RegistroAlteracao registroAlteracao);

    List<RegistroAlteracao> listarTodos();
}
