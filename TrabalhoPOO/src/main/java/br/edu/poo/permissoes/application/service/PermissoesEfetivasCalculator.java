package br.edu.poo.permissoes.application.service;

import java.util.Set;

import br.edu.poo.permissoes.domain.user.Usuario;

public interface PermissoesEfetivasCalculator {

    Set<String> calcular(Usuario usuario);
}