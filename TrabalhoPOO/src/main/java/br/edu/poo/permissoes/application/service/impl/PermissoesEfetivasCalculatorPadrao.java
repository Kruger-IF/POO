package br.edu.poo.permissoes.application.service.impl;

import java.util.LinkedHashSet;
import java.util.Set;

import br.edu.poo.permissoes.application.service.PermissoesEfetivasCalculator;
import br.edu.poo.permissoes.domain.permission.PermissaoId;
import br.edu.poo.permissoes.domain.role.Papel;
import br.edu.poo.permissoes.domain.role.PapelId;
import br.edu.poo.permissoes.domain.user.Usuario;
import br.edu.poo.permissoes.infrastructure.repository.PapelRepository;
import br.edu.poo.permissoes.infrastructure.repository.PermissaoRepository;
import br.edu.poo.permissoes.shared.exception.DominioException;

public final class PermissoesEfetivasCalculatorPadrao implements PermissoesEfetivasCalculator {

    private final PapelRepository papelRepository;
    private final PermissaoRepository permissaoRepository;

    public PermissoesEfetivasCalculatorPadrao(
        PapelRepository papelRepository,
        PermissaoRepository permissaoRepository
    ) {
        this.papelRepository = papelRepository;
        this.permissaoRepository = permissaoRepository;
    }

    @Override
    public Set<String> calcular(Usuario usuario) {
        Set<String> permissoes = new LinkedHashSet<>();
        for (PapelId papelId : usuario.papeis().itens()) {
            Papel papel = papelRepository.buscarPorId(papelId)
                .orElseThrow(() -> new DominioException("Papel inexistente: " + papelId.valor()));
            for (PermissaoId permissaoId : papel.permissoes().itens()) {
                permissaoRepository.buscarPorId(permissaoId)
                    .ifPresent(permissao -> permissoes.add(permissao.codigo()));
            }
        }
        return Set.copyOf(permissoes);
    }
}