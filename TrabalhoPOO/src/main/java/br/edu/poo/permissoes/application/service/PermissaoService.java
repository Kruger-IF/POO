package br.edu.poo.permissoes.application.service;

import java.util.List;

import br.edu.poo.permissoes.domain.permission.Permissao;

public interface PermissaoService {

    default Permissao cadastrar(String codigo) {
        return cadastrar(codigo, codigo);
    }

    Permissao cadastrar(String codigo, String descricao);

    List<Permissao> listarTodas();
}
