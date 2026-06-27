import facade.SistemaSubmissaoFacade;
import strategy.*;
import java.time.LocalDateTime;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Inicializa a fachada do sistema (sua parte central)
        SistemaSubmissaoFacade sistema = new SistemaSubmissaoFacade();

        System.out.println("=== SIMULAÇÃO DA PARTE 1: SISTEMA DE SUBMISSÃO ===");

        try {
            // 1. POVOANDO USUÁRIOS (Simulando a leitura automática de dados/CSV)
            sistema.cadastrarUsuario("jose.cruz@ifpb.edu.br", "senha123", "IFPB");
            sistema.cadastrarUsuario("maria.geografia@escola.com", "senha456", "UFPB");
            sistema.cadastrarUsuario("coordenadora.damires@ifpb.edu.br", "senha789", "IFPB");
            sistema.cadastrarUsuario("coautor.silva@ifpb.edu.br", "abc111", "IFPB");
            System.out.println("[OK] Usuários de teste cadastrados com sucesso.");

            // 2. CADASTRENDO ÁREAS TEMÁTICAS (RF03)
            sistema.cadastrarAreaTematica("Inteligência Artificial");
            sistema.cadastrarAreaTematica("Engenharia de Software");
            sistema.cadastrarAreaTematica("Machine Learning");
            System.out.println("[OK] Áreas temáticas do evento configuradas.");

            // 3. INICIANDO UM NOVO EVENTO (RF01 e RF04)
            // Criando um evento do tipo "Full Paper" com prazo limite para amanhã
            TipoEventoStrategy categoriaFullPaper = new FullPaperStrategy();
            LocalDateTime prazoSubmissao = LocalDateTime.now().plusDays(1); 
            
            sistema.iniciarNovoEvento(
                "Simpósio Brasileiro de Sistemas de Informação (SBSI) - 2026",
                "Vitória - ES",
                "25 de maio a 28 de maio de 2026",
                prazoSubmissao,
                categoriaFullPaper
            );
            System.out.println("[OK] Evento '" + sistema.getEventoAtual().getNome() + "' iniciado com sucesso!");
            System.out.println("     Categoria: " + sistema.getEventoAtual().getCategoria().getNomeCategoria());

            // 4. SUBMETENDO UM ARTIGO COM SUCESSO (RF05)
            Set<String> areasArtigo = new HashSet<>(Arrays.asList("Engenharia de Software"));
            List<String> coautores = Arrays.asList("coautor.silva@ifpb.edu.br");
            
            sistema.submeterArtigo(
                "249227", // ID do artigo
                "Um Levantamento sobre os principais erros cometidos na aplicação de padrões de projeto de software",
                "Este artigo faz um levantamento prático de vários erros de aplicação de padrões...",
                "jose.cruz@ifpb.edu.br", // Autor Principal
                coautores,
                areasArtigo
            );
            System.out.println("[OK] Artigo ID 249227 submetido com sucesso!");

            // 5. TESTANDO AS VALIDAÇÕES (Garantindo que o sistema barra erros sem usar System.out interno)
            System.out.println("\n--- Testando validações de segurança ---");
            
            try {
                // Tentando submeter artigo com autor inexistente
                sistema.submeterArtigo("1111", "Título X", "Resumo X", "invalido@email.com", coautores, areasArtigo);
            } catch (IllegalArgumentException e) {
                System.out.println("Pegou erro esperado (Autor Inválido): " + e.getMessage());
            }

            // 6. EXIBINDO OS DADOS CONSOLIDADOS PARA A PESSOA 2
            System.out.println("\n=== Resumo dos dados prontos na memória para a Parte 2 ===");
            System.out.println("Total de Artigos na Facade: " + sistema.getArtigos().size());
            sistema.getArtigos().values().forEach(artigo -> {
                System.out.println("- Artigo: " + artigo.getTitulo());
                System.out.println("  Autor Principal: " + artigo.getAutorPrincipal().getEmail());
                System.out.println("  Status Inicial (Padrão State): " + artigo.getNomeStatus());
            });

        } catch (Exception e) {
            System.err.println("Erro inesperado na simulação: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
