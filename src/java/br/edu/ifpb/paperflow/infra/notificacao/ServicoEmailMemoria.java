package br.edu.ifpb.paperflow.infra.notificacao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServicoEmailMemoria implements ServicoEmail {
    private final List<Email> caixaDeSaida = new ArrayList<>();

    @Override
    public void enviar(String destinatario, String assunto, String corpo) {
        caixaDeSaida.add(new Email.Builder()
                .comDestinatario(destinatario)
                .comAssunto(assunto)
                .comCorpo(corpo)
                .build());
    }

    public List<Email> getCaixaDeSaida() {
        return Collections.unmodifiableList(caixaDeSaida);
    }
}
