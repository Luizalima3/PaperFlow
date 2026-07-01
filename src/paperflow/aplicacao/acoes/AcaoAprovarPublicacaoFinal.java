package paperflow.aplicacao.acoes;

import paperflow.aplicacao.SistemaSubmissoes;

public class AcaoAprovarPublicacaoFinal implements AcaoAdministrativa {
    private final String artigoId;

    public AcaoAprovarPublicacaoFinal(String artigoId) {
        this.artigoId = artigoId;
    }

    @Override
    public void executar(SistemaSubmissoes sistema) {
        sistema.aprovarPublicacaoFinal(artigoId);
    }

    @Override
    public String descricao() {
        return "Aprovar publicacao final do artigo " + artigoId;
    }
}
