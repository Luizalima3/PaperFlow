package br.edu.ifpb.paperflow.dominio;

public class Revisao {
    private final String artigoId;
    private final Usuario revisor;
    private EstadoRevisao estado;
    private Parecer parecer;

    public Revisao(String artigoId, Usuario revisor) {
        if (artigoId == null || artigoId.isBlank()) {
            throw new IllegalArgumentException("ID do artigo e obrigatorio para revisao.");
        }
        if (revisor == null) {
            throw new IllegalArgumentException("Revisor obrigatorio.");
        }
        this.artigoId = artigoId;
        this.revisor = revisor;
        this.estado = EstadoRevisao.PENDENTE;
    }

    public void aceitar() {
        if (estado != EstadoRevisao.PENDENTE) {
            throw new IllegalStateException("A revisao so pode ser aceita quando estiver pendente.");
        }
        this.estado = EstadoRevisao.ACEITA;
    }

    public void concluir(Parecer parecer) {
        if (estado == EstadoRevisao.PENDENTE) {
            throw new IllegalStateException("O revisor deve aceitar a revisao antes de concluir.");
        }
        if (estado == EstadoRevisao.CONCLUIDA) {
            throw new IllegalStateException("Revisao ja concluida.");
        }
        this.parecer = parecer;
        this.estado = EstadoRevisao.CONCLUIDA;
    }

    public String getArtigoId() {
        return artigoId;
    }

    public Usuario getRevisor() {
        return revisor;
    }

    public EstadoRevisao getEstado() {
        return estado;
    }

    public Parecer getParecer() {
        return parecer;
    }
}
