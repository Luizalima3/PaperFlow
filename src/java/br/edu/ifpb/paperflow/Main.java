package br.edu.ifpb.paperflow;

import br.edu.ifpb.paperflow.aplicacao.SistemaSubmissoes;
import br.edu.ifpb.paperflow.aplicacao.acoes.AcaoAprovarPublicacaoFinal;
import br.edu.ifpb.paperflow.aplicacao.acoes.AcaoReabrirSubmissao;
import br.edu.ifpb.paperflow.aplicacao.acoes.AcaoReenviarNotificacoes;
import br.edu.ifpb.paperflow.dominio.DashboardResumo;
import br.edu.ifpb.paperflow.dominio.Veredito;
import br.edu.ifpb.paperflow.dominio.categoria.CategoriaEvento;
import br.edu.ifpb.paperflow.dominio.categoria.CategoriaFullPaper;
import br.edu.ifpb.paperflow.infra.notificacao.Email;
import br.edu.ifpb.paperflow.infra.notificacao.ServicoEmail;
import br.edu.ifpb.paperflow.infra.notificacao.ServicoEmailSmtp;
import java.time.LocalDateTime;
import java.util.*;

public class Main {
    
    private static class ServicoEmailTeste implements ServicoEmail {
        @Override
        public void enviar(String destinatario, String assunto, String corpo) {
            System.out.println("[SIMULADO] Email para: " + destinatario);
        }
    }

    private static void etapa(String titulo) {
        System.out.println("\n" + titulo);
    }
    
    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("  Sistema de Submissao e Revisao de Artigos");
        System.out.println("  IFPB - Padrões de Projeto de Software");
        System.out.println("=================================================\n");
        
        // Para enviar emails de verdade, descomente a linha abaixo e coloque suas credenciais:
        // ServicoEmail servicoEmail = new ServicoEmailSmtp("smtp.gmail.com", 587, "lui", "bhla ltde rvcm enad");
        
        // Modo TESTE - apenas simula emails (padrão)
        // Comente a linha abaixo e descomente a linha acima para enviar emails de verdade
        ServicoEmail servicoEmail = new ServicoEmailTeste();
        System.out.println("[MODO TESTE] Emails serao apenas simulados (nao enviados).\n");
        
        SistemaSubmissoes sistema = new SistemaSubmissoes(servicoEmail, 2);

