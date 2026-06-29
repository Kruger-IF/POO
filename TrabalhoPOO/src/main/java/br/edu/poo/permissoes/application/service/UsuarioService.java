package br.edu.poo.permissoes.application.service;

import java.util.List;
import java.util.Set;

import br.edu.poo.permissoes.domain.role.PapelId;
import br.edu.poo.permissoes.domain.user.Usuario;
import br.edu.poo.permissoes.domain.user.UsuarioId;

public interface UsuarioService {

    Usuario cadastrar(Usuario usuario);

    void associarPapel(UsuarioId usuarioId, PapelId papelId);

    void removerPapel(UsuarioId usuarioId, PapelId papelId);

    void bloquear(UsuarioId usuarioId, String motivo);

    void desbloquear(UsuarioId usuarioId, String motivo);

    Set<String> listarPermissoesEfetivas(UsuarioId usuarioId);

    Usuario consultar(UsuarioId usuarioId);

    List<Usuario> listarTodos();
}
