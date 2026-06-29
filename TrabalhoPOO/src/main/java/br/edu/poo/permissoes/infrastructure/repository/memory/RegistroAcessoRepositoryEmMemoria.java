package br.edu.poo.permissoes.infrastructure.repository.memory;

import java.util.ArrayList;
import java.util.List;

import br.edu.poo.permissoes.domain.audit.RegistroAcesso;
import br.edu.poo.permissoes.infrastructure.repository.RegistroAcessoRepository;

public final class RegistroAcessoRepositoryEmMemoria implements RegistroAcessoRepository {

    private final List<RegistroAcesso> registros;

    public RegistroAcessoRepositoryEmMemoria() {
        this.registros = new ArrayList<>();
    }

    @Override
    public void salvar(RegistroAcesso registroAcesso) {
        registros.add(registroAcesso);
    }

    @Override
    public List<RegistroAcesso> listarTodos() {
        return List.copyOf(registros);
    }
}
