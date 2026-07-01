package paperflow.infra.notificacao;

public abstract class CorpoEmailBase {
    public final String montarCorpo() {
        return saudacao() + "\n\n" + corpoPrincipal() + "\n\n" + rodape();
    }

    protected String saudacao() {
        return "Prezado(a),";
    }

    protected abstract String corpoPrincipal();

    protected String rodape() {
        return "Equipe do evento.";
    }
}
