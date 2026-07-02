package br.edu.ifpb.paperflow.infra.notificacao;

public class Email {
    private final String destinatario;
    private final String assunto;
    private final String corpo;

    private Email(Builder builder) {
        this.destinatario = builder.destinatario;
        this.assunto = builder.assunto;
        this.corpo = builder.corpo;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public String getAssunto() {
        return assunto;
    }

    public String getCorpo() {
        return corpo;
    }

    public static class Builder {
        private String destinatario;
        private String assunto;
        private String corpo;

        public Builder comDestinatario(String destinatario) {
            this.destinatario = destinatario;
            return this;
        }

        public Builder comAssunto(String assunto) {
            this.assunto = assunto;
            return this;
        }

        public Builder comCorpo(String corpo) {
            this.corpo = corpo;
            return this;
        }

        public Email build() {
            if (destinatario == null || destinatario.isBlank()) {
                throw new IllegalArgumentException("Destinatario do e-mail e obrigatorio.");
            }
            if (assunto == null || assunto.isBlank()) {
                throw new IllegalArgumentException("Assunto do e-mail e obrigatorio.");
            }
            if (corpo == null || corpo.isBlank()) {
                throw new IllegalArgumentException("Corpo do e-mail e obrigatorio.");
            }
            return new Email(this);
        }
    }
}
