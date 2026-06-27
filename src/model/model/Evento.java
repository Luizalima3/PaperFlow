package model;

import java.time.LocalDateTime;
import strategy.TipoEventoStrategy;

// Gerencia as informações do congresso atual e valida as datas limites de envio.

public class Evento {
    private final String nome;
    private final String cidade;
    private final String periodo;
    private final LocalDateTime dataLimiteSubmissao;
    private final TipoEventoStrategy categoria;
    private boolean aberto;

    public Evento(String nome, String cidade, String periodo, LocalDateTime dataLimite, TipoEventoStrategy categoria) {
        this.nome = nome;
        this.cidade = cidade;
        this.periodo = periodo;
        this.dataLimiteSubmissao = dataLimite;
        this.categoria = categoria;
        this.aberto = true; // Por padrão, o evento inicia de portas abertas
    }

    // Regra de negócio do RF05: Só aceita submissões se o evento estiver aberto E antes do prazo

    public boolean estaAberto() {
        return aberto && LocalDateTime.now().isBefore(dataLimiteSubmissao);
    }

    // Permite fechar o evento manualmente se o Chair (coordenador) quiser
    
    public void setAberto(boolean aberto) { this.aberto = aberto; }
    public String getNome() { return nome; }
    public TipoEventoStrategy getCategoria() { return categoria; }
}
