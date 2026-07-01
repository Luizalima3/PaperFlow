package paperflow.aplicacao.acoes;

import paperflow.aplicacao.SistemaSubmissoes;

public interface AcaoAdministrativa {
    void executar(SistemaSubmissoes sistema);
    String descricao();
}
