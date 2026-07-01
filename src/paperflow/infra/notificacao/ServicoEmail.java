package paperflow.infra.notificacao;

public interface ServicoEmail {
    void enviar(String destinatario, String assunto, String corpo);
}
