package br.edu.ifpb.paperflow.infra.notificacao;

public class GeradorResultadoAutor extends GeradorEmail {
    @Override
    protected String criarAssunto(MensagemNotificacao mensagem) {
        return "Resultado da submissao " + mensagem.getArtigo().getId() + " - " + mensagem.getArtigo().getNomeStatus();
    }

    @Override
    protected String criarCorpo(MensagemNotificacao mensagem) {
        return new CorpoResultadoAutor(mensagem.getArtigo(), mensagem.getRevisoes()).montarCorpo();
    }
}
