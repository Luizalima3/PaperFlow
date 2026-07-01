package paperflow.dominio;

public enum Veredito {
    RECUSADO(1),
    FRACAMENTE_RECUSADO(2),
    FRACAMENTE_ACEITO(3),
    ACEITO(4);

    private final int pontuacao;

    Veredito(int pontuacao) {
        this.pontuacao = pontuacao;
    }

    public int getPontuacao() {
        return pontuacao;
    }
}
