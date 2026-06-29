package br.edu.poo.permissoes.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import br.edu.poo.permissoes.domain.role.Papel;
import br.edu.poo.permissoes.domain.role.PapelId;

public interface PapelRepository {

    Papel salvar(Papel papel);

    Optional<Papel> buscarPorId(PapelId id);

    Optional<Papel> buscarPorNome(String nome);

    List<Papel> listarTodos();
}
