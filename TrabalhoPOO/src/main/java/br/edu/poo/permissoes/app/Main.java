package br.edu.poo.permissoes.app;

import br.edu.poo.permissoes.application.policy.PoliticaAcesso;
import br.edu.poo.permissoes.application.policy.PoliticaAcessoPadrao;
import br.edu.poo.permissoes.application.service.AuditoriaService;
import br.edu.poo.permissoes.application.service.AutorizacaoService;
import br.edu.poo.permissoes.application.service.PapelService;
import br.edu.poo.permissoes.application.service.PermissaoService;
import br.edu.poo.permissoes.application.service.PermissoesEfetivasCalculator;
import br.edu.poo.permissoes.application.service.UsuarioService;
import br.edu.poo.permissoes.application.service.impl.AuditoriaServiceEmMemoria;
import br.edu.poo.permissoes.application.service.impl.AutorizacaoServicePadrao;
import br.edu.poo.permissoes.application.service.impl.PapelServicePadrao;
import br.edu.poo.permissoes.application.service.impl.PermissaoServicePadrao;
import br.edu.poo.permissoes.application.service.impl.PermissoesEfetivasCalculatorPadrao;
import br.edu.poo.permissoes.application.service.impl.UsuarioServicePadrao;
import br.edu.poo.permissoes.domain.access.DecisaoAcesso;
import br.edu.poo.permissoes.domain.permission.Permissao;
import br.edu.poo.permissoes.domain.role.Papel;
import br.edu.poo.permissoes.domain.user.Administrador;
import br.edu.poo.permissoes.domain.user.Usuario;
import br.edu.poo.permissoes.domain.user.UsuarioComum;
import br.edu.poo.permissoes.domain.user.UsuarioSistema;
import br.edu.poo.permissoes.infrastructure.repository.PapelRepository;
import br.edu.poo.permissoes.infrastructure.repository.PermissaoRepository;
import br.edu.poo.permissoes.infrastructure.repository.RegistroAcessoRepository;
import br.edu.poo.permissoes.infrastructure.repository.RegistroAlteracaoRepository;
import br.edu.poo.permissoes.infrastructure.repository.UsuarioRepository;
import br.edu.poo.permissoes.infrastructure.repository.memory.PapelRepositoryEmMemoria;
import br.edu.poo.permissoes.infrastructure.repository.memory.PermissaoRepositoryEmMemoria;
import br.edu.poo.permissoes.infrastructure.repository.memory.RegistroAcessoRepositoryEmMemoria;
import br.edu.poo.permissoes.infrastructure.repository.memory.RegistroAlteracaoRepositoryEmMemoria;
import br.edu.poo.permissoes.infrastructure.repository.memory.UsuarioRepositoryEmMemoria;

public class Main {

    public static void main(String[] args) {
        new Main().executar();
    }

