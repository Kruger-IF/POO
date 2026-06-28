package br.edu.poo.permissoes.infrastructure.repository;

import java.util.List;

import br.edu.poo.permissoes.domain.audit.RegistroAcesso;

public interface RegistroAcessoRepository {

    void salvar(RegistroAcesso registroAcesso);

    List<RegistroAcesso> listarTodos();
}
