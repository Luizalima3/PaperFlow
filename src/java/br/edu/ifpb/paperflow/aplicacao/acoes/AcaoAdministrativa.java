package br.edu.ifpb.paperflow.aplicacao.acoes;

import br.edu.ifpb.paperflow.aplicacao.SistemaSubmissoes;

public interface AcaoAdministrativa {
    void executar(SistemaSubmissoes sistema);
    String descricao();
}
