package br.edu.poo.permissoes.application.service;

import java.util.List;

import br.edu.poo.permissoes.domain.permission.Permissao;

public interface PermissaoService {

    Permissao cadastrar(String codigo);

    List<Permissao> listarTodas();
}
