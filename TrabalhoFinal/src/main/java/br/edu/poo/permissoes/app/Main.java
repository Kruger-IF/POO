
package br.edu.poo.permissoes.app;

import br.edu.poo.permissoes.application.policy.PoliticaAcesso;
import br.edu.poo.permissoes.application.policy.PoliticaAcessoPadrao;
import br.edu.poo.permissoes.application.service.AuditoriaService;
import br.edu.poo.permissoes.application.service.AutorizacaoService;
import br.edu.poo.permissoes.application.service.PapelService;
import br.edu.poo.permissoes.application.service.PermissaoService;
import br.edu.poo.permissoes.application.service.UsuarioService;
import br.edu.poo.permissoes.application.service.impl.AuditoriaServiceEmMemoria;
import br.edu.poo.permissoes.application.service.impl.AutorizacaoServicePadrao;
import br.edu.poo.permissoes.application.service.impl.PapelServicePadrao;
import br.edu.poo.permissoes.application.service.impl.PermissaoServicePadrao;
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

        PermissaoRepository permissaoRepository = new PermissaoRepositoryEmMemoria();
        PapelRepository papelRepository = new PapelRepositoryEmMemoria();
        UsuarioRepository usuarioRepository = new UsuarioRepositoryEmMemoria();
        RegistroAcessoRepository registroAcessoRepository = new RegistroAcessoRepositoryEmMemoria();
        RegistroAlteracaoRepository registroAlteracaoRepository = new RegistroAlteracaoRepositoryEmMemoria();

        AuditoriaService auditoriaService = new AuditoriaServiceEmMemoria(
            registroAcessoRepository,
            registroAlteracaoRepository
        );

        PoliticaAcesso politicaAcesso = new PoliticaAcessoPadrao();

        PermissaoService permissaoService = new PermissaoServicePadrao(permissaoRepository, auditoriaService);
        PapelService papelService = new PapelServicePadrao(papelRepository, permissaoRepository, auditoriaService);
        UsuarioService usuarioService = new UsuarioServicePadrao(
            usuarioRepository,
            papelRepository,
            permissaoRepository,
            auditoriaService
        );
        AutorizacaoService autorizacaoService = new AutorizacaoServicePadrao(
            usuarioRepository,
            papelRepository,
            permissaoRepository,
            politicaAcesso,
            auditoriaService
        );

        Permissao userCreate = permissaoService.cadastrar("USER_CREATE");
        Permissao userDelete = permissaoService.cadastrar("USER_DELETE");
        Permissao roleCreate = permissaoService.cadastrar("ROLE_CREATE");
        Permissao reportView = permissaoService.cadastrar("REPORT_VIEW");
        Permissao reportExport = permissaoService.cadastrar("REPORT_EXPORT");
        Permissao systemConfig = permissaoService.cadastrar("SYSTEM_CONFIG");

        Papel admin = papelService.cadastrar("ADMIN");
        Papel manager = papelService.cadastrar("MANAGER");
        Papel auditor = papelService.cadastrar("AUDITOR");
        Papel basicUser = papelService.cadastrar("BASIC_USER");

        papelService.associarPermissao(admin.id(), userCreate.id());
        papelService.associarPermissao(admin.id(), userDelete.id());
        papelService.associarPermissao(admin.id(), roleCreate.id());
        papelService.associarPermissao(admin.id(), reportView.id());
        papelService.associarPermissao(admin.id(), reportExport.id());
        papelService.associarPermissao(admin.id(), systemConfig.id());

        papelService.associarPermissao(manager.id(), reportView.id());
        papelService.associarPermissao(manager.id(), reportExport.id());

        papelService.associarPermissao(auditor.id(), reportView.id());
        papelService.associarPermissao(basicUser.id(), reportView.id());

        Usuario administrador = usuarioService.cadastrar(new Administrador("admin@empresa.com", "Administrador Geral"));
        Usuario gerente = usuarioService.cadastrar(new UsuarioComum("gerente@empresa.com", "Gerente Regional"));
        Usuario usuarioAuditor = usuarioService.cadastrar(new UsuarioSistema("auditor@empresa.com", "Servico de Auditoria"));
        Usuario usuarioComum = usuarioService.cadastrar(new UsuarioComum("user@empresa.com", "Usuario Basico"));
        Usuario usuarioBloqueado = usuarioService.cadastrar(new UsuarioComum("bloqueado@empresa.com", "Usuario Bloqueado"));

        usuarioService.associarPapel(administrador.id(), admin.id());
        usuarioService.associarPapel(gerente.id(), manager.id());
        usuarioService.associarPapel(gerente.id(), auditor.id());
        usuarioService.associarPapel(usuarioAuditor.id(), auditor.id());
        usuarioService.associarPapel(usuarioComum.id(), basicUser.id());
        usuarioService.associarPapel(usuarioBloqueado.id(), basicUser.id());

        usuarioService.bloquear(usuarioBloqueado.id(), "Bloqueio inicial para demonstracao");

        executarCenario("Usuario autorizado acessando recurso permitido", () ->
            imprimirAcesso(autorizacaoService, administrador, "USER_CREATE")
        );

        executarCenario("Usuario tentando acessar recurso sem permissao", () ->
            imprimirAcesso(autorizacaoService, usuarioComum, "USER_DELETE")
        );

        executarCenario("Usuario bloqueado tentando acessar qualquer recurso", () ->
            imprimirAcesso(autorizacaoService, usuarioBloqueado, "REPORT_VIEW")
        );

        executarCenario("Usuario com multiplos papeis", () -> {
            imprimirAcesso(autorizacaoService, gerente, "REPORT_VIEW");
            imprimirAcesso(autorizacaoService, gerente, "REPORT_EXPORT");
        });

        executarCenario("Remocao de permissao e nova verificacao", () -> {
            imprimirAcesso(autorizacaoService, gerente, "REPORT_EXPORT");
            papelService.removerPermissao(manager.id(), reportExport.id());
            imprimirAcesso(autorizacaoService, gerente, "REPORT_EXPORT");
        });

        executarCenario("Remocao de papel do usuario", () -> {
            imprimirAcesso(autorizacaoService, usuarioComum, "REPORT_VIEW");
            usuarioService.removerPapel(usuarioComum.id(), basicUser.id());
            imprimirAcesso(autorizacaoService, usuarioComum, "REPORT_VIEW");
        });

        executarCenario("Desbloqueio de usuario", () -> {
            imprimirAcesso(autorizacaoService, usuarioBloqueado, "REPORT_VIEW");
            usuarioService.desbloquear(usuarioBloqueado.id(), "Desbloqueio para demonstracao");
            imprimirAcesso(autorizacaoService, usuarioBloqueado, "REPORT_VIEW");
        });

        executarCenario("Permissoes efetivas por usuario", () -> {
            imprimirPermissoesEfetivas(usuarioService, administrador);
            imprimirPermissoesEfetivas(usuarioService, gerente);
            imprimirPermissoesEfetivas(usuarioService, usuarioAuditor);
            imprimirPermissoesEfetivas(usuarioService, usuarioComum);
            imprimirPermissoesEfetivas(usuarioService, usuarioBloqueado);
        });

        executarCenario("Historico de tentativas autorizadas", () ->
            auditoriaService.listarTentativasAutorizadas().forEach(registro -> System.out.println("  " + registro.resumo()))
        );

        executarCenario("Historico de tentativas negadas", () ->
            auditoriaService.listarTentativasNegadas().forEach(registro -> System.out.println("  " + registro.resumo()))
        );

        executarCenario("Alteracoes em papeis e usuarios", () ->
            auditoriaService.listarAlteracoes().forEach(registro -> System.out.println("  " + registro.resumo()))
        );

        imprimirRodape();
    }

    private void imprimirAcesso(AutorizacaoService autorizacaoService, Usuario usuario, String codigoPermissao) {
        DecisaoAcesso decisao = autorizacaoService.verificarAcesso(usuario.id(), codigoPermissao);
        System.out.printf(
            "  Usuario=%s | Tipo=%s | Acao=%s | Resultado=%s | Motivo=%s%n",
            usuario.identificador(),
            usuario.tipo(),
            codigoPermissao,
            decisao.resultado(),
            decisao.motivo()
        );
    }

    private void imprimirPermissoesEfetivas(UsuarioService usuarioService, Usuario usuario) {
        System.out.printf(
            "  Usuario=%s | Tipo=%s | Bloqueado=%s | Permissoes=%s%n",
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
        } catch (RuntimeException e) {
            System.out.println("  Erro: " + e.getMessage());
        }
    }

    private void imprimirCabecalho() {
        System.out.println("============================================");
        System.out.println(" SISTEMA DE CONTROLE DE PERMISSOES E ACESSOS");
        System.out.println(" Cenarios manuais obrigatorios");
        System.out.println("============================================");
    }

    private void imprimirRodape() {
        System.out.println();
        System.out.println("============================================");
        System.out.println(" Fim da execucao dos cenarios");
        System.out.println("============================================");
    }
}
