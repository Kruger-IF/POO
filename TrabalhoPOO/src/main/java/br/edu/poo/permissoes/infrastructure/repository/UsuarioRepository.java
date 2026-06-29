package br.edu.poo.permissoes.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import br.edu.poo.permissoes.domain.user.Usuario;
import br.edu.poo.permissoes.domain.user.UsuarioId;

public interface UsuarioRepository {

    Usuario salvar(Usuario usuario);

    Optional<Usuario> buscarPorId(UsuarioId id);

    List<Usuario> listarTodos();
}