        try {
            etapa("[1] Cadastro de usuarios");
            sistema.cadastrarUsuario("jose.cruzz@ifpb.edu.br", "senha123", "IFPB");
            sistema.cadastrarUsuario("maria.geografiaaa@escola.com", "senha456", "UFPB");
            sistema.cadastrarUsuario("coordenadoraaaa.damires@ifpb.edu.br", "senha789", "IFPB");
            sistema.cadastrarUsuario("coautorr.silva@ifpb.edu.br", "abc111", "IFPB");
            sistema.cadastrarUsuario("revisor.anaa@ifpb.edu.br", "r1", "IFPB");
            sistema.cadastrarUsuario("revisor.brunoo@ifpb.edu.br", "r2", "UFPB");
            sistema.cadastrarUsuario("revisor.claraa@ifpb.edu.br", "r3", "UFPE");
            System.out.println("    Usuarios de teste cadastrados.");

            etapa("[2] Evento e areas tematicas");
            sistema.cadastrarAreaTematica("Inteligência Artificial");
            sistema.cadastrarAreaTematica("Engenharia de Software");
            sistema.cadastrarAreaTematica("Machine Learning");
            sistema.cadastrarAreaTematica("Visao Computacional");
            System.out.println("    Areas tematicas do evento configuradas.");

            CategoriaEvento categoriaFullPaper = new CategoriaFullPaper();
            LocalDateTime prazoSubmissao = LocalDateTime.now().plusDays(1);

            sistema.iniciarNovoEvento(
                "Simpósio Brasileiro de Sistemas de Informação (SBSI) - 2026",
                "Vitória - ES",
                "25 de maio a 28 de maio de 2026",
                prazoSubmissao,
                categoriaFullPaper
            );
            System.out.println("    Evento '" + sistema.getEventoAtual().getNome() + "' iniciado.");
            System.out.println("     Categoria: " + sistema.getEventoAtual().getCategoria().getNomeCategoria());

            etapa("[3] Comitê tecnico e revisores");
            sistema.registrarRevisorNoComite(
                    "revisor.anaa@ifpb.edu.br",
                    new HashSet<>(Arrays.asList("inteligência artificial", "machine learning", "visao computacional")));
            sistema.registrarRevisorNoComite(
                    "revisor.brunoo@ifpb.edu.br",
                    new HashSet<>(Arrays.asList("engenharia de software", "machine learning")));
            sistema.registrarRevisorNoComite(
                    "revisor.claraa@ifpb.edu.br",
                    new HashSet<>(Arrays.asList("engenharia de software", "visao computacional")));
                System.out.println("    Comite tecnico registrado com " + sistema.getRevisoresComite().size() + " revisores.");

                etapa("[4] Submissao de artigos");
            Set<String> areasArtigo1 = new HashSet<>(Arrays.asList("Engenharia de Software", "Machine Learning"));
            Set<String> areasArtigo2 = new HashSet<>(Arrays.asList("Visao Computacional"));
            List<String> coautores = Arrays.asList("coautorr.silva@ifpb.edu.br");

            sistema.submeterArtigo(
                "249227",
                "Um Levantamento sobre os principais erros cometidos na aplicação de padrões de projeto de software",
                "Este artigo faz um levantamento prático de vários erros de aplicação de padrões...",
                "jose.cruzz@ifpb.edu.br",
                coautores,
                areasArtigo1
            );

            sistema.submeterArtigo(
                "249228",
                "Sistema de apoio visual para leitura de exames",
                "Trabalho de demonstracao com tecnicas de visao computacional.",
                "maria.geografiaaa@escola.com",
                Collections.emptyList(),
                areasArtigo2
            );
            System.out.println("    Artigos submetidos: " + sistema.getArtigos().size());

            etapa("[5] Distribuicao automatica");
            sistema.distribuirArtigosAutomaticamente();
            System.out.println("    Distribuicao automatica realizada.");

            sistema.executarAcao(new AcaoReabrirSubmissao());

            etapa("[6] Avaliacao e agregacao de resultado");
            sistema.aceitarRevisao("249227", "revisor.anaa@ifpb.edu.br");
            sistema.aceitarRevisao("249227", "revisor.brunoo@ifpb.edu.br");
            sistema.aceitarRevisao("249228", "revisor.claraa@ifpb.edu.br");
            sistema.aceitarRevisao("249228", "revisor.anaa@ifpb.edu.br");

            sistema.concluirRevisao(
                    "249227",
                    "revisor.anaa@ifpb.edu.br",
                    "Boa metodologia e delimitacao clara do problema.",
                    "Poderia trazer avaliacao experimental mais ampla.",
                    Veredito.FRACAMENTE_ACEITO);
            sistema.concluirRevisao(
                    "249227",
                    "revisor.brunoo@ifpb.edu.br",
                    "Tema relevante para qualidade de software.",
                    "Faltou comparacao com trabalhos mais recentes.",
                    Veredito.ACEITO);
            sistema.concluirRevisao(
                    "249228",
                    "revisor.claraa@ifpb.edu.br",
                    "Prototipo funcional e facil de reproduzir.",
                    "Resultados ainda preliminares.",
                    Veredito.FRACAMENTE_ACEITO);
            sistema.concluirRevisao(
                    "249228",
                    "revisor.anaa@ifpb.edu.br",
                    "Contribuicao pratica para apoio a diagnostico.",
                    "Necessita ampliar base de teste.",
                    Veredito.FRACAMENTE_ACEITO);

                    etapa("[7] Encerramento e notificacoes");
                    sistema.fecharCicloRevisaoEPublicarResultado();
                    sistema.executarAcao(new AcaoReenviarNotificacoes("249227"));
                    sistema.executarAcao(new AcaoAprovarPublicacaoFinal("249228"));

                    etapa("[8] Dashboard e resultado final");

            DashboardResumo dashboard = sistema.gerarDashboard();
                    System.out.println("\n=== DASHBOARD ===");
            System.out.println("Total de artigos: " + dashboard.getTotalArtigos());
            System.out.println("Total de revisores: " + dashboard.getTotalRevisores());
            System.out.println("Artigos avaliados: " + dashboard.getTotalArtigosAvaliados());
            System.out.println("Pendentes: " + dashboard.getTotalPendentes());
            for (String pendencia : dashboard.getPendencias()) {
                System.out.println("- " + pendencia);
            }

            System.out.println("\n=== RESULTADO FINAL ===");
            sistema.getArtigos().values().forEach(artigo ->
                    System.out.println("Artigo " + artigo.getId() + " => " + artigo.getNomeStatus()));

                    etapa("[9] Teste de envio e historico de emails");
            sistema.enviarEmail(
                    "maria.lima.5@academico.ifpb.edu.br",
                    "Teste de E-mail do Sistema PaperFlow",
                    "Olá Maria,\n\n" +
                    "Este é um teste do sistema de e-mail do PaperFlow.\n" +
                    "Se você recebeu esta mensagem, o envio está funcionando corretamente!\n\n" +
                    "Atenciosamente,\nSistema PaperFlow"
            );

            System.out.println("\n=== E-MAILS ENVIADOS ===");
            for (Email email : sistema.getEmailsEnviados()) {
                System.out.println("Para: " + email.getDestinatario());
                System.out.println("Assunto: " + email.getAssunto());
                System.out.println(email.getCorpo());
                System.out.println("---------------------------");
            }

            System.out.println("\n=================================================");
            System.out.println("  Fim da demonstracao");
            System.out.println("=================================================");

        } catch (Exception e) {
            System.err.println("Erro inesperado na simulação: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
