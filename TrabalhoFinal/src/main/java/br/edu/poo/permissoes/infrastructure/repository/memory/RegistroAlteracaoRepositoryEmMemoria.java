package br.edu.poo.permissoes.infrastructure.repository.memory;

import java.util.ArrayList;
import java.util.List;

import br.edu.poo.permissoes.domain.audit.RegistroAlteracao;
import br.edu.poo.permissoes.infrastructure.repository.RegistroAlteracaoRepository;

public final class RegistroAlteracaoRepositoryEmMemoria implements RegistroAlteracaoRepository {

    private final List<RegistroAlteracao> registros;

    public RegistroAlteracaoRepositoryEmMemoria() {
        this.registros = new ArrayList<>();
    }

    @Override
    public void salvar(RegistroAlteracao registroAlteracao) {
        registros.add(registroAlteracao);
    }

    @Override
    public List<RegistroAlteracao> listarTodos() {
        return List.copyOf(registros);
    }
}
