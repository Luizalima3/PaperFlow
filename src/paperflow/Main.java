package paperflow;

import paperflow.aplicacao.SistemaSubmissoes;
import paperflow.aplicacao.acoes.AcaoAprovarPublicacaoFinal;
import paperflow.aplicacao.acoes.AcaoReabrirSubmissao;
import paperflow.aplicacao.acoes.AcaoReenviarNotificacoes;
import paperflow.dominio.DashboardResumo;
import paperflow.dominio.Veredito;
import paperflow.dominio.categoria.CategoriaEvento;
import paperflow.dominio.categoria.CategoriaFullPaper;
import paperflow.infra.notificacao.Email;
import java.time.LocalDateTime;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        SistemaSubmissoes sistema = new SistemaSubmissoes();

        System.out.println("=== SIMULACAO COMPLETA: SUBMISSAO, REVISAO E RESULTADO ===");

        try {
            sistema.cadastrarUsuario("jose.cruz@ifpb.edu.br", "senha123", "IFPB");
            sistema.cadastrarUsuario("maria.geografia@escola.com", "senha456", "UFPB");
            sistema.cadastrarUsuario("coordenadora.damires@ifpb.edu.br", "senha789", "IFPB");
            sistema.cadastrarUsuario("coautor.silva@ifpb.edu.br", "abc111", "IFPB");
            sistema.cadastrarUsuario("revisor.ana@ifpb.edu.br", "r1", "IFPB");
            sistema.cadastrarUsuario("revisor.bruno@ifpb.edu.br", "r2", "UFPB");
            sistema.cadastrarUsuario("revisor.clara@ifpb.edu.br", "r3", "UFPE");
            System.out.println("[OK] Usuarios de teste cadastrados.");

            sistema.cadastrarAreaTematica("Inteligência Artificial");
            sistema.cadastrarAreaTematica("Engenharia de Software");
            sistema.cadastrarAreaTematica("Machine Learning");
            sistema.cadastrarAreaTematica("Visao Computacional");
            System.out.println("[OK] Areas tematicas do evento configuradas.");

            CategoriaEvento categoriaFullPaper = new CategoriaFullPaper();
            LocalDateTime prazoSubmissao = LocalDateTime.now().plusDays(1);

            sistema.iniciarNovoEvento(
                "Simpósio Brasileiro de Sistemas de Informação (SBSI) - 2026",
                "Vitória - ES",
                "25 de maio a 28 de maio de 2026",
                prazoSubmissao,
                categoriaFullPaper
            );
            System.out.println("[OK] Evento '" + sistema.getEventoAtual().getNome() + "' iniciado.");
            System.out.println("     Categoria: " + sistema.getEventoAtual().getCategoria().getNomeCategoria());

            sistema.registrarRevisorNoComite(
                    "revisor.ana@ifpb.edu.br",
                    new HashSet<>(Arrays.asList("inteligência artificial", "machine learning", "visao computacional")));
            sistema.registrarRevisorNoComite(
                    "revisor.bruno@ifpb.edu.br",
                    new HashSet<>(Arrays.asList("engenharia de software", "machine learning")));
            sistema.registrarRevisorNoComite(
                    "revisor.clara@ifpb.edu.br",
                    new HashSet<>(Arrays.asList("engenharia de software", "visao computacional")));
            System.out.println("[OK] Comite tecnico registrado com " + sistema.getRevisoresComite().size() + " revisores.");

            Set<String> areasArtigo1 = new HashSet<>(Arrays.asList("Engenharia de Software", "Machine Learning"));
            Set<String> areasArtigo2 = new HashSet<>(Arrays.asList("Visao Computacional"));
            List<String> coautores = Arrays.asList("coautor.silva@ifpb.edu.br");

            sistema.submeterArtigo(
                "249227",
                "Um Levantamento sobre os principais erros cometidos na aplicação de padrões de projeto de software",
                "Este artigo faz um levantamento prático de vários erros de aplicação de padrões...",
                "jose.cruz@ifpb.edu.br",
                coautores,
                areasArtigo1
            );

            sistema.submeterArtigo(
                "249228",
                "Sistema de apoio visual para leitura de exames",
                "Trabalho de demonstracao com tecnicas de visao computacional.",
                "maria.geografia@escola.com",
                Collections.emptyList(),
                areasArtigo2
            );
            System.out.println("[OK] Artigos submetidos: " + sistema.getArtigos().size());

            sistema.distribuirArtigosAutomaticamente();
            System.out.println("[OK] Distribuicao automatica realizada.");

            sistema.executarAcao(new AcaoReabrirSubmissao());

            sistema.aceitarRevisao("249227", "revisor.ana@ifpb.edu.br");
            sistema.aceitarRevisao("249227", "revisor.bruno@ifpb.edu.br");
            sistema.aceitarRevisao("249228", "revisor.clara@ifpb.edu.br");
            sistema.aceitarRevisao("249228", "revisor.ana@ifpb.edu.br");

            sistema.concluirRevisao(
                    "249227",
                    "revisor.ana@ifpb.edu.br",
                    "Boa metodologia e delimitacao clara do problema.",
                    "Poderia trazer avaliacao experimental mais ampla.",
                    Veredito.FRACAMENTE_ACEITO);
            sistema.concluirRevisao(
                    "249227",
                    "revisor.bruno@ifpb.edu.br",
                    "Tema relevante para qualidade de software.",
                    "Faltou comparacao com trabalhos mais recentes.",
                    Veredito.ACEITO);
            sistema.concluirRevisao(
                    "249228",
                    "revisor.clara@ifpb.edu.br",
                    "Prototipo funcional e facil de reproduzir.",
                    "Resultados ainda preliminares.",
                    Veredito.FRACAMENTE_ACEITO);
            sistema.concluirRevisao(
                    "249228",
                    "revisor.ana@ifpb.edu.br",
                    "Contribuicao pratica para apoio a diagnostico.",
                    "Necessita ampliar base de teste.",
                    Veredito.FRACAMENTE_ACEITO);

            DashboardResumo dashboard = sistema.gerarDashboard();
            System.out.println("\n=== DASHBOARD ===");
            System.out.println("Total de artigos: " + dashboard.getTotalArtigos());
            System.out.println("Total de revisores: " + dashboard.getTotalRevisores());
            System.out.println("Artigos avaliados: " + dashboard.getTotalArtigosAvaliados());
            System.out.println("Pendentes: " + dashboard.getTotalPendentes());
            for (String pendencia : dashboard.getPendencias()) {
                System.out.println("- " + pendencia);
            }

            sistema.fecharCicloRevisaoEPublicarResultado();
            sistema.executarAcao(new AcaoReenviarNotificacoes("249227"));
            sistema.executarAcao(new AcaoAprovarPublicacaoFinal("249228"));

            System.out.println("\n=== RESULTADO FINAL ===");
            sistema.getArtigos().values().forEach(artigo ->
                    System.out.println("Artigo " + artigo.getId() + " => " + artigo.getNomeStatus()));

            System.out.println("\n=== E-MAILS ENVIADOS ===");
            for (Email email : sistema.getEmailsEnviados()) {
                System.out.println("Para: " + email.getDestinatario());
                System.out.println("Assunto: " + email.getAssunto());
                System.out.println(email.getCorpo());
                System.out.println("---------------------------");
            }

        } catch (Exception e) {
            System.err.println("Erro inesperado na simulação: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
