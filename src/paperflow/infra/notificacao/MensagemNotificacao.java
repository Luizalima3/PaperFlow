package paperflow.infra.notificacao;

import paperflow.dominio.Artigo;
import paperflow.dominio.Revisao;

import java.util.Collections;
import java.util.List;

public class MensagemNotificacao {
    private final TipoNotificacao tipo;
    private final String destinatario;
    private final Artigo artigo;
    private final List<Revisao> revisoes;

    public MensagemNotificacao(TipoNotificacao tipo, String destinatario, Artigo artigo, List<Revisao> revisoes) {
        this.tipo = tipo;
        this.destinatario = destinatario;
        this.artigo = artigo;
        this.revisoes = revisoes == null ? Collections.emptyList() : revisoes;
    }

    public TipoNotificacao getTipo() {
        return tipo;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public Artigo getArtigo() {
        return artigo;
    }

    public List<Revisao> getRevisoes() {
        return Collections.unmodifiableList(revisoes);
    }
}
