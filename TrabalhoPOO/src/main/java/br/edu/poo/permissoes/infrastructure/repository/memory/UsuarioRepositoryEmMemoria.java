package br.edu.poo.permissoes.infrastructure.repository.memory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import br.edu.poo.permissoes.domain.user.Usuario;
import br.edu.poo.permissoes.domain.user.UsuarioId;
import br.edu.poo.permissoes.infrastructure.repository.UsuarioRepository;

public final class UsuarioRepositoryEmMemoria implements UsuarioRepository {

    private final Map<String, Usuario> usuarios;

    public UsuarioRepositoryEmMemoria() {
        this.usuarios = new LinkedHashMap<>();
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        usuarios.put(usuario.id().valor(), usuario);
        return usuario;
    }

    @Override
    public Optional<Usuario> buscarPorId(UsuarioId id) {
        return Optional.ofNullable(usuarios.get(id.valor()));
    }

    @Override
    public List<Usuario> listarTodos() {
        return new ArrayList<>(usuarios.values());
    }
}
