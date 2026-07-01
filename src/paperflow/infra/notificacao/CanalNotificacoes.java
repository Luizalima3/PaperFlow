package paperflow.infra.notificacao;

import java.util.ArrayList;
import java.util.List;

public class CanalNotificacoes implements CanalNotificacao {
    private final List<OuvinteNotificacao> ouvintes = new ArrayList<>();

    @Override
    public void adicionarOuvinte(OuvinteNotificacao ouvinte) {
        ouvintes.add(ouvinte);
    }

    @Override
    public void removerOuvinte(OuvinteNotificacao ouvinte) {
        ouvintes.remove(ouvinte);
    }

    @Override
    public void notificar(MensagemNotificacao mensagem) {
        for (OuvinteNotificacao ouvinte : ouvintes) {
            ouvinte.receber(mensagem);
        }
    }
}
