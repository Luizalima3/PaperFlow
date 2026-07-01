package paperflow.aplicacao;

import paperflow.aplicacao.acoes.AcaoAdministrativa;
import paperflow.dominio.*;
import paperflow.dominio.categoria.CategoriaEvento;
import paperflow.infra.notificacao.CanalNotificacoes;
import paperflow.infra.notificacao.Email;
import paperflow.infra.notificacao.MensagemNotificacao;
import paperflow.infra.notificacao.OuvinteEmail;
import paperflow.infra.notificacao.ServicoEmail;
import paperflow.infra.notificacao.ServicoEmailMemoria;
import paperflow.infra.notificacao.TipoNotificacao;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class SistemaSubmissoes {
    private final Map<String, Usuario> usuarios = new HashMap<>();
    private final Set<String> areasTematicas = new HashSet<>();
    private final Map<String, Artigo> artigos = new HashMap<>();
    private final Set<String> revisoresComite = new HashSet<>();
    private final Map<String, List<Revisao>> revisoesPorArtigo = new HashMap<>();
    private final Map<String, Integer> cargaPorRevisor = new HashMap<>();
    private final ServicoEmail servicoEmail;
    private final CanalNotificacoes canalNotificacoes;
    private final int revisoresPorArtigo;
    private Evento eventoAtual;

    public SistemaSubmissoes() {
        this(new ServicoEmailMemoria(), 2);
    }

    public SistemaSubmissoes(ServicoEmail servicoEmail, int revisoresPorArtigo) {
        if (servicoEmail == null) {
            throw new IllegalArgumentException("Servico de e-mail e obrigatorio.");
        }
        if (revisoresPorArtigo < 1) {
            throw new IllegalArgumentException("Deve haver ao menos um revisor por artigo.");
        }
        this.servicoEmail = servicoEmail;
        this.canalNotificacoes = new CanalNotificacoes();
        this.canalNotificacoes.adicionarOuvinte(new OuvinteEmail(servicoEmail));
        this.revisoresPorArtigo = revisoresPorArtigo;
    }

    // RF01 - Start
    public void iniciarNovoEvento(String nome, String cidade, String periodo, LocalDateTime dataLimite, CategoriaEvento categoria) {
        this.eventoAtual = new Evento(nome, cidade, periodo, dataLimite, categoria);
        this.artigos.clear();
        this.revisoesPorArtigo.clear();
        this.revisoresComite.clear();
        this.cargaPorRevisor.clear();
    }

    // RF02 - Cadastro de Usuários
    public void cadastrarUsuario(String email, String senha, String instituicao) {
        if (usuarios.containsKey(email)) throw new IllegalArgumentException("Usuário já cadastrado: " + email);
        usuarios.put(email, new Usuario(email, senha, instituicao));
    }

    // RF03 - Cadastro de Áreas
    public void cadastrarAreaTematica(String area) {
        areasTematicas.add(area.toLowerCase().trim());
    }

    // RF04 - Organizacao do comite tecnico
    public void registrarRevisorNoComite(String email, Set<String> especialidades) {
        Usuario usuario = usuarios.get(email);
        if (usuario == null) {
            throw new IllegalArgumentException("Revisor nao cadastrado como usuario: " + email);
        }

        if (especialidades != null) {
            for (String especialidade : especialidades) {
                if (especialidade != null && !especialidade.isBlank()) {
                    usuario.adicionarEspecialidade(especialidade);
                }
            }
        }

        revisoresComite.add(email);
        cargaPorRevisor.putIfAbsent(email, 0);
    }

    // RF05 - Submissão de Artigos
    public void submeterArtigo(String id, String titulo, String resumo, String emailAutor, List<String> emailsCoautores, Set<String> areas) {
        if (eventoAtual == null || !eventoAtual.estaAberto()) {
            throw new IllegalStateException("Submissão rejeitada: O sistema está fechado ou o prazo expirou.");
        }

        Usuario autor = usuarios.get(emailAutor);
        if (autor == null) throw new IllegalArgumentException("Autor principal não cadastrado.");

        List<Usuario> coautoresValidados = new ArrayList<>();
        for (String emailCoautor : emailsCoautores) {
            Usuario coautor = usuarios.get(emailCoautor);
            if (coautor != null) coautoresValidados.add(coautor);
        }

        Set<String> areasNormalizadas = normalizarAreas(areas);

        if (!areasTematicas.containsAll(areasNormalizadas)) {
            throw new IllegalArgumentException("O artigo possui area tematica nao cadastrada no evento.");
        }

        Artigo artigo = new Artigo.Builder(id, titulo, autor)
                .comResumo(resumo)
                .comCoautores(coautoresValidados)
                .comAreasTematicas(areasNormalizadas)
                .build();

        artigos.put(id, artigo);
    }

    // RF06 - Distribuicao automatica com afinidade e sem conflito de interesse
    public void distribuirArtigosAutomaticamente() {
        if (revisoresComite.isEmpty()) {
            throw new IllegalStateException("Nao ha revisores registrados no comite tecnico.");
        }

        for (Artigo artigo : artigos.values()) {
            List<Revisao> revisoesAtuais = revisoesPorArtigo.computeIfAbsent(artigo.getId(), key -> new ArrayList<>());
            int vagas = Math.max(0, revisoresPorArtigo - revisoesAtuais.size());
            if (vagas == 0) {
                continue;
            }

            List<Usuario> candidatosOrdenados = selecionarCandidatosOrdenados(artigo, revisoesAtuais);
            for (int i = 0; i < Math.min(vagas, candidatosOrdenados.size()); i++) {
                Usuario revisor = candidatosOrdenados.get(i);
                Revisao revisao = new Revisao(artigo.getId(), revisor);
                revisoesAtuais.add(revisao);
                cargaPorRevisor.put(revisor.getEmail(), cargaPorRevisor.getOrDefault(revisor.getEmail(), 0) + 1);

                canalNotificacoes.notificar(new MensagemNotificacao(
                    TipoNotificacao.CONVITE_REVISAO,
                    revisor.getEmail(),
                    artigo,
                    Collections.emptyList()));
            }

            if (!revisoesAtuais.isEmpty()) {
                artigo.setStatus(new StatusEmRevisao());
            }
        }
    }

    // RF07 - Revisor aceita e conclui revisao
    public void aceitarRevisao(String artigoId, String emailRevisor) {
        Revisao revisao = obterRevisao(artigoId, emailRevisor);
        revisao.aceitar();
    }

    public void concluirRevisao(String artigoId, String emailRevisor, String contribuicao, String critica, Veredito veredito) {
        Revisao revisao = obterRevisao(artigoId, emailRevisor);
        Parecer parecer = new Parecer(contribuicao, critica, veredito);
        revisao.concluir(parecer);
    }

    // RF08 - Dashboard consolidado
    public DashboardResumo gerarDashboard() {
        int avaliados = 0;
        int pendentes = 0;
        List<String> pendencias = new ArrayList<>();

        for (Artigo artigo : artigos.values()) {
            List<Revisao> revisoes = revisoesPorArtigo.getOrDefault(artigo.getId(), Collections.emptyList());

            if (revisoes.isEmpty()) {
                pendentes++;
                pendencias.add("Artigo " + artigo.getId() + " pendente: sem revisor designado.");
                continue;
            }

            List<String> revisoresPendentes = revisoes.stream()
                    .filter(revisao -> revisao.getEstado() != EstadoRevisao.CONCLUIDA)
                    .map(revisao -> revisao.getRevisor().getEmail())
                    .collect(Collectors.toList());

            if (revisoresPendentes.isEmpty()) {
                avaliados++;
            } else {
                pendentes++;
                pendencias.add("Artigo " + artigo.getId() + " pendente com: " + String.join(", ", revisoresPendentes));
            }
        }

        return new DashboardResumo(
                artigos.size(),
                revisoresComite.size(),
                avaliados,
                pendentes,
                pendencias);
    }

    // RF09 - Fecha ciclo e notifica autores com pareceres
    public void fecharCicloRevisaoEPublicarResultado() {
        for (Artigo artigo : artigos.values()) {
            List<Revisao> revisoes = revisoesPorArtigo.getOrDefault(artigo.getId(), Collections.emptyList());
            if (revisoes.isEmpty()) {
                continue;
            }

            boolean todasConcluidas = revisoes.stream().allMatch(revisao -> revisao.getEstado() == EstadoRevisao.CONCLUIDA);
            if (!todasConcluidas) {
                continue;
            }

            double media = revisoes.stream()
                    .map(Revisao::getParecer)
                    .map(Parecer::getVeredito)
                    .mapToInt(Veredito::getPontuacao)
                    .average()
                    .orElse(0.0);

            if (media >= 3.0) {
                artigo.setStatus(new StatusAceito());
            } else {
                artigo.setStatus(new StatusRejeitado());
            }

            enviarResultadoParaAutores(artigo, revisoes);
        }
    }

    // RF10 - Acoes administrativas encapsuladas
    public void executarAcao(AcaoAdministrativa acao) {
        if (acao == null) {
            throw new IllegalArgumentException("Acao administrativa obrigatoria.");
        }
        acao.executar(this);
    }

    public void cancelarDistribuicao(String artigoId) {
        Artigo artigo = obterArtigo(artigoId);
        List<Revisao> removidas = revisoesPorArtigo.remove(artigoId);
        if (removidas != null) {
            for (Revisao revisao : removidas) {
                String email = revisao.getRevisor().getEmail();
                int cargaAtual = cargaPorRevisor.getOrDefault(email, 0);
                cargaPorRevisor.put(email, Math.max(0, cargaAtual - 1));
            }
        }
        artigo.setStatus(new StatusSubmetido());
    }

    public void reenviarNotificacoesResultado(String artigoId) {
        Artigo artigo = obterArtigo(artigoId);
        List<Revisao> revisoes = revisoesPorArtigo.getOrDefault(artigoId, Collections.emptyList());
        if (revisoes.isEmpty()) {
            throw new IllegalStateException("Nao ha revisoes para reenviar notificacoes.");
        }
        if (!"Aceito".equals(artigo.getNomeStatus()) && !"Rejeitado".equals(artigo.getNomeStatus())) {
            throw new IllegalStateException("O artigo ainda nao teve resultado final.");
        }
        enviarResultadoParaAutores(artigo, revisoes);
    }

    public void reabrirSubmissao() {
        if (eventoAtual == null) {
            throw new IllegalStateException("Nao ha evento ativo para reabrir submissao.");
        }
        eventoAtual.setAberto(true);
    }

    public void aprovarPublicacaoFinal(String artigoId) {
        Artigo artigo = obterArtigo(artigoId);
        artigo.setStatus(new StatusAceito());
    }

    public List<Revisao> getRevisoesDoArtigo(String artigoId) {
        return Collections.unmodifiableList(revisoesPorArtigo.getOrDefault(artigoId, Collections.emptyList()));
    }

    public List<Email> getEmailsEnviados() {
        if (servicoEmail instanceof ServicoEmailMemoria) {
            return ((ServicoEmailMemoria) servicoEmail).getCaixaDeSaida();
        }
        return Collections.emptyList();
    }

    // Getters de infraestrutura para a Pessoa 2 usar nas revisões e dashboards
    public Map<String, Artigo> getArtigos() { return Collections.unmodifiableMap(artigos); }
    public Map<String, Usuario> getUsuarios() { return Collections.unmodifiableMap(usuarios); }
    public Set<String> getAreasTematicas() { return Collections.unmodifiableSet(areasTematicas); }
    public Set<String> getRevisoresComite() { return Collections.unmodifiableSet(revisoresComite); }
    public Evento getEventoAtual() { return eventoAtual; }

    private Set<String> normalizarAreas(Set<String> areas) {
        if (areas == null) {
            return Collections.emptySet();
        }
        Set<String> normalizadas = new HashSet<>();
        for (String area : areas) {
            if (area != null && !area.isBlank()) {
                normalizadas.add(area.toLowerCase().trim());
            }
        }
        return normalizadas;
    }

    private List<Usuario> selecionarCandidatosOrdenados(Artigo artigo, List<Revisao> revisoesAtuais) {
        Set<String> jaSelecionados = revisoesAtuais.stream()
                .map(revisao -> revisao.getRevisor().getEmail())
                .collect(Collectors.toSet());

        List<Usuario> candidatos = new ArrayList<>();
        for (String emailRevisor : revisoresComite) {
            Usuario revisor = usuarios.get(emailRevisor);
            if (revisor == null) {
                continue;
            }
            if (jaSelecionados.contains(emailRevisor)) {
                continue;
            }
            if (existeConflitoInteresse(artigo, revisor)) {
                continue;
            }
            candidatos.add(revisor);
        }

        candidatos.sort((a, b) -> {
            int scoreA = scoreAfinidade(artigo, a);
            int scoreB = scoreAfinidade(artigo, b);
            boolean compativelA = scoreA > 0;
            boolean compativelB = scoreB > 0;

            if (compativelA != compativelB) {
                return compativelA ? -1 : 1;
            }

            int cargaA = cargaPorRevisor.getOrDefault(a.getEmail(), 0);
            int cargaB = cargaPorRevisor.getOrDefault(b.getEmail(), 0);
            if (cargaA != cargaB) {
                return Integer.compare(cargaA, cargaB);
            }

            if (scoreA != scoreB) {
                return Integer.compare(scoreB, scoreA);
            }
            return a.getEmail().compareTo(b.getEmail());
        });

        return candidatos;
    }

    private int scoreAfinidade(Artigo artigo, Usuario revisor) {
        Set<String> areasArtigo = artigo.getAreasTematicas();
        Set<String> especialidades = revisor.getEspecialidades();
        int score = 0;
        for (String area : areasArtigo) {
            if (especialidades.contains(area)) {
                score++;
            }
        }
        return score;
    }

    private boolean existeConflitoInteresse(Artigo artigo, Usuario revisor) {
        if (artigo.getAutorPrincipal().getEmail().equals(revisor.getEmail())) {
            return true;
        }
        for (Usuario coautor : artigo.getCoautores()) {
            if (coautor.getEmail().equals(revisor.getEmail())) {
                return true;
            }
        }
        return false;
    }

    private Revisao obterRevisao(String artigoId, String emailRevisor) {
        List<Revisao> revisoes = revisoesPorArtigo.getOrDefault(artigoId, Collections.emptyList());
        return revisoes.stream()
                .filter(revisao -> revisao.getRevisor().getEmail().equals(emailRevisor))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Revisao nao encontrada para este revisor e artigo."));
    }

    private void enviarResultadoParaAutores(Artigo artigo, List<Revisao> revisoes) {
        canalNotificacoes.notificar(new MensagemNotificacao(
                TipoNotificacao.RESULTADO_AUTOR,
                artigo.getAutorPrincipal().getEmail(),
                artigo,
                revisoes));

        for (Usuario coautor : artigo.getCoautores()) {
                canalNotificacoes.notificar(new MensagemNotificacao(
                    TipoNotificacao.RESULTADO_AUTOR,
                    coautor.getEmail(),
                    artigo,
                    revisoes));
        }
    }

    private Artigo obterArtigo(String artigoId) {
        Artigo artigo = artigos.get(artigoId);
        if (artigo == null) {
            throw new IllegalArgumentException("Artigo nao encontrado: " + artigoId);
        }
        return artigo;
    }

    /**
     * Enviar e-mail para qualquer destinatario (usuario geral, professor, etc)
     */
    public void enviarEmail(String destinatario, String assunto, String corpo) {
        servicoEmail.enviar(destinatario, assunto, corpo);
    }
}
