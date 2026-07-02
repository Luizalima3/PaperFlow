package br.edu.ifpb.paperflow.aplicacao.acoes;

import br.edu.ifpb.paperflow.aplicacao.SistemaSubmissoes;

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