    private void executar() {
        imprimirCabecalho();

        // Infraestrutura: repositórios em memória
        PermissaoRepository permissaoRepository = new PermissaoRepositoryEmMemoria();
        PapelRepository papelRepository = new PapelRepositoryEmMemoria();
        UsuarioRepository usuarioRepository = new UsuarioRepositoryEmMemoria();
        RegistroAcessoRepository registroAcessoRepository = new RegistroAcessoRepositoryEmMemoria();
        RegistroAlteracaoRepository registroAlteracaoRepository = new RegistroAlteracaoRepositoryEmMemoria();

        // Serviços de aplicação
        AuditoriaService auditoriaService = new AuditoriaServiceEmMemoria(
            registroAcessoRepository,
            registroAlteracaoRepository
        );

        PermissoesEfetivasCalculator permissoesEfetivasCalculator = new PermissoesEfetivasCalculatorPadrao(
            papelRepository,
            permissaoRepository
        );

        // Política de acesso composta por cadeia de regras
        PoliticaAcesso politicaAcesso = new PoliticaAcessoPadrao();

        PermissaoService permissaoService = new PermissaoServicePadrao(permissaoRepository, auditoriaService);
        PapelService papelService = new PapelServicePadrao(papelRepository, permissaoRepository, auditoriaService);
        UsuarioService usuarioService = new UsuarioServicePadrao(
            usuarioRepository,
            papelRepository,
            permissoesEfetivasCalculator,
            auditoriaService
        );
        AutorizacaoService autorizacaoService = new AutorizacaoServicePadrao(
            usuarioRepository,
            permissoesEfetivasCalculator,
            politicaAcesso,
            auditoriaService
        );

        // Dados iniciais: permissões
        Permissao userCreate   = permissaoService.cadastrar("USER_CREATE",   "Criacao de usuarios");
        Permissao userDelete   = permissaoService.cadastrar("USER_DELETE",   "Exclusao de usuarios");
        Permissao roleCreate   = permissaoService.cadastrar("ROLE_CREATE",   "Criacao de papeis");
        Permissao reportView   = permissaoService.cadastrar("REPORT_VIEW",   "Visualizacao de relatorios");
        Permissao reportExport = permissaoService.cadastrar("REPORT_EXPORT", "Exportacao de relatorios");
        Permissao systemConfig = permissaoService.cadastrar("SYSTEM_CONFIG", "Configuracao do sistema");

        // Dados iniciais: papéis
        Papel admin     = papelService.cadastrar("ADMIN");
        Papel manager   = papelService.cadastrar("MANAGER");
        Papel auditor   = papelService.cadastrar("AUDITOR");
        Papel basicUser = papelService.cadastrar("BASIC_USER");

        // ADMIN recebe todas as permissões
        papelService.associarPermissao(admin.id(), userCreate.id());
        papelService.associarPermissao(admin.id(), userDelete.id());
        papelService.associarPermissao(admin.id(), roleCreate.id());
        papelService.associarPermissao(admin.id(), reportView.id());
        papelService.associarPermissao(admin.id(), reportExport.id());
        papelService.associarPermissao(admin.id(), systemConfig.id());

        // MANAGER: visualizar e exportar relatórios
        papelService.associarPermissao(manager.id(), reportView.id());
        papelService.associarPermissao(manager.id(), reportExport.id());

        // AUDITOR: apenas visualizar relatórios
        papelService.associarPermissao(auditor.id(), reportView.id());

        // BASIC_USER: permissões limitadas
        papelService.associarPermissao(basicUser.id(), reportView.id());

        // Dados iniciais: usuários
        Usuario administrador   = usuarioService.cadastrar(new Administrador("admin@empresa.com",    "Administrador Geral"));
        Usuario gerente         = usuarioService.cadastrar(new UsuarioComum("gerente@empresa.com",   "Gerente Regional"));
        Usuario usuarioAuditor  = usuarioService.cadastrar(new UsuarioComum("auditor@empresa.com",  "Auditor Interno"));
        Usuario usuarioSistema  = usuarioService.cadastrar(new UsuarioSistema("sistema@empresa.com","Servico de Integracao"));
        Usuario usuarioComum    = usuarioService.cadastrar(new UsuarioComum("user@empresa.com",      "Usuario Basico"));
        Usuario usuarioBloqueado = usuarioService.cadastrar(new UsuarioComum("bloqueado@empresa.com","Usuario Bloqueado"));

        // Associação de papéis
        usuarioService.associarPapel(administrador.id(),    admin.id());
        usuarioService.associarPapel(gerente.id(),          manager.id());
        usuarioService.associarPapel(gerente.id(),          auditor.id());   // múltiplos papéis
        usuarioService.associarPapel(usuarioAuditor.id(),   auditor.id());
        usuarioService.associarPapel(usuarioSistema.id(),   basicUser.id());
        usuarioService.associarPapel(usuarioComum.id(),     basicUser.id());
        usuarioService.associarPapel(usuarioBloqueado.id(), basicUser.id());

        // Bloqueio inicial
        usuarioService.bloquear(usuarioBloqueado.id(), "Bloqueio inicial para demonstracao");

        // ─────────────────────────────────────────────────────────
        // CENÁRIO 1: Usuário autorizado acessando recurso permitido
        // ─────────────────────────────────────────────────────────
        executarCenario("1. Usuario autorizado acessando recurso permitido", () ->
            imprimirAcesso(autorizacaoService, administrador, "USER_CREATE")
        );

        // ─────────────────────────────────────────────────────────────────
        // CENÁRIO 2: Usuário tentando acessar recurso sem permissão
        // ─────────────────────────────────────────────────────────────────
        executarCenario("2. Usuario tentando acessar recurso sem permissao", () ->
            imprimirAcesso(autorizacaoService, usuarioComum, "USER_DELETE")
        );

        // ─────────────────────────────────────────────────────────────────────
        // CENÁRIO 3: Usuário bloqueado tentando acessar qualquer recurso
        //   (garantido pela RegraUsuarioBloqueado, primeira da cadeia)
        // ─────────────────────────────────────────────────────────────────────
        executarCenario("3. Usuario bloqueado tentando acessar qualquer recurso", () ->
            imprimirAcesso(autorizacaoService, usuarioBloqueado, "REPORT_VIEW")
        );

        // ─────────────────────────────────────────────────────────────────
        // CENÁRIO 4: Usuário com múltiplos papéis (gerente = MANAGER + AUDITOR)
        // ─────────────────────────────────────────────────────────────────
        executarCenario("4. Usuario com multiplos papeis (MANAGER + AUDITOR)", () -> {
            imprimirAcesso(autorizacaoService, gerente, "REPORT_VIEW");
            imprimirAcesso(autorizacaoService, gerente, "REPORT_EXPORT");
        });

        // ─────────────────────────────────────────────────────────────────
        // CENÁRIO 5: Remoção de permissão e nova verificação
        // ─────────────────────────────────────────────────────────────────
        executarCenario("5. Remocao de permissao e nova verificacao", () -> {
            System.out.println("  [Antes] Gerente tenta REPORT_EXPORT:");
            imprimirAcesso(autorizacaoService, gerente, "REPORT_EXPORT");
            papelService.removerPermissao(manager.id(), reportExport.id());
            System.out.println("  [Depois] REPORT_EXPORT removida do papel MANAGER:");
            imprimirAcesso(autorizacaoService, gerente, "REPORT_EXPORT");
        });

        // ─────────────────────────────────────────────────────────────────
        // CENÁRIO 6 (extra): Remoção de papel do usuário
        // ─────────────────────────────────────────────────────────────────
        executarCenario("6. Remocao de papel do usuario", () -> {
            System.out.println("  [Antes] Usuario comum tenta REPORT_VIEW:");
            imprimirAcesso(autorizacaoService, usuarioComum, "REPORT_VIEW");
            usuarioService.removerPapel(usuarioComum.id(), basicUser.id());
            System.out.println("  [Depois] Papel BASIC_USER removido do usuario:");
            imprimirAcesso(autorizacaoService, usuarioComum, "REPORT_VIEW");
        });

        // ─────────────────────────────────────────────────────────────────
        // CENÁRIO 7 (extra): Desbloqueio de usuário
        // ─────────────────────────────────────────────────────────────────
        executarCenario("7. Desbloqueio de usuario e nova verificacao", () -> {
            System.out.println("  [Bloqueado] Tentativa de acesso:");
            imprimirAcesso(autorizacaoService, usuarioBloqueado, "REPORT_VIEW");
            usuarioService.desbloquear(usuarioBloqueado.id(), "Desbloqueio para demonstracao");
            System.out.println("  [Desbloqueado] Tentativa de acesso:");
            imprimirAcesso(autorizacaoService, usuarioBloqueado, "REPORT_VIEW");
        });

        // ─────────────────────────────────────────────────────────────────
        // CENÁRIO 8: Administrador bloqueado ainda acessa via seus papeis
        //   Demonstra que RegraAdministrador avalia ANTES da RegraUsuarioBloqueado,
        //   permitindo recuperacao do sistema mesmo com admin bloqueado.
        //   Acesso e concedido pela permissao nos papeis, nao pelo tipo.
        // ─────────────────────────────────────────────────────────────────
        executarCenario("8. Administrador bloqueado ainda acessa via seus papeis", () -> {
            usuarioService.bloquear(administrador.id(), "Bloqueio de teste para demonstracao");
            System.out.println("  [Admin bloqueado] Tentativa de acesso a SYSTEM_CONFIG:");
            imprimirAcesso(autorizacaoService, administrador, "SYSTEM_CONFIG");
            System.out.println("  [Admin bloqueado] Tentativa de acesso a USER_CREATE:");
            imprimirAcesso(autorizacaoService, administrador, "USER_CREATE");
            usuarioService.desbloquear(administrador.id(), "Desbloqueio apos demonstracao");
        });

        // ─────────────────────────────────────────────────────────────────
        // CENÁRIO 9: UsuarioSistema acessa qualquer permissao dos seus papeis
        //   Ignora bloqueio manual (processo automatizado).
        //   Acesso negado apenas quando a permissao nao esta em nenhum papel.
        // ─────────────────────────────────────────────────────────────────
        executarCenario("9. UsuarioSistema - acesso por permissoes dos papeis (ignora bloqueio)", () -> {
            System.out.println("  [Sistema] REPORT_VIEW (tem no papel BASIC_USER):");
            imprimirAcesso(autorizacaoService, usuarioSistema, "REPORT_VIEW");
            System.out.println("  [Sistema] USER_CREATE (nao tem no papel BASIC_USER):");
            imprimirAcesso(autorizacaoService, usuarioSistema, "USER_CREATE");
            usuarioService.bloquear(usuarioSistema.id(), "Bloqueio de teste");
            System.out.println("  [Sistema bloqueado] REPORT_VIEW (bloqueio ignorado por ser sistema):");
            imprimirAcesso(autorizacaoService, usuarioSistema, "REPORT_VIEW");
            usuarioService.desbloquear(usuarioSistema.id(), "Desbloqueio apos demonstracao");
        });

        // ─────────────────────────────────────────────────────────────────
        // CENÁRIO 10: Permissões efetivas por usuário
        // ─────────────────────────────────────────────────────────────────
        executarCenario("10. Permissoes efetivas por usuario", () -> {
            imprimirPermissoesEfetivas(usuarioService, administrador);
            imprimirPermissoesEfetivas(usuarioService, gerente);
            imprimirPermissoesEfetivas(usuarioService, usuarioAuditor);
            imprimirPermissoesEfetivas(usuarioService, usuarioSistema);
            imprimirPermissoesEfetivas(usuarioService, usuarioComum);
            imprimirPermissoesEfetivas(usuarioService, usuarioBloqueado);
        });

        // ─────────────────────────────────────────────────────────────────
        // CENÁRIO 11: Histórico de tentativas autorizadas
        // ─────────────────────────────────────────────────────────────────
        executarCenario("11. Historico de tentativas autorizadas", () ->
            auditoriaService.listarTentativasAutorizadas()
                .forEach(registro -> System.out.println("  " + registro.resumo()))
        );

        // ─────────────────────────────────────────────────────────────────
        // CENÁRIO 12: Histórico de tentativas negadas
        // ─────────────────────────────────────────────────────────────────
        executarCenario("12. Historico de tentativas negadas", () ->
            auditoriaService.listarTentativasNegadas()
                .forEach(registro -> System.out.println("  " + registro.resumo()))
        );

        // ─────────────────────────────────────────────────────────────────
        // CENÁRIO 13: Alterações auditadas em papéis e usuários
        // ─────────────────────────────────────────────────────────────────
        executarCenario("13. Alteracoes auditadas em papeis e usuarios", () ->
            auditoriaService.listarAlteracoes()
                .forEach(registro -> System.out.println("  " + registro.resumo()))
        );

        imprimirRodape();
    }

