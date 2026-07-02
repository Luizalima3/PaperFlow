package br.edu.ifpb.paperflow.infra.notificacao;

public interface CanalNotificacao {
    void adicionarOuvinte(OuvinteNotificacao ouvinte);
    void removerOuvinte(OuvinteNotificacao ouvinte);
    void notificar(MensagemNotificacao mensagem);
}
