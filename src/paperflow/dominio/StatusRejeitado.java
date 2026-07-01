package paperflow.dominio;

public class StatusRejeitado implements StatusArtigo {
    @Override
    public String getNome() {
        return "Rejeitado";
    }
}
