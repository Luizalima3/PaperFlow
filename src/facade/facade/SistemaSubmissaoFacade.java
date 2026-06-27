package facade;

import model.*;
import strategy.TipoEventoStrategy;
import java.time.LocalDateTime;
import java.util.*;

public class SistemaSubmissaoFacade {
    private final Map<String, Usuario> usuarios = new HashMap<>();
    private final Set<String> areasTematicas = new HashSet<>();
    private final Map<String, Artigo> artigos = new HashMap<>();
    private Evento eventoAtual;

    // RF01 - Start
    public void iniciarNovoEvento(String nome, String cidade, String periodo, LocalDateTime dataLimite, TipoEventoStrategy categoria) {
        this.eventoAtual = new Evento(nome, cidade, periodo, dataLimite, categoria);
        this.artigos.clear(); 
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

        Artigo artigo = new Artigo.Builder(id, titulo, autor)
                .comResumo(resumo)
                .comCoautores(coautoresValidados)
                .comAreasTematicas(areas)
                .build();

        artigos.put(id, artigo);
    }

    // Getters de infraestrutura para a Pessoa 2 usar nas revisões e dashboards
    public Map<String, Artigo> getArtigos() { return Collections.unmodifiableMap(artigos); }
    public Map<String, Usuario> getUsuarios() { return Collections.unmodifiableMap(usuarios); }
    public Set<String> getAreasTematicas() { return Collections.unmodifiableSet(areasTematicas); }
    public Evento getEventoAtual() { return eventoAtual; }
}
