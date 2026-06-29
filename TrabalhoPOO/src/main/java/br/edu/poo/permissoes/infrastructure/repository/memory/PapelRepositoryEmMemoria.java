package br.edu.poo.permissoes.infrastructure.repository.memory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import br.edu.poo.permissoes.domain.role.Papel;
import br.edu.poo.permissoes.domain.role.PapelId;
import br.edu.poo.permissoes.infrastructure.repository.PapelRepository;

public final class PapelRepositoryEmMemoria implements PapelRepository {

    private final Map<String, Papel> papeis;

    public PapelRepositoryEmMemoria() {
        this.papeis = new LinkedHashMap<>();
    }

    @Override
    public Papel salvar(Papel papel) {
        papeis.put(papel.id().valor(), papel);
        return papel;
    }

    @Override
    public Optional<Papel> buscarPorId(PapelId id) {
        return Optional.ofNullable(papeis.get(id.valor()));
    }

    @Override
    public Optional<Papel> buscarPorNome(String nome) {
        return papeis.values().stream()
            .filter(papel -> papel.nome().equals(nome))
            .findFirst();
    }

    @Override
    public List<Papel> listarTodos() {
        return new ArrayList<>(papeis.values());
    }
}
