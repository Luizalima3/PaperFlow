package paperflow.dominio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

// Esta classe utiliza o padrão Builder (a classe interna Builder). O construtor do artigo é privado, ou seja, ninguém consegue dar um new Artigo() fora daqui sem passar pelo processo de montagem correto.

public class Artigo {
    private final String id;
    private final String titulo;
    private final String resumo;
    private final Usuario autorPrincipal;
    private final List<Usuario> coautores;
    private final Set<String> areasTematicas;
    private StatusArtigo status;

    private Artigo(Builder builder) {
        this.id = builder.id;
        this.titulo = builder.titulo;
        this.resumo = builder.resumo;
        this.autorPrincipal = builder.autorPrincipal;
        this.coautores = builder.coautores;
        this.areasTematicas = builder.areasTematicas;
        this.status = new StatusSubmetido();
    }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getResumo() { return resumo; }
    public Usuario getAutorPrincipal() { return autorPrincipal; }
    public List<Usuario> getCoautores() { return Collections.unmodifiableList(coautores); }
    public Set<String> getAreasTematicas() { return Collections.unmodifiableSet(areasTematicas); }
    public String getNomeStatus() { return status.getNome(); }

    public void setStatus(StatusArtigo novoStatus) { this.status = novoStatus; }

    // --- IMPLEMENTAÇÃO DO PADRÃO BUILDER ---
    // Uma classe estática interna dedicada a fabricar artigos com segurança
    // --- PADRÃO BUILDER ---
    public static class Builder {
        private final String id;
        private final String titulo;
        private final Usuario autorPrincipal;
        private String resumo;
        private List<Usuario> coautores = new ArrayList<>();
        private Set<String> areasTematicas;

        public Builder(String id, String titulo, Usuario autorPrincipal) {
            if (id == null || titulo == null || autorPrincipal == null) {
                throw new IllegalArgumentException("ID, Título e Autor são obrigatórios.");
            }
            this.id = id;
            this.titulo = titulo;
            this.autorPrincipal = autorPrincipal;
        }

        public Builder comResumo(String resumo) {
            this.resumo = resumo;
            return this;
        }

        public Builder comCoautores(List<Usuario> coautores) {
            if (coautores != null) this.coautores = coautores;
            return this;
        }

        public Builder comAreasTematicas(Set<String> areasTematicas) {
            this.areasTematicas = areasTematicas;
            return this;
        }

        public Artigo build() {
            if (resumo == null || resumo.isBlank()) throw new IllegalArgumentException("Resumo é obrigatório.");
            if (areasTematicas == null || areasTematicas.isEmpty()) throw new IllegalArgumentException("Selecione ao menos uma área.");
            return new Artigo(this);
        }
    }
}
