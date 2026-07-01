package paperflow.dominio;

public class Parecer {
    private final String contribuicao;
    private final String critica;
    private final Veredito veredito;

    public Parecer(String contribuicao, String critica, Veredito veredito) {
        if (contribuicao == null || contribuicao.isBlank()) {
            throw new IllegalArgumentException("Contribuicao do parecer e obrigatoria.");
        }
        if (critica == null || critica.isBlank()) {
            throw new IllegalArgumentException("Critica do parecer e obrigatoria.");
        }
        if (veredito == null) {
            throw new IllegalArgumentException("Veredito do parecer e obrigatorio.");
        }
        this.contribuicao = contribuicao;
        this.critica = critica;
        this.veredito = veredito;
    }

    public String getContribuicao() {
        return contribuicao;
    }

    public String getCritica() {
        return critica;
    }

    public Veredito getVeredito() {
        return veredito;
    }
}
