package br.edu.ifpb.paperflow.infra.notificacao;

public abstract class GeradorEmail {
    public Email criarEmail(MensagemNotificacao mensagem) {
        String assunto = criarAssunto(mensagem);
        String corpo = criarCorpo(mensagem);
        return new Email.Builder()
                .comDestinatario(mensagem.getDestinatario())
                .comAssunto(assunto)
                .comCorpo(corpo)
                .build();
    }

    protected abstract String criarAssunto(MensagemNotificacao mensagem);

    protected abstract String criarCorpo(MensagemNotificacao mensagem);
}
