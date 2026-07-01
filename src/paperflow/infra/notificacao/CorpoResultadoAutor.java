package paperflow.infra.notificacao;

import paperflow.dominio.Artigo;
import paperflow.dominio.Parecer;
import paperflow.dominio.Revisao;

import java.util.List;

public class CorpoResultadoAutor extends CorpoEmailBase {
    private final Artigo artigo;
    private final List<Revisao> revisoes;

    public CorpoResultadoAutor(Artigo artigo, List<Revisao> revisoes) {
        this.artigo = artigo;
        this.revisoes = revisoes;
    }

    @Override
    protected String corpoPrincipal() {
        StringBuilder corpo = new StringBuilder();
        corpo.append("A submissao '").append(artigo.getTitulo()).append("' recebeu o resultado: ")
                .append(artigo.getNomeStatus()).append(".\n\n")
                .append("Pareceres anonimizados:\n");

        int indice = 1;
        for (Revisao revisao : revisoes) {
            Parecer parecer = revisao.getParecer();
            corpo.append("[Revisor ").append(indice++).append("]\n")
                    .append("Contribuicao: ").append(parecer.getContribuicao()).append("\n")
                    .append("Critica: ").append(parecer.getCritica()).append("\n")
                    .append("Veredito: ").append(parecer.getVeredito()).append("\n\n");
        }

        return corpo.toString();
    }
}
