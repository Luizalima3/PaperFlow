package paperflow.aplicacao.acoes;

import paperflow.aplicacao.SistemaSubmissoes;

public class AcaoReenviarNotificacoes implements AcaoAdministrativa {
    private final String artigoId;

    public AcaoReenviarNotificacoes(String artigoId) {
        this.artigoId = artigoId;
    }

    @Override
    public void executar(SistemaSubmissoes sistema) {
        sistema.reenviarNotificacoesResultado(artigoId);
    }

    @Override
    public String descricao() {
        return "Reenviar notificacao de resultado do artigo " + artigoId;
    }
}
