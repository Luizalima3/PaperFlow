package paperflow.infra.notificacao;

public class GeradorConviteRevisao extends GeradorEmail {
    @Override
    protected String criarAssunto(MensagemNotificacao mensagem) {
        return "Convite para revisar artigo " + mensagem.getArtigo().getId();
    }

    @Override
    protected String criarCorpo(MensagemNotificacao mensagem) {
        return new CorpoConviteRevisao(mensagem.getArtigo()).montarCorpo();
    }
}
