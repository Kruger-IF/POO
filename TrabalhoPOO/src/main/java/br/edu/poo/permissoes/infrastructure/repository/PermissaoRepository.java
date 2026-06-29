package br.edu.poo.permissoes.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import br.edu.poo.permissoes.domain.permission.Permissao;
import br.edu.poo.permissoes.domain.permission.PermissaoId;

public interface PermissaoRepository {

    Permissao salvar(Permissao permissao);

    Optional<Permissao> buscarPorId(PermissaoId id);

    Optional<Permissao> buscarPorCodigo(String codigo);

    List<Permissao> listarTodas();
}
