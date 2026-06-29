package br.edu.poo.permissoes.infrastructure.repository.memory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import br.edu.poo.permissoes.domain.permission.Permissao;
import br.edu.poo.permissoes.domain.permission.PermissaoId;
import br.edu.poo.permissoes.infrastructure.repository.PermissaoRepository;

public final class PermissaoRepositoryEmMemoria implements PermissaoRepository {

    private final Map<String, Permissao> permissoes;

    public PermissaoRepositoryEmMemoria() {
        this.permissoes = new LinkedHashMap<>();
    }

    @Override
    public Permissao salvar(Permissao permissao) {
        permissoes.put(permissao.codigo(), permissao);
        return permissao;
    }

    @Override
    public Optional<Permissao> buscarPorId(PermissaoId id) {
        return Optional.ofNullable(permissoes.get(id.valor()));
    }

    @Override
    public Optional<Permissao> buscarPorCodigo(String codigo) {
        return Optional.ofNullable(permissoes.get(codigo));
    }

    @Override
    public List<Permissao> listarTodas() {
        return new ArrayList<>(permissoes.values());
    }
}