    private void imprimirAcesso(AutorizacaoService autorizacaoService, Usuario usuario, String codigoPermissao) {
        DecisaoAcesso decisao = autorizacaoService.verificarAcesso(usuario.id(), codigoPermissao);
        System.out.printf(
            "  Usuario=%-35s | Tipo=%-14s | Acao=%-15s | Resultado=%-10s | Motivo=%s%n",
            usuario.identificador(),
            usuario.tipo(),
            codigoPermissao,
            decisao.resultado(),
            decisao.motivo()
        );
    }

    private void imprimirPermissoesEfetivas(UsuarioService usuarioService, Usuario usuario) {
        System.out.printf(
            "  Usuario=%-35s | Tipo=%-14s | Bloqueado=%-5s | Permissoes=%s%n",
            usuario.identificador(),
            usuario.tipo(),
            usuario.estaBloqueado(),
            usuarioService.listarPermissoesEfetivas(usuario.id())
        );
    }

    private void executarCenario(String titulo, Runnable acao) {
        System.out.println();
        System.out.println("=== " + titulo + " ===");
        try {
            acao.run();
        } catch (RuntimeException excecao) {
            System.out.println("  Erro: " + excecao.getMessage());
        }
    }

    private void imprimirCabecalho() {
        System.out.println("============================================================");
        System.out.println("  SISTEMA DE CONTROLE DE PERMISSOES E ACESSOS");
        System.out.println("  Cenarios manuais de demonstracao");
        System.out.println("============================================================");
    }

    private void imprimirRodape() {
        System.out.println();
        System.out.println("============================================================");
        System.out.println("  Fim da execucao dos cenarios");
        System.out.println("============================================================");
    }
}
