package paperflow.aplicacao.acoes;

import paperflow.aplicacao.SistemaSubmissoes;

public class AcaoReabrirSubmissao implements AcaoAdministrativa {
    @Override
    public void executar(SistemaSubmissoes sistema) {
        sistema.reabrirSubmissao();
    }

    @Override
    public String descricao() {
        return "Reabrir submissao do evento";
    }
}
