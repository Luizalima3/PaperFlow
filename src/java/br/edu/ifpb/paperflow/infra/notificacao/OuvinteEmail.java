package br.edu.ifpb.paperflow.infra.notificacao;

public class OuvinteEmail implements OuvinteNotificacao {
    private final ServicoEmail servicoEmail;
    private final GeradorEmail conviteEmail = new GeradorConviteRevisao();
    private final GeradorEmail resultadoEmail = new GeradorResultadoAutor();

    public OuvinteEmail(ServicoEmail servicoEmail) {
        this.servicoEmail = servicoEmail;
    }

    @Override
    public void receber(MensagemNotificacao mensagem) {
        Email email;
        if (mensagem.getTipo() == TipoNotificacao.CONVITE_REVISAO) {
            email = conviteEmail.criarEmail(mensagem);
        } else {
            email = resultadoEmail.criarEmail(mensagem);
        }
        servicoEmail.enviar(email.getDestinatario(), email.getAssunto(), email.getCorpo());
    }
}
