package br.edu.poo.permissoes.application.service;

import java.util.List;

import br.edu.poo.permissoes.domain.role.Papel;
import br.edu.poo.permissoes.domain.role.PapelId;
import br.edu.poo.permissoes.domain.permission.PermissaoId;

public interface PapelService {

    Papel cadastrar(String nome);

    void associarPermissao(PapelId papelId, PermissaoId permissaoId);

    void removerPermissao(PapelId papelId, PermissaoId permissaoId);

    Papel consultar(PapelId papelId);

    List<Papel> listarTodos();
}
