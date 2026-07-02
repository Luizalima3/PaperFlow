package br.edu.ifpb.paperflow.infra.notificacao;

import br.edu.ifpb.paperflow.dominio.Artigo;

public class CorpoConviteRevisao extends CorpoEmailBase {
    private final Artigo artigo;

    public CorpoConviteRevisao(Artigo artigo) {
        this.artigo = artigo;
    }

    @Override
    protected String corpoPrincipal() {
        return "Voce recebeu o artigo '" + artigo.getTitulo() + "' para avaliacao.\n"
                + "Acesse o sistema para aceitar e concluir a revisao.";
    }
}
