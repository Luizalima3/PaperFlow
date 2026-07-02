package br.edu.ifpb.paperflow.dominio;

import java.util.Collections;
import java.util.List;

public class DashboardResumo {
    private final int totalArtigos;
    private final int totalRevisores;
    private final int totalArtigosAvaliados;
    private final int totalPendentes;
    private final List<String> pendencias;

    public DashboardResumo(
            int totalArtigos,
            int totalRevisores,
            int totalArtigosAvaliados,
            int totalPendentes,
            List<String> pendencias) {
        this.totalArtigos = totalArtigos;
        this.totalRevisores = totalRevisores;
        this.totalArtigosAvaliados = totalArtigosAvaliados;
        this.totalPendentes = totalPendentes;
        this.pendencias = pendencias;
    }

    public int getTotalArtigos() {
        return totalArtigos;
    }

    public int getTotalRevisores() {
        return totalRevisores;
    }

    public int getTotalArtigosAvaliados() {
        return totalArtigosAvaliados;
    }

    public int getTotalPendentes() {
        return totalPendentes;
    }

    public List<String> getPendencias() {
        return Collections.unmodifiableList(pendencias);
    }
}
