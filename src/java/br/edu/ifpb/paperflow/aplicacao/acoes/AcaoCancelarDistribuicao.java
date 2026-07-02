package br.edu.ifpb.paperflow.aplicacao.acoes;

import br.edu.ifpb.paperflow.aplicacao.SistemaSubmissoes;

public class AcaoCancelarDistribuicao implements AcaoAdministrativa {
    private final String artigoId;

    public AcaoCancelarDistribuicao(String artigoId) {
        this.artigoId = artigoId;
    }

    @Override
    public void executar(SistemaSubmissoes sistema) {
        sistema.cancelarDistribuicao(artigoId);
    }

    @Override
    public String descricao() {
        return "Cancelar distribuicao do artigo " + artigoId;
    }
}
