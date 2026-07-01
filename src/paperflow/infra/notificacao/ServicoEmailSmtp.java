package paperflow.infra.notificacao;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class ServicoEmailSmtp implements ServicoEmail {
    private final String host;
    private final int porta;
    private final String usuario;
    private final String senha;

    public ServicoEmailSmtp(String host, int porta, String usuario, String senha) {
        this.host = host;
        this.porta = porta;
        this.usuario = usuario;
        this.senha = senha;
    }

    @Override
    public void enviar(String destinatario, String assunto, String corpo) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", String.valueOf(porta));
        props.put("mail.smtp.connectiontimeout", "5000");
        props.put("mail.smtp.timeout", "5000");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(usuario, senha);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(usuario));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(assunto);
            message.setText(corpo);

            Transport.send(message);
            System.out.println("[EMAIL ENVIADO] Para: " + destinatario);

        } catch (MessagingException e) {
            System.err.println("[ERRO EMAIL] " + e.getMessage());
            throw new RuntimeException("Falha ao enviar e-mail para " + destinatario, e);
        }
    }
    

}
