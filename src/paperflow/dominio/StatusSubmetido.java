package paperflow.dominio;

// Esta é a classe concreta que implementa o estado inicial do artigo. Ela diz ao sistema que o artigo está no estado "Submetido".

public class StatusSubmetido implements StatusArtigo {
    @Override
    public String getNome() {
        return "Submetido";
    }
}
